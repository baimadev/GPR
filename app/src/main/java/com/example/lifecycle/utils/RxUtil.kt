package com.photo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.example.lifecycle.BuildConfig
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Action
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher
import java.io.File


//Rx extensions

fun <T> Single<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Single<T> = subscribeOn(subscribeOn).observeOn(observeOn)

fun Completable.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Completable = subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Maybe<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Maybe<T> = subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Flowable<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> = subscribeOn(subscribeOn).observeOn(observeOn)

object RxUtil {

    class SwitchTransform<T> internal constructor(
        val subscribeOn: Scheduler,
        val observeOn: Scheduler
    ) : ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>,
        SingleTransformer<T, T>,
        CompletableTransformer {

        override fun apply(upstream: Completable): CompletableSource {
            return upstream.subscribeOn(subscribeOn)
                .observeOn(observeOn)
        }

        override fun apply(upstream: Flowable<T>): Publisher<T> {
            return upstream.subscribeOn(subscribeOn)
                .observeOn(observeOn)
        }

        override fun apply(upstream: Maybe<T>): MaybeSource<T> {
            return upstream.subscribeOn(subscribeOn)
                .observeOn(observeOn)
        }

        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream.subscribeOn(subscribeOn)
                .observeOn(observeOn)
        }

        override fun apply(upstream: Single<T>): SingleSource<T> {
            return upstream.subscribeOn(subscribeOn)
                .observeOn(observeOn)
        }
    }


    fun <T> emptyAction(): Consumer<T> = Consumer { }

    fun emptyAction0(): Action = Action {}

    fun <T> identity(): (T) -> T = { it }

    fun <T> rxToCall(f: (Observable<T>) -> Unit): (T) -> Unit {
        val subject = PublishSubject.create<T>().toSerialized()
        f(subject)
        return { subject.onNext(it) }
    }

    fun <T> callToRx(setter: (((T) -> Unit)?) -> Unit): Observable<T> =
        Observable.create<T> { emmit ->
            setter { item -> emmit.onNext(item) }
        }.doOnDispose { setter(null) }

    fun <T, R> lastChecker(accumulator: BiFunction<T, T, R>): ObservableOperator<R, T> {
        ObjectHelper.requireNonNull(accumulator, "accumulator is null")
        return ObservableOperator { obs -> ObserverLastChecker<T, R>(obs, accumulator) }
    }

    fun <T> fromUI(): SwitchTransform<T> {
        return switchThread(AndroidSchedulers.mainThread(), Schedulers.io())
    }

    fun <T> switchThread(
        subscribeOn: Scheduler = Schedulers.io(),
        observeOn: Scheduler = AndroidSchedulers.mainThread()
    ): SwitchTransform<T> {
        return SwitchTransform(subscribeOn, observeOn)
    }

    fun postUI(runnable: Runnable) {
        AndroidSchedulers.mainThread().createWorker().schedule(runnable)
    }


    fun clearCache(context: Context) {
        TempPath.clear(context)
    }


    private fun getTempFileName(fileName: String, extension: String): String {
        var file =
            if (TextUtils.isEmpty(fileName)) System.currentTimeMillis().toString() else fileName
        val ext = "." + if (TextUtils.isEmpty(extension)) "tmp" else extension
        if (!file.endsWith(ext))
            file = file + ext
        return file
    }


internal class ObserverLastChecker<Downstream, T>(
    val actual: Observer<in T>,
    val accumulator: BiFunction<Downstream, Downstream, T>
) : Observer<Downstream>, Disposable {

    private var s: Disposable? = null

    private var value: Downstream? = null

    private var done: Boolean = false

    override fun onSubscribe(s: Disposable) {
        if (DisposableHelper.validate(this.s, s)) {
            this.s = s
            actual.onSubscribe(this)
        }
    }

    override fun dispose() {
        s!!.dispose()
    }

    override fun isDisposed(): Boolean = s!!.isDisposed

    override fun onNext(t: Downstream) {
        if (done) {
            return
        }
        val a = actual
        val v = value
        if (v == null) {
            value = t
        } else {
            val u: T

            try {
                u = ObjectHelper.requireNonNull(
                    accumulator.apply(v, t),
                    "The value returned by the accumulator is null"
                )
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                s!!.dispose()
                onError(e)
                return
            }

            value = t
            a.onNext(u)
        }
    }

    override fun onError(t: Throwable) {
        if (done) {
            RxJavaPlugins.onError(t)
            return
        }
        done = true
        value = null
        actual.onError(t)
    }

    override fun onComplete() {
        if (done) {
            return
        }
        done = true
        value = null
        actual.onComplete()
    }
}

internal sealed class TempPath {

    internal class ExPath(val exDir: File) : TempPath() {
        public override fun createFile(fileName: String): File {
            return File(exDir, fileName)
        }
    }

    internal class InPath(val inDir: File) : TempPath() {
        public override fun createFile(fileName: String): File {
            return File(inDir, fileName)
        }
    }

    internal abstract fun createFile(fileName: String): File

    companion object {
        private val LOCAL_DIR_PATH = "temp"
        private val EXTERNAL_DIR_PATH = "Android/data/${BuildConfig.APPLICATION_ID}/cache/temp"

        fun unit(context: Context): TempPath =
            getExDir()?.let { ExPath(it) } ?: InPath(getInDir(context)!!)

        fun clear(context: Context) {
            getExDir()?.let { delete(it) }
            getInDir(context)?.let { delete(it) }
        }

        fun delete(file: File) {
            if (file.isFile) {
                file.delete()
                return
            }

            if (file.isDirectory) {
                val childFiles = file.listFiles()
                if (childFiles == null || childFiles.size == 0) {
                    file.delete()
                    return
                }

                for (i in childFiles.indices) {
                    delete(childFiles[i])
                }
                file.delete()
            }
        }

        fun getExDir(): File? {
            var fileDir: File? = null
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED,
                    ignoreCase = true
                )
            ) {
                try {
                    val f = Environment.getExternalStorageDirectory()
                    fileDir = File(f, EXTERNAL_DIR_PATH)
                    if (!fileDir.exists()) {
                        if (!fileDir.mkdirs())
                            fileDir = null
                    }
                } catch (e: Exception) {
                    fileDir = null
                }

            }
            return fileDir
        }

        fun getInDir(context: Context): File? {
            var fileDir: File?
            try {
                fileDir = context.getDir(LOCAL_DIR_PATH, Activity.MODE_PRIVATE)
                if (!fileDir!!.exists()) {
                    if (!fileDir.mkdirs())
                        fileDir = null
                }
            } catch (e: Exception) {
                fileDir = null
            }

            return fileDir
        }
    }
}
}