package com.cl.gesturelock.activity.ui.verified

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cl.gesturelock.R
import com.cl.gesturelock.listener.GestureLockListener
import com.cl.gesturelock.util.LockSettingUtil
import com.cl.gesturelock.view.DefaultLockCellView
import com.cl.gesturelock.view.GestureLockView
import com.cl.gesturelock.view.SimpleLockCellView

class VerifiedFragment : Fragment() {
    private lateinit var mClRoot: ConstraintLayout
    private lateinit var mGlvLock: GestureLockView
    private lateinit var instance: LockSettingUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = LockSettingUtil.instance
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verified, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClRoot = view.findViewById(R.id.cl_root)
        mGlvLock = view.findViewById(R.id.glv_lock)
        mGlvLock.isVerifyMode(instance.saveLock)
        mGlvLock.setGestureLockListener(object : GestureLockListener {
            override fun onStart() {
            }

            override fun onChange(touchList: ArrayList<Int>) {
            }

            override fun onComplete(touchList: ArrayList<Int>, encryptContent: String) {
            }

            override fun onStatusChange(statusMessage: String) {
                Toast.makeText(requireActivity(), statusMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onUnlockError() {
                Toast.makeText(requireActivity(), "解锁失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }

    private fun initUI() {
        val color = ContextCompat.getColor(
            requireContext(), if (instance.darkMode) {
                R.color.colorPrimary
            } else {
                R.color.white
            }
        )

        mClRoot.setBackgroundColor(color)

        mGlvLock.resetCell(instance.rowNum, instance.columnNum)
        mGlvLock.setFillColor(color)
        mGlvLock.setLockCellView(
            if (instance.style) {
                DefaultLockCellView()
            } else {
                SimpleLockCellView()
            }
        )
        mGlvLock.enableFeedback(instance.enableFeedback)
        mGlvLock.setLineWidth(toDp(instance.lineWidth))
    }

    fun toDp(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }
}