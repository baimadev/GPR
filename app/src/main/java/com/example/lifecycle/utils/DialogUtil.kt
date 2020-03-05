package com.photo.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.LongDef
import androidx.annotation.StringRes
import com.example.lifecycle.R
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.atomic.AtomicInteger

object DialogUtil {

    const val CALLBACK_TYPE_CANCEL = 0x1L
    const val CALLBACK_TYPE_OK = 0x1L shl 1
    const val CALLBACK_TYPE_RETRY = 0x1L shl 2
    const val CALLBACK_TYPE_DISMISS = 0x1L shl 3
    const val CALLBACK_TYPE_CLOSE = 0x1L shl 4

    private var progressDialog: Dialog? = null
    private val dialogCount = AtomicInteger(0)
    private val dialogContainer = mutableSetOf<AlertDialog>()

    @LongDef(
        CALLBACK_TYPE_CANCEL,
        CALLBACK_TYPE_OK,
        CALLBACK_TYPE_RETRY,
        CALLBACK_TYPE_DISMISS,
        CALLBACK_TYPE_CLOSE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class CallbackType

    @JvmOverloads
    fun showProgressDialogNow(context: Context?, isCancelable: Boolean = false): Dialog {
        val dialog = createFullDialog(
            context,
            R.layout.loading_layout,
            true,
            isCancelable
        )
        dialog.show()
        return dialog
    }

    fun <T> composeNetProgressDialog(context: Context): SingleTransformer<T, T> {
        return SingleTransformer<T, T> {
            it
                .doOnSubscribe { showProgressDialog(context) }
                .doOnDispose { hideProgressDialog() }
                .doOnError { DialogUtil.hideProgressDialog() }
                .doOnSuccess { DialogUtil.hideProgressDialog() }
        }
    }

    fun <T> composeNetProgressDialogObs(context: Context): ObservableTransformer<T, T> {
        return ObservableTransformer<T, T> {
            it
                .doOnSubscribe { showProgressDialog(context) }
                .doOnDispose { hideProgressDialog() }
                .doOnError { DialogUtil.hideProgressDialog() }
                .doOnComplete { DialogUtil.hideProgressDialog() }
        }
    }

    fun composeNetProgressDialogCompletable(context: Context): CompletableTransformer {
        return CompletableTransformer { obs ->
            obs
                .doOnSubscribe { showProgressDialog(context) }
                .doOnDispose { hideProgressDialog() }
                .doOnError { t -> DialogUtil.hideProgressDialog() }
                .doOnComplete { hideProgressDialog() }
        }
    }

    fun <T> composeNetProgressDialogMaybe(context: Context): MaybeTransformer<T, T> {
        return MaybeTransformer<T, T> { obs ->
            obs
                .doOnSubscribe { showProgressDialog(context) }
                .doOnDispose { hideProgressDialog() }
                .doOnError { DialogUtil.hideProgressDialog() }
                .doOnComplete { hideProgressDialog() }
        }
    }

    @JvmOverloads
    fun showProgressDialog(context: Context?, isCancelable: Boolean = false) {
        if (progressDialog != null)
            return
        if (context == null) {
            dialogCount.incrementAndGet()
            return
        }
        AndroidSchedulers.mainThread().createWorker().schedule {
            if (progressDialog == null && dialogCount.get() == 0) {
                progressDialog = DialogUtil.showProgressDialogNow(context, isCancelable)
            }
            dialogCount.incrementAndGet()
        }
    }

    /**
     * May not going to dismiss the ProgressDialog if dialogCount > 1.
     */

    fun hideProgressDialog() {
        AndroidSchedulers.mainThread().createWorker().schedule {
            if (dialogCount.get() > 0)
                dialogCount.decrementAndGet()
            if (progressDialog != null && dialogCount.get() == 0) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        }
    }

    /**
     * Force to dismiss the ProgressDialog with ignoring the dialogCount.
     */

    fun hideAllProgressDialog() {
        AndroidSchedulers.mainThread().createWorker().schedule {
            if (dialogCount.get() > 0) {
                dialogCount.set(0)
                progressDialog!!.dismiss()
                progressDialog = null
            }
        }
    }

    @JvmOverloads
    fun createFullDialog(
        context: Context?,
        @LayoutRes layout: Int,
        clearBackground: Boolean = false, isCancelable: Boolean = false
    ): Dialog {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        if (window != null) {
            if (clearBackground)
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        }
        dialog.setContentView(layout)
        dialog.setCancelable(isCancelable)

        return dialog
    }

    /**
     * for MessageDialog
     */

    //create ok button dialog
    fun showMessageDialog(
        context: Context,
        title: CharSequence?,
        message: CharSequence
    ): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_button_ok_en, null)
            .show()
            .bindLife()
    }

    //create custom button dialog
    fun showMessageDialog(
        context: Context,
        title: CharSequence?,
        message: CharSequence,
        @CallbackType vararg buttons: Long,
        callback: (DialogEvent) -> Unit
    ): Dialog {

        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_button_ok_en, null)

        configureBtn(builder, callback, buttons)

        return builder.show().bindLife()
    }

    //create custom button dialog with return single
    fun showMessageDialogObs(
        context: Context,
        title: CharSequence,
        message: CharSequence,
        @CallbackType vararg buttons: Long
    ): Single<DialogEvent> {

        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)

        if (buttons.isEmpty())
            return Single.create<DialogEvent> { emitter ->
                builder.setPositiveButton(R.string.dialog_button_ok_en)
                { d, _ -> emitter.onSuccess(DialogEvent(d, CALLBACK_TYPE_OK)) }
            }

        return Single.create<DialogEvent> { emitter ->
            buttons.forEach {
                when (it) {
                    CALLBACK_TYPE_OK ->
                        builder.setPositiveButton(R.string.dialog_button_cancel_jp)
                        { d, _ -> emitter.onSuccess(DialogEvent(d, CALLBACK_TYPE_OK)) }
                    CALLBACK_TYPE_RETRY ->
                        builder.setPositiveButton(R.string.dialog_button_retry_jp)
                        { d, _ -> emitter.onSuccess(DialogEvent(d, CALLBACK_TYPE_RETRY)) }
                    CALLBACK_TYPE_CANCEL ->
                        builder.setNegativeButton(R.string.dialog_button_cancel_jp)
                        { d, _ -> emitter.onSuccess(DialogEvent(d, CALLBACK_TYPE_CANCEL)) }
                    CALLBACK_TYPE_CLOSE ->
                        builder.setNegativeButton(R.string.dialog_button_close)
                        { d, _ -> emitter.onSuccess(DialogEvent(d, CALLBACK_TYPE_CLOSE)) }
                }
            }
            builder.show()
        }
    }


    /**
     * for DealErrorUtil
     */

