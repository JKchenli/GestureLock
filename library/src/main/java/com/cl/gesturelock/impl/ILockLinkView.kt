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
     * @param canvas
     * @param touchList
     * @param cellBean
     * @param endX 手指位置X
     * @param endY 手指位置Y
     * @param paint
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
     * @param canvas
     * @param touchList
     * @param cellBean
     * @param paint
     */
    fun drawError(
        canvas: Canvas,
        touchList: ArrayList<Int>,
        cellBean: SparseArray<LockCellBean>,
        paint: Paint
    )

}