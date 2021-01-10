# GestureLock
手势锁（包含基本的加解密功能）

# xml中可配置项
```
//Cell绘制颜色
<item name="lock_normalColor">#CCCCCC</item>
//选中Cell时颜色
<item name="lock_touchColor">#FCC123</item>
//连线宽度
<item name="lock_lineWidth">2dp</item>
//状态保持时间
<item name="lock_statusStayDuration">1</item>
//处于错误状态下的选中Cell颜色
<item name="lock_errorColor">#E85053</item>
//Cell背景填充颜色
<item name="lock_fillColor">#FFFFFF</item>
//最低触碰数量
<item name="lock_minTouchNum">4</item>
//最大错误次数
<item name="lock_maxErrorNum">5</item>
//每行Cell数
<item name="lock_rowNum">3</item>
//每列Cell数
<item name="lock_columnNum">3</item>
//Cell绘制方式接口
<item name="lock_lockView">com.cl.gesturelock.view.DefaultLockCellView</item>
//连线绘制方式接口
<item name="lock_linkView">com.cl.gesturelock.view.DefaultLockLinkView</item>
//LockView Helper 实现加解密，判断状态等方法
<item name="lock_helper">com.cl.gesturelock.util.DefaultLockHelper</item>
```
#自定义实现Cell绘制方式
```
自定义类实现ILockCellView接口，如DefaultLockCellView
class DefaultLockCellView : ILockCellView {
    /**
     * 绘制无触碰状态下Cell
     *
     * @param canvas
     * @param cellBean
     * @param paint
     */
    override fun drawNormal(canvas: Canvas, cellBean: LockCellBean, paint: Paint) {
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, paint)
    }
    /**
     * 绘制触碰状态下Cell
     * @param canvas
     * @param cellBean
     * @param nextCellBean
     * @param paint
     */
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
    /**
     * 绘制触碰错误状态下Cell
     * @param canvas
     * @param cellBean
     * @param nextCellBean
     * @param paint
     */
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
```
#自定义实现连线绘制方式
```
自定义类实现ILockLinkView接口，如DefaultLockLinkView
class DefaultLockLinkView : ILockLinkView {
    /**
     * 绘制触碰状态下连线
     * @param canvas
     * @param touchList
     * @param cellBean
     * @param endX 手指位置X
     * @param endY 手指位置Y
     * @param paint
     */
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

    /**
     * 绘制触碰错误状态下连线
     * @param canvas
     * @param touchList
     * @param cellBean
     * @param paint
     */
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
```
#自定义实现加解密
```
自定义类继承AbstractLockHelper，如DefaultLockHelper
class DefaultLockHelper : AbstractLockHelper() {
    /**
     * 加盐（任意字符串,后转md5）
     */
    private var slatKey = "GestureLock"

    fun setSlatKey(slatKey: String) {
        this.slatKey = slatKey
    }

    /**
     * 加盐加密
     * @param content
     */
    override fun encrypt(content: String): String {
        val cipher: Cipher = Cipher.getInstance(CIPHER_MODE)
        val secretKey: SecretKey = SecretKeySpec(slatKey.md5().toByteArray(), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted: ByteArray = cipher.doFinal(content.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    /**
     * 解密
     * @param encryptContent
     */
    override fun decrypt(encryptContent: String): String {
        val cipher: Cipher = Cipher.getInstance(CIPHER_MODE)
        val secretKey: SecretKey = SecretKeySpec(slatKey.md5().toByteArray(), ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val content: ByteArray = Base64.decode(encryptContent, Base64.DEFAULT)
        val encrypted = cipher.doFinal(content)
        return String(encrypted)
    }

    companion object {
        private const val CIPHER_MODE = "AES/ECB/PKCS5Padding"
        private const val ALGORITHM = "AES"

        fun String.md5(): String {
            val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
            return bytes.hex().substring(8, 24)
        }

        private fun ByteArray.hex(): String {
            return joinToString("") { "%02X".format(it) }
        }
    }
}
```
#实现LockView状态监听
```
mGlvLock.setGestureLockListener(object : GestureLockListener {
    /**
     * 触碰开始时
     */
    override fun onStart() {
    }

    /**
     * touchList发生改变，即当触碰到下一个Cell时
     * @param touchList 触摸list
     */
    override fun onChange(touchList: ArrayList<Int>) {
    }

    /**
     * 触碰结束时
     * @param touchList 本次完成后的触摸list
     */
    override fun onComplete(touchList: ArrayList<Int>, encryptContent: String) {
        instance.saveLock = encryptContent
    }
    /**
     * 状态改变时消息反馈
     * @param statusMessage 反馈的消息
     */
    override fun onStatusChange(statusMessage: String) {
        Toast.makeText(requireActivity(), statusMessage, Toast.LENGTH_SHORT).show()
    }
    /**
     * 解锁失败
     */
    override fun onUnlockError() {
    }

    })
```