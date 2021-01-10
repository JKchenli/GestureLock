package com.cl.gesturelock.util

import android.content.Context
import com.cl.gesturelock.R

/**
 * @author Chenli
 * @Date 2021/1/7
 * @Description
 */
abstract class AbstractLockHelper {
    /**
     * 最大错误次数
     */
    var maxErrorNum: Int = 0

    /**
     * 当前错误次数
     */
    private var errorNum: Int = 0

    /**
     * 是否处于错误状态
     */
    var isError: Boolean = false

    /**
     * 是否处于解锁失败状态
     */
    var isUnlockError: Boolean = false

    /**
     * 最低触碰数量
     */
    var minTouchNum: Int = 0

    /**
     * 加密的触碰list
     */
    var encryptContent: String? = null

    /**
     * 状态信息
     */
    var statusMessage: String = ""

    /**
     * 解锁模式
     */
    private var lockMode: Int = SETTING

    /**
     * 上下文
     */
    private var context: Context? = null

    /**
     * 加密方式
     */
    abstract fun encrypt(content: String): String

    /**
     * 解密方式
     */
    abstract fun decrypt(encryptContent: String): String

    /**
     * 设置为设置手势锁模式
     */
    fun isSettingMode() {
        lockMode = SETTING
        isUnlockError = false
        encryptContent = null
    }

    /**
     * 设置为验证手势锁模式
     */
    fun isVerifyMode(encryptContent: String) {
        lockMode = VERIFY
        isUnlockError = false
        this.encryptContent = encryptContent
    }

    /**
     * 检测触碰结果
     */
    fun checkInput(touchList: ArrayList<Int>) {
        if (touchList.size < minTouchNum) {//少于最低触碰数量
            isError = true
            statusMessage =
                String.format(context?.getString(R.string.inputNotEnough).toString(), minTouchNum)
            return
        }
        val content = touchList.toString()
        when (encryptContent) {
            null -> {//Setting模式中首次未设置加密内容时
                encryptContent = encrypt(content)
                statusMessage = context?.getString(R.string.setting).toString()
            }
            encrypt(content) -> {//第二次触碰结果与加密结果相同
                when (lockMode) {
                    SETTING -> {//Setting 模式 设置成功
                        statusMessage = context?.getString(R.string.setting_success).toString()
                    }
                    VERIFY -> {//Verify 模式 验证成功
                        errorNum = 0
                        statusMessage = context?.getString(R.string.verify_success).toString()
                    }
                }
            }
            else -> {//第二次触碰结果与加密结果不同
                isError = true//设置为触碰错误状态
                when (lockMode) {
                    SETTING -> {//Setting 模式 设置失败
                        statusMessage = context?.getString(R.string.setting_failure).toString()
                        encryptContent = null
                    }
                    VERIFY -> {//Verify 模式 验证失败
                        errorNum++ //更新当前错误次数
                        statusMessage =
                            String.format(
                                context?.getString(R.string.verify_failure).toString(),
                                maxErrorNum - errorNum
                            )
                        if (errorNum == maxErrorNum) {//当前错误次数与最大错误次数一致时。设置为解锁失败状态
                            isUnlockError = true
                        }
                    }
                }
            }
        }
    }

    fun attached(context: Context?) {
        this.context = context
    }

    fun detached() {
        this.context = null
    }

    companion object {
        /**
         * Cell状态
         */
        const val SETTING = 1
        const val VERIFY = 2
    }
}