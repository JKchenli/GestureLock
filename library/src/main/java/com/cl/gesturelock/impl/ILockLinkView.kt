package com.cl.gesturelock.impl

import android.graphics.Canvas
import android.graphics.Paint
import android.util.SparseArray
import com.cl.gesturelock.bean.LockCellBean

/**
 * @author Chenli
 * @Date 2020/12/22
 * @Description
 */
interface ILockLinkView {
    /**
     * 绘制触碰状态下连线
     */
    fun drawTouch(
        canvas: Canvas,
        touchList: ArrayList<Int>,
        cellBean: SparseArray<LockCellBean>,
        endX: Float,
        endY: Float,
        paint: Paint
    )

    /**
     * 绘制触碰错误状态下连线
     */
    fun drawError(
        canvas: Canvas,
        touchList: ArrayList<Int>,
        cellBean: SparseArray<LockCellBean>,
        paint: Paint
    )

}