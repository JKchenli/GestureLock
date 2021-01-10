package com.cl.gesturelock.util

/**
 * @author Chenli
 * @Date 2021/1/4
 * @Description
 */
class LockSettingUtil private constructor() {

    companion object {
        val instance = LockSettingUtilHolder.holder
    }

    private object LockSettingUtilHolder {
        val holder = LockSettingUtil()
    }

    var saveLock: String = ""
    var rowNum: Int = 3
    var columnNum: Int = 3
    var lineWidth: Int = 2
    var darkMode: Boolean = true
    var style: Boolean = true
    var enableFeedback: Boolean = false

}