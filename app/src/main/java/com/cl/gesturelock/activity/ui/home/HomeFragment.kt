package com.cl.gesturelock.activity.ui.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.cl.gesturelock.R
import com.cl.gesturelock.util.LockSettingUtil

class HomeFragment : Fragment() {

    private lateinit var instance: LockSettingUtil
    private lateinit var mEtRow: EditText
    private lateinit var mEtColumn: EditText
    private lateinit var mEtLineWidth: EditText
    private lateinit var mBtSave: Button
    private lateinit var mRbDark: RadioButton
    private lateinit var mRbLight: RadioButton
    private lateinit var mRbSimple: RadioButton
    private lateinit var mRbDefault: RadioButton
    private lateinit var mRbEnable: RadioButton
    private lateinit var mRbDisable: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = LockSettingUtil.instance
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEtRow = view.findViewById(R.id.et_row)
        mEtColumn = view.findViewById(R.id.et_column)
        mEtLineWidth = view.findViewById(R.id.et_line_width)
        mBtSave = view.findViewById(R.id.bt_save)
        mRbDark = view.findViewById(R.id.rb_dark)
        mRbLight = view.findViewById(R.id.rb_light)
        mRbDefault = view.findViewById(R.id.rb_default)
        mRbSimple = view.findViewById(R.id.rb_simple)
        mRbEnable = view.findViewById(R.id.rb_enable)
        mRbDisable = view.findViewById(R.id.rb_disable)
        mBtSave.setOnClickListener {
            instance.columnNum = mEtColumn.text.toString().toInt()
            instance.rowNum = mEtRow.text.toString().toInt()
            instance.lineWidth = mEtLineWidth.text.toString().toInt()
            instance.darkMode = mRbDark.isChecked
            instance.style = mRbDefault.isChecked
            instance.enableFeedback = mRbEnable.isChecked
            requireActivity().hideSoftKeyboard(mEtColumn)
            requireActivity().hideSoftKeyboard(mEtRow)
            requireActivity().hideSoftKeyboard(mEtLineWidth)
        }
    }

    override fun onResume() {
        super.onResume()
        mEtRow.setText("${instance.rowNum}")
        mEtColumn.setText("${instance.columnNum}")
        mEtLineWidth.setText("${instance.lineWidth}")
        mRbDark.isChecked = instance.darkMode
        mRbLight.isChecked = !instance.darkMode
        mRbDefault.isChecked = instance.style
        mRbSimple.isChecked = !instance.style
        mRbEnable.isChecked = instance.enableFeedback
        mRbDisable.isChecked = !instance.enableFeedback
    }

    private fun Activity.hideSoftKeyboard(focusView: View? = null) {
        val focusV = focusView ?: currentFocus
        focusV?.apply {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}