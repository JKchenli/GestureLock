package com.cl.gesturelock.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.cl.gesturelock.bean.LockCellBean
import com.cl.gesturelock.impl.ILockCellView
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Chenli
 * @Date 2020/12/21
 * @Description
 */
class DefaultLockCellView : ILockCellView {

    override fun drawNormal(canvas: Canvas, cellBean: LockCellBean, paint: Paint) {
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, paint)
    }

    override fun drawTouch(
        canvas: Canvas,
        cellBean: LockCellBean,
        nextCellBean: LockCellBean?,
        paint: Paint
    ) {
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3, paint)
        nextCellBean?.let {
            val angle = cellBean.getAngle(it.x, it.y)
            var distance = cellBean.radius / 4 * 3
            var triangle = cellBean.radius / 4
            var path = Path()
            path.moveTo(
                cellBean.x + cos(angle) * distance,
                cellBean.y + sin(angle) * distance
            )
            var tx = cellBean.x + cos(angle) * (distance - triangle)
            var ty = cellBean.y + sin(angle) * (distance - triangle)
            path.lineTo(
                tx + cos(angle + Math.PI.toFloat() / 2) * triangle,
                ty + sin(angle + Math.PI.toFloat() / 2) * triangle
            )
            path.lineTo(
                tx + cos(angle - Math.PI.toFloat() / 2) * triangle,
                ty + sin(angle - Math.PI.toFloat() / 2) * triangle
            )
            path.close()
            canvas.drawPath(path, paint)
        }
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, paint)
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
    }

}