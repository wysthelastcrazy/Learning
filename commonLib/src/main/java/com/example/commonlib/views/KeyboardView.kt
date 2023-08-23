package com.example.commonlib.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.commonlib.databinding.KeyboardBinding

/**
 *@author wangyasheng
 *@date 2023/8/21
 * 自定义数字键盘
 */
class KeyboardView : FrameLayout, View.OnClickListener {

    lateinit var binding: KeyboardBinding

    constructor(context: Context): this(context,null)
    constructor(context: Context,attrs: AttributeSet?):super(context, attrs){
        initView(context)
    }
    private fun initView(context: Context){
        binding = KeyboardBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        setOnClickListeners()
    }
    private fun setOnClickListeners(){
        with(binding) {
            one.setOnClickListener(this@KeyboardView)
            two.setOnClickListener(this@KeyboardView)
            three.setOnClickListener(this@KeyboardView)
            four.setOnClickListener(this@KeyboardView)
            five.setOnClickListener(this@KeyboardView)
            six.setOnClickListener(this@KeyboardView)
            seven.setOnClickListener(this@KeyboardView)
            eight.setOnClickListener(this@KeyboardView)
            nine.setOnClickListener(this@KeyboardView)
            star.setOnClickListener(this@KeyboardView)
            zero.setOnClickListener(this@KeyboardView)
            pound.setOnClickListener(this@KeyboardView)
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                one -> onItemClick('1')
                two -> onItemClick('2')
                three -> onItemClick('3')
                four -> onItemClick('4')
                five -> onItemClick('5')
                six -> onItemClick('6')
                seven -> onItemClick('7')
                eight -> onItemClick('8')
                nine -> onItemClick('9')
                star -> onItemClick('*')
                zero -> onItemClick('0')
                pound -> onItemClick('#')
            }
        }
    }
    private fun onItemClick(char: Char) {

    }
    override fun onKeyDown(keyCode:Int, event:KeyEvent): Boolean {
        val pressedChar = event!!.unicodeChar.toChar()
        Log.d("KeyboardView","pressedChar:$pressedChar")
        return true
    }
}