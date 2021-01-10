package com.cl.gesturelock.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.cl.gesturelock.bean.LockCellBean
import com.cl.gesturelock.impl.ILockCellView

/**
 * @author Chenli
 * @Date 2021/1/10
 * @Description
 */
class SimpleLockCellView : ILockCellView {
    override fun drawNormal(canvas: Canvas, cellBean: LockCellBean, paint: Paint) {
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, paint)
        paint.style = Paint.Style.FILL
        paint.alpha = 88
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3 * 2, paint)
    }

    override fun drawTouch(
        canvas: Canvas,
        cellBean: LockCellBean,
        nextCellBean: LockCellBean?,
        paint: Paint
    ) {
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, paint)
        paint.style = Paint.Style.FILL
        paint.alpha = 88
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3 * 2, paint)
    }

    override fun drawError(
        canvas: Canvas,
        cellBean: LockCellBean,
        nextCellBean: LockCellBean?,
        paint: Paint
    ) {
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, paint)
        paint.style = Paint.Style.FILL
        paint.alpha = 88
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3 * 2, paint)
    }
}