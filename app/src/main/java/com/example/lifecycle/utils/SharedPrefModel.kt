package com.example.lifecycle.utils

import com.chibatching.kotpref.KotprefModel
import com.photo.utils.*


object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME

    var isFistTime by booleanPref(true)
    var mSaturation by floatPref(1f)
    var mLum by floatPref(1f)
    var mTruncation by intPref(50)

    var samples by intPref(0)
    var samples2 by intPref(0)

    var lastTrace by intPref(0)

    var col by intPref(0)
    var col2 by intPref(0)

    var timeWindow by floatPref(0f)
    var distanceInterval by floatPref(0f)

    var mHuePos by intPref(0)
    var mSaturationPos by intPref(127)
    var mLumPos by intPref(127)

    var mMidLinePos by intPref(Constants.DefaultTraces/2)



}