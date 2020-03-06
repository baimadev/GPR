package com.example.lifecycle.utils

import com.chibatching.kotpref.KotprefModel
import com.photo.utils.*


object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME

    var isFistTime by booleanPref(true)
    var mHue by floatPref(1f)
    var mSaturation by floatPref(1f)
    var mLum by floatPref(1f)

    var mHuePos by intPref(127)
    var mSaturationPos by intPref(127)
    var mLumPos by intPref(127)



}