//    fun <F> showMessageDialogObs(
//        AC: Async<F>,
//        context: Context,
//        title: CharSequence,
//        message: CharSequence,
//        @CallbackType vararg buttons: Long
//    ): HK<F, DialogEvent> {
//        return showMessageDialogObs(AC, context, title, message, true, *buttons)
//    }
//
//    fun <F> showMessageDialogObs(
//        AC: Async<F>, context: Context,
//        title: CharSequence,
//        message: CharSequence,
//        cancelable: Boolean,
//        @CallbackType vararg buttons: Long
//    ): HK<F, DialogEvent> {
//        val builder = AlertDialog.Builder(context)
//            .setTitle(title)
//            .setMessage(message)
//
//        return AC.async<DialogEvent> { emitter ->
//            configureBtn(
//                builder,
//                { emitter(it.right()) },
//                buttons
//            )
//            builder.setOnCancelListener { dialog ->
//                emitter(
//                    DialogEvent(
//                        dialog, CALLBACK_TYPE_DISMISS
//                    ).right()
//                )
//            }
//
//            builder.setCancelable(cancelable)
//            builder.show().bindLife()
//        }
//    }


    fun showDialogSingle(
        context: Context,
        title: String = "",
        msg: String = "",
        cancelable: Boolean = true,
        @StringRes negativeButtonText: Int? = null,
        @StringRes positiveButtonText: Int = R.string.dialog_button_ok_en,
        @CallbackType negativeButton: Long? = null,
        @CallbackType positiveButton: Long = CALLBACK_TYPE_OK
    ): Single<DialogEvent> {
        return Single.create<DialogEvent> { emitter ->
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positiveButtonText)
                { _, _ -> emitter.onSuccess(DialogEvent(button = positiveButton)) }
                .apply {
                    if (negativeButton != null && negativeButtonText != null)
                        setNegativeButton(negativeButtonText)
                        { _, _ -> emitter.onSuccess(DialogEvent(button = negativeButton)) }
                }
                .setCancelable(cancelable)
                .show()
                .bindLife()
        }
    }

    private fun configureBtn(
        builder: AlertDialog.Builder,
        callback: (DialogEvent) -> Unit,
        buttons: LongArray
    ) {
        if (buttons.isEmpty()) {
            builder.setPositiveButton(R.string.dialog_button_ok_en, null)
            return
        }

        for (btn in buttons) {
            if (checkFlag(btn, CALLBACK_TYPE_CANCEL))
                builder.setNegativeButton(
                    R.string.dialog_button_cancel_jp
                ) { dialog, which -> callback(DialogEvent(dialog, CALLBACK_TYPE_CANCEL)) }
            else if (checkFlag(btn, CALLBACK_TYPE_OK))
                builder.setPositiveButton(
                    R.string.dialog_button_ok_en
                ) { dialog, which ->
                    callback(DialogEvent(dialog, CALLBACK_TYPE_OK))
                    dialog.dismiss()
                }
            else if (checkFlag(btn, CALLBACK_TYPE_RETRY))
                builder.setPositiveButton(
                    R.string.dialog_button_retry_jp
                ) { dialog, which -> callback(DialogEvent(dialog, CALLBACK_TYPE_RETRY)) }
            else if (checkFlag(btn, CALLBACK_TYPE_CLOSE))
                builder.setNegativeButton(R.string.dialog_button_close) { dialog, which ->
                    callback(DialogEvent(dialog, CALLBACK_TYPE_CLOSE))
                }
        }
    }

    private fun checkFlag(flag: Long, target: Long): Boolean {
        return flag and target != 0L
    }

    private fun AlertDialog.bindLife(): AlertDialog {
        dialogContainer.add(this)
        this.setOnDismissListener { dialogContainer.remove(this) }
        return this
    }

    //dismiss all dialogs except progress dialog
    fun dismissAllDialogs() {
        dialogContainer.apply {
            forEach { it.dismiss() }
            clear()
        }
    }

    data class DialogEvent(
        val dialog: DialogInterface? = null,
        @CallbackType
        val button: Long = 0
    )
}

//Rx extensions

fun <T> Single<T>.netProgressDialog(context: Context) =
    compose(DialogUtil.composeNetProgressDialog(context))

fun Completable.netProgressDialog(context: Context) =
    compose(DialogUtil.composeNetProgressDialogCompletable(context))

fun <T> Maybe<T>.netProgressDialog(context: Context) =
    compose(DialogUtil.composeNetProgressDialogMaybe(context))

fun <T> Observable<T>.netProgressDialog(context: Context) =
    compose(DialogUtil.composeNetProgressDialogObs(context))
