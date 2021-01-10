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