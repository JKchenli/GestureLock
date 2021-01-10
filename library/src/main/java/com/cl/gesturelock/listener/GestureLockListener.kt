package com.cl.gesturelock.listener

/**
 * @author Chenli
 * @Date 2021/1/3
 * @Description
 */
interface GestureLockListener {

    /**
     * 触碰开始时
     */
    fun onStart() {}

    /**
     * touchList发生改变，即当触碰到下一个Cell时
     * @param touchList 触摸list
     */
    fun onChange(touchList: ArrayList<Int>) {}

    /**
     * 触碰结束时
     * @param touchList 本次完成后的触摸list
     */
    fun onComplete(touchList: ArrayList<Int>, encryptContent: String) {}

    /**
     * 状态改变时消息反馈
     * @param statusMessage 反馈的消息
     */
    fun onStatusChange(statusMessage: String) {}

    /**
     * 解锁失败
     */
    fun onUnlockError() {}

}