package com.example.lifecycle.utils

import com.chibatching.kotpref.KotprefModel
import com.photo.utils.*


object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME

    var isFistTime by booleanPref(true)
    var mSaturation by floatPref(255f)
    var mLum by floatPref(0.5f)

    var mHuePos by intPref(0)
    var mSaturationPos by intPref(127)
    var mLumPos by intPref(127)

    var defaultTraces by intPref(1500)

}