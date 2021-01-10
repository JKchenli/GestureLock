package com.cl.gesturelock.bean

import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author Chenli
 * @Date 2020/12/21
 * @Description
 */
data class LockCellBean(
    val id: Int,
    val x: Float,
    val y: Float,
    val radius: Float,
    var isTouch: Boolean = false
) {
    /**
     * 检测是否触碰到Cell
     */
    fun touchInside(x: Float, y: Float): Boolean {
        return sqrt((this.x - x).pow(2) + (this.y - y).pow(2)) < radius
    }

    /**
     * 获取下一个坐标方位Angle
     */
    fun getAngle(nextX: Float, nextY: Float): Float {
        val dx = nextX - this.x
        val dy = nextY - this.y
        var angle = atan(dy / dx)
        if (dx >= 0 && dy < 0) {//第4象限(角度为-0~-90，sin为负 cos为正)
            angle += Math.PI.toFloat() * 2
        } else if (dx < 0 && dy <= 0) {//第3象限(角度为0~90，sin为正 cos为正)
            angle += Math.PI.toFloat()
        } else if (dx < 0 && dy > 0) {//第2象限(角度为0~-90，sin为负 cos为正)
            angle += Math.PI.toFloat()
        } else if (dx >= 0 && dy >= 0) {//第1象限(角度为0~90，sin为正 cos为正)
            angle
        }
        return angle
    }
}