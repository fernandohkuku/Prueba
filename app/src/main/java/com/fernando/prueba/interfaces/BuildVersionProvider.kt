package com.fernando.prueba.interfaces

interface BuildVersionProvider {
    fun isMarshmallowAndAbove():Boolean
    fun isLollipopAndAbove():Boolean
    fun isAndroidRAndAbove():Boolean
    fun isAndroidNAndAbove():Boolean
}