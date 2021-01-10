package com.cl.gesturelock.impl

import android.graphics.Canvas
import android.graphics.Paint
import com.cl.gesturelock.bean.LockCellBean

/**
 * @author Chenli
 * @Date 2020/12/22
 * @Description
 */
interface ILockCellView {
    /**
     * 绘制无触碰状态下Cell
     */
    fun drawNormal(
        canvas: Canvas,
        cellBean: LockCellBean,
        paint: Paint
    )

    /**
     * 绘制触碰状态下Cell
     */
    fun drawTouch(
        canvas: Canvas,
        cellBean: LockCellBean,
        nextCellBean: LockCellBean?,
        paint: Paint
    )

    /**
     * 绘制触碰错误状态下Cell
     */
    fun drawError(
        canvas: Canvas,
        cellBean: LockCellBean,
        nextCellBean: LockCellBean?,
        paint: Paint
    )


}