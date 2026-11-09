package com.axiom.ioskeyboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val blue = Color.rgb(20, 126, 251)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 42, 24, 24)
            setBackgroundColor(Color.rgb(244, 246, 250))
        }

        val title = TextView(this).apply {
            text = "iOS Keyboard Ultra"
            textSize = 29f
            setTextColor(Color.rgb(25, 30, 40))
            typeface = Typeface.DEFAULT_BOLD
        }
        page.addView(title)
        page.addView(TextView(this).apply {
            text = "Telefonunun klavyesini kişiselleştir."
            textSize = 15f
            setTextColor(Color.rgb(100, 108, 120))
            setPadding(0, 8, 0, 26)
        })

        val preview = TextView(this).apply {
            text = "q  w  e  r  t  y  u  ı  o  p\na  s  d  f  g  h  j  k  l\n⇧  z  x  c  v  b  n  m  ⌫\n123     🌐       English       ↵"
            textSize = 19f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(45, 50, 60))
            setPadding(14, 24, 14, 24)
            background = rounded(Color.WHITE, 22)
        }
        page.addView(preview, LinearLayout.LayoutParams(-1, 190))

        page.addView(actionButton("1. Klavyeyi etkinleştir") {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        })
        page.addView(actionButton("2. Klavyeyi seç") {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        })
        page.addView(actionButton("Test alanını aç") {
            val edit = android.widget.EditText(this).apply {
                hint = "Yeni klavyeyi burada dene..."
                textSize = 18f
                setPadding(16, 18, 16, 18)
                background = rounded(Color.WHITE, 16)
            }
            page.addView(edit, LinearLayout.LayoutParams(-1, 58))
            edit.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT)
        })

        page.addView(TextView(this).apply {
            text = "Gizlilik: Bu başlangıç sürümü yazdıklarını sunucuya göndermez. Çeviri kısayolu yalnızca sen dokunduğunda metni Google Translate'e açar."
            textSize = 13f
            setTextColor(Color.rgb(100, 108, 120))
            setPadding(2, 24, 2, 0)
        })
        setContentView(page)
    }

    private fun actionButton(label: String, action: () -> Unit): Button =
        Button(this).apply {
            text = label
            textSize = 15f
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = rounded(blue, 16)
            setPadding(12, 10, 12, 10)
            setOnClickListener { action() }
            val p = LinearLayout.LayoutParams(-1, 58)
            p.topMargin = 14
            layoutParams = p
        }

    private fun rounded(color: Int, radius: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius.toFloat()
    }
}
