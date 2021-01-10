package com.cl.gesturelock.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.SparseArray
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.util.valueIterator
import com.cl.gesturelock.R
import com.cl.gesturelock.bean.LockCellBean
import com.cl.gesturelock.impl.ILockCellView
import com.cl.gesturelock.impl.ILockLinkView
import com.cl.gesturelock.listener.GestureLockListener
import com.cl.gesturelock.util.AbstractLockHelper
import com.cl.gesturelock.util.DefaultLockHelper
import kotlin.math.min

/**
 * @author Chenli
 * @Date 2020/12/15
 * @Description
 */
class GestureLockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : View(context, attrs, defStyleAttr) {
    /**
     * 手指终点坐标X
     */
    private var endX: Float = 0f

    /**
     * 手指终点坐标Y
     */
    private var endY: Float = 0f

    /**
     * 可绘制区域高度
     */
    private var viewHeight: Int = 0

    /**
     * 可绘制区域宽度
     */
    private var viewWidth: Int = 0

    /**
     * 是否触碰数据发生改变
     */
    private var isChange: Boolean = false

    /**
     * Cell集合
     */
    private var cellMap: SparseArray<LockCellBean> = SparseArray()

    /**
     * 选中的Cell集合
     */
    private var touchList = ArrayList<Int>()

    /**
     * 最低触碰数量
     */
    private var minTouchNum: Int = 0

    /**
     * 最大错误次数
     */
    private var maxErrorNum: Int = 0

    /**
     * 状态保持时间
     */
    private var statusStayDuration: Int = 0

    /**
     * 设置状态保持时间
     */
    fun setStatusStayDuration(statusStayDuration: Int) {
        this.statusStayDuration = statusStayDuration
    }

    /**
     * 是否激活触碰反馈
     */
    private var enableFeedback: Boolean = false

    /**
     * 设置是否激活触碰反馈
     */
    fun enableFeedback(enableFeedback: Boolean) {
        this.enableFeedback = enableFeedback
    }

    /**
     * Cell背景填充颜色
     */
    @ColorInt
    private var fillColor: Int = 0

    /**
     * 设置Cell背景填充颜色
     */
    fun setFillColor(@ColorInt color: Int) {
        this.fillColor = color
        invalidate()
    }

    /**
     * Cell绘制颜色
     */
    @ColorInt
    private var normalColor: Int = 0

    /**
     * 设置Cell绘制颜色
     */
    fun setNormalColor(@ColorInt color: Int) {
        this.normalColor = color
        invalidate()
    }

    /**
     * 选中Cell时颜色
     */
    @ColorInt
    private var touchColor: Int = 0

    /**
     * 设置选中Cell时颜色
     */
    fun setTouchColor(@ColorInt color: Int) {
        this.touchColor = color
        invalidate()
    }

    /**
     * 处于错误状态下的选中Cell颜色
     */
    @ColorInt
    private var errorColor: Int = 0

    /**
     * 设置处于错误状态下的选中Cell颜色
     */
    fun setErrorColor(@ColorInt color: Int) {
        this.errorColor = color
        invalidate()
    }

    /**
     * 默认预留的左右间隔距离
     */
    private var reserve: Int = 30

    /**
     * 设置预留的左右间隔距离
     */
    fun setReserve(reserve: Int) {
        this.reserve = reserve
        invalidate()
    }

    /**
     * 连线宽度
     */
    @Dimension
    private var lineWidth: Float = 0f

    /**
     * 设置连线宽度
     */
    fun setLineWidth(lineWidth: Float) {
        this.lineWidth = lineWidth
        invalidate()
    }

    /**
     * Cell绘制方式接口
     */
    private var lockCellView: ILockCellView

    /**
     * 设置Cell绘制方式
     */
    fun setLockCellView(lockCellView: ILockCellView) {
        this.lockCellView = lockCellView
        invalidate()
    }

    /**
     * 连线绘制方式接口
     */
    private var lockLinkView: ILockLinkView

    /**
     * 设置连线绘制方式
     */
    fun setLockLinkView(lockLinkView: ILockLinkView) {
        this.lockLinkView = lockLinkView
        invalidate()
    }

    /**
     * LockView状态监听
     */
    private var listener: GestureLockListener? = null

    /**
     * 设置LockView状态监听
     */
    fun setGestureLockListener(listener: GestureLockListener) {
        this.listener = listener
    }

    /**
     * LockView Helper 实现加解密，判断状态等方法
     */
    private var lockHelper: AbstractLockHelper

