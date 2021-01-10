package com.cl.gesturelock.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.SparseArray
import com.cl.gesturelock.bean.LockCellBean
import com.cl.gesturelock.impl.ILockLinkView

/**
 * @author Chenli
 * @Date 2020/12/21
 * @Description
 */
class DefaultLockLinkView : ILockLinkView {

    override fun drawTouch(
        canvas: Canvas,
        touchList: ArrayList<Int>,
        cellBean: SparseArray<LockCellBean>,
        endX: Float,
        endY: Float,
        paint: Paint
    ) {
        var path = Path()
        touchList.forEach {
            val lockCellBean = cellBean[it]
            if (path.isEmpty) {
                path.moveTo(lockCellBean.x, lockCellBean.y)
            } else {
                path.lineTo(lockCellBean.x, lockCellBean.y)
            }
        }
        if (endX != 0f && endY != 0f && touchList.size < cellBean.size()) {
            path.lineTo(endX, endY)
        }
        canvas.drawPath(path, paint)
    }

    override fun drawError(
        canvas: Canvas,
        touchList: ArrayList<Int>,
        cellBean: SparseArray<LockCellBean>,
        paint: Paint
    ) {
        var path = Path()
        touchList.forEach {
            val lockCellBean = cellBean[it]
            if (path.isEmpty) {
                path.moveTo(lockCellBean.x, lockCellBean.y)
            } else {
                path.lineTo(lockCellBean.x, lockCellBean.y)
            }
        }
        canvas.drawPath(path, paint)
    }

}