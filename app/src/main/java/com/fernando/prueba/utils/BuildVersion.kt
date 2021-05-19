package com.fernando.prueba.utils

import android.os.Build
import com.fernando.prueba.interfaces.BuildVersionProvider

class BuildVersion:BuildVersionProvider {
    override fun isMarshmallowAndAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    override fun isLollipopAndAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    override fun isAndroidRAndAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    override fun isAndroidNAndAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }
}