    /**
     * 设置LockView Helper 实现加解密，判断状态等方法
     */
    fun setLockHelper(lockHelper: AbstractLockHelper) {
        this.lockHelper = lockHelper
        this.lockHelper.minTouchNum = minTouchNum
        this.lockHelper.maxErrorNum = maxErrorNum
        onAttachedToWindow()
        invalidate()
    }

    fun getLockHelper(): AbstractLockHelper {
        return this.lockHelper
    }

    /**
     * 每行Cell数
     */
    private var rowNum: Int

    /**
     * 每列Cell数
     */
    private var columnNum: Int

    /**
     * 重设Cell行列数
     */
    fun resetCell(rowNum: Int, columnNum: Int) {
        this.rowNum = rowNum
        this.columnNum = columnNum
        if (viewHeight != 0 && viewWidth != 0) {
            initCell()
            touchList.clear()
        }
        invalidate()
    }

    /**
     * 设置为Setting模式
     */
    fun isSettingMode() {
        lockHelper.isSettingMode()
    }

    /**
     * 设置为Verify模式
     */
    fun isVerifyMode(encryptContent: String) {
        lockHelper.isVerifyMode(encryptContent)
    }

    init {
        val attr =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.GestureLockView,
                defStyleAttr,
                R.style.GestureLockViewDefault
            )
        statusStayDuration =
            attr.getInt(R.styleable.GestureLockView_lock_statusStayDuration, defStyleAttr)
        enableFeedback =
            attr.getBoolean(R.styleable.GestureLockView_lock_enableFeedback, false)
        minTouchNum = attr.getInt(R.styleable.GestureLockView_lock_minTouchNum, defStyleAttr)
        maxErrorNum = attr.getInt(R.styleable.GestureLockView_lock_maxErrorNum, defStyleAttr)
        rowNum = attr.getInt(R.styleable.GestureLockView_lock_rowNum, defStyleAttr)
        columnNum = attr.getInt(R.styleable.GestureLockView_lock_columnNum, defStyleAttr)
        fillColor = attr.getColor(R.styleable.GestureLockView_lock_fillColor, defStyleAttr)
        normalColor = attr.getColor(R.styleable.GestureLockView_lock_normalColor, defStyleAttr)
        touchColor = attr.getColor(R.styleable.GestureLockView_lock_touchColor, defStyleAttr)
        errorColor = attr.getColor(R.styleable.GestureLockView_lock_errorColor, defStyleAttr)
        lineWidth =
            attr.getDimension(R.styleable.GestureLockView_lock_lineWidth, defStyleAttr.toFloat())
        lockCellView = try {
            var lockViewPath = attr.getString(R.styleable.GestureLockView_lock_lockView)
            Class.forName(lockViewPath).newInstance() as ILockCellView
        } catch (e: Exception) {
            DefaultLockCellView()
        }
        lockLinkView = try {
            var linkViewPath = attr.getString(R.styleable.GestureLockView_lock_linkView)
            Class.forName(linkViewPath).newInstance() as ILockLinkView
        } catch (e: Exception) {
            DefaultLockLinkView()
        }
        lockHelper = try {
            var lockHelperPath = attr.getString(R.styleable.GestureLockView_lock_helper)
            Class.forName(lockHelperPath).newInstance() as AbstractLockHelper
        } catch (e: Exception) {
            DefaultLockHelper()
        }
        if (lockHelper != null) {
            lockHelper.maxErrorNum = maxErrorNum
            lockHelper.minTouchNum = minTouchNum
        }
        attr.recycle()
    }

    /**
     * 测定绘制区域大小
     * @viewWidth 测定宽去掉左右padding再去掉左右预留间隔
     * @viewHeight 测定高去掉上下padding再去掉上下预留间隔
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(size, size)
        viewWidth = measuredWidth - paddingLeft - paddingRight - (reserve * 2)
        viewHeight = measuredHeight - paddingTop - paddingBottom - (reserve * 2)
    }

    /**
     *绘制
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (cellMap.size() == 0) {//未设置
            initCell()
        }
        drawLink(canvas)
        drawFill(canvas)
        drawCell(canvas)
    }


    /**
     * 点击事件分发处理
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                actionUp(event)
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    /**
     * 画笔
     */
    private var paint: Paint = Paint().apply {
        isDither = true
        isAntiAlias = true
    }

    /**
     * 设置连线画笔
     */
    private fun getLinkPaint(status: Int): Paint {
        when (status) {
            TOUCH -> paint.color = touchColor
            ERROR -> paint.color = errorColor
        }
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth
        return paint
    }

    /**
     * 设置Cell背景填充画笔
     */
    private fun getFillPaint(): Paint {
        paint.color = fillColor
        paint.style = Paint.Style.FILL
        return paint
    }

    /**
     * 设置Cell画笔
     */
    private fun getCellPaint(status: Int): Paint {
        when (status) {
            NORMAL -> paint.color = normalColor
            TOUCH -> paint.color = touchColor
            ERROR -> paint.color = errorColor
        }
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5f
        return paint
    }

    /**
     * 处理触摸按下事件
     */
    private fun actionDown(event: MotionEvent): Boolean {
        clearTouchData()
        lockHelper.isError = false
        insideCheck(event.x, event.y)
        listener?.onStart()
        invalidate()
        return true
    }

    /**
     * 处理触摸移动事件
     */
    private fun actionMove(event: MotionEvent): Boolean {
        insideCheck(event.x, event.y)
        endX = event.x
        endY = event.y
        if (isChange) {
            listener?.onChange(touchList)
            isChange = false
        }
        invalidate()
        return true
    }

    /**
     * 处理触摸放开事件
     */
    private fun actionUp(event: MotionEvent): Boolean {
        endX = 0f
        endY = 0f
        lockHelper.checkInput(touchList)
        listener?.onStatusChange(lockHelper.statusMessage)
        if (!lockHelper.isError) {
            listener?.onComplete(touchList, lockHelper.encryptContent!!)
        }
        if (lockHelper.isUnlockError) {
            listener?.onUnlockError()
        }
        postDelayed({ clearTouchData(true) }, statusStayDuration * 1000L)
        invalidate()
        return true
    }

    /**
     * 检查是否触碰到Cell内
     */
    private fun insideCheck(x: Float, y: Float) {
        for (value in cellMap.valueIterator()) {
            if (value.touchInside(x, y) && !value.isTouch) {
                value.isTouch = true
                touchList.add(value.id)
                isChange = true
                feedBack()
            }
        }
    }

    /**
     * 触碰反馈
     */
    private fun feedBack() {
        if (this.enableFeedback) {
            this.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                        or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        }
    }

    /**
     * 清空触碰数据
     */
    private fun clearTouchData(clearByAuto: Boolean = false) {
        touchList.clear()
        for (value in cellMap.valueIterator()) {
            value.isTouch = false
        }
        if (clearByAuto) {
            invalidate()
        }
    }

    /**
     * 绘制连线
     */
    private fun drawLink(canvas: Canvas) {
        if (touchList.size > 0) {
            if (lockHelper.isError) {
                lockLinkView.drawError(canvas, touchList, cellMap, getLinkPaint(ERROR))
            } else {
                lockLinkView.drawTouch(canvas, touchList, cellMap, endX, endY, getLinkPaint(TOUCH))
            }
        }
    }

    /**
     * 绘制Cell填充背景
     */
    private fun drawFill(canvas: Canvas) {
        for (cellBean in cellMap.valueIterator()) {
            canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, getFillPaint())
        }
    }

    /**
     * 绘制Cell
     */
    private fun drawCell(canvas: Canvas) {
        for (cellBean in cellMap.valueIterator()) {
            if (cellBean.isTouch) {
                val nextTouchCellBean = checkHasNextTouch(cellBean.id)
                if (lockHelper.isError) {
                    lockCellView.drawError(
                        canvas, cellBean,
                        nextTouchCellBean, getCellPaint(ERROR)
                    )
                } else {
                    lockCellView.drawTouch(
                        canvas, cellBean,
                        nextTouchCellBean, getCellPaint(TOUCH)
                    )
                }
            } else {
                lockCellView.drawNormal(canvas, cellBean, getCellPaint(NORMAL))
            }
        }
    }

    /**
     * 检查是否存在下一个触碰的Cell
     */
    private fun checkHasNextTouch(id: Int): LockCellBean? {
        val index = touchList.indexOf(id)
        if (index in 0 until touchList.size - 1) {
            return cellMap[touchList[index + 1]]
        }
        return null
    }

    /**
     * 初始化Cell数据
     */
    private fun initCell() {
        cellMap = SparseArray()
        val width = viewWidth.toFloat() / (rowNum * 3 - 1)
        val height = viewHeight.toFloat() / (columnNum * 3 - 1)
        for (column in 0 until columnNum) {
            for (row in 0 until rowNum) {
                val id = column * rowNum + row
                val x = width * (row * 3 + 1) + paddingLeft + reserve
                val y = height * (column * 3 + 1) + paddingTop + reserve
                cellMap.put(id, LockCellBean(id, x, y, min(width, height)))
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lockHelper.attached(context)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lockHelper.detached()
    }

    companion object {
        /**
         * Cell状态
         */
        const val NORMAL = 1
        const val TOUCH = 2
        const val ERROR = 3
    }
}