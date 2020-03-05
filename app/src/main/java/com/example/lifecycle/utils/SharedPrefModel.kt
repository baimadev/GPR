package com.example.lifecycle.utils

import com.chibatching.kotpref.KotprefModel
import com.photo.utils.*


object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME

    var isFistTime by booleanPref(true)

    var durationTextSize by floatPref(12f)

    var dialogDisplay by booleanPref(true)

    //last Google Play review popup date after order complete (yyyy/MM/dd)
    var lastReviewPopupDate by stringPref()

    //app.json
    var topBgImageUrl by stringPref()
    var informations by nullableStringPref()
    var maintenance by nullableStringPref()
    var enableFaceBook by booleanPref(false)

    var template by nullableStringPref()
    var authorization by nullableStringPref()
    var templateUpdated by nullableStringPref()



}