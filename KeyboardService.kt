package com.axiom.ioskeyboard

import android.inputmethodservice.InputMethodService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import java.net.URLEncoder

class KeyboardService : InputMethodService() {
    private var root: LinearLayout? = null
    private var shifted = false
    private var symbols = false
    private var emojiMode = false

    private val keyText = Color.rgb(35, 39, 46)
    private val blue = Color.rgb(20, 126, 251)
    private val special = Color.rgb(205, 211, 220)

    override fun onCreateInputView(): View {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4, 8, 4, 8)
            setBackgroundColor(Color.rgb(224, 228, 235))
        }
        render()
        return root!!
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        render()
    }

    private fun render() {
        val panel = root ?: return
        panel.removeAllViews()

        val toolbar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        listOf("☷", "✨", "🌐", "📋", "⚙").forEachIndexed { index, item ->
            toolbar.addView(key(item, special, 42) {
                when (index) {
                    0 -> { emojiMode = !emojiMode; render() }
                    1 -> translateText()
                    2 -> { symbols = !symbols; emojiMode = false; render() }
                    3 -> copyBeforeCursor()
                    4 -> startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }, weightParams(1f, 42))
        }
        panel.addView(toolbar)

        if (emojiMode) {
            addRow(panel, listOf("😀","😂","🥰","😍","😭","🔥","❤️","👍","✨","🙏"), 46)
            addRow(panel, listOf("🎉","😊","😎","🥳","💯","🤔","😴","💀","👏","😘"), 46)
            addRow(panel, listOf("ABC","🙂","🚀","🌈","☀️","🌙","💖","💬","⌫"), 46)
            addBottomRow(panel)
            return
        }

        val rows = if (symbols) listOf(
            listOf("1","2","3","4","5","6","7","8","9","0"),
            listOf("@","#","$","%","&","-","+","(",")","/"),
            listOf("= *", ".", ",", "?", "!", "'", "⌫")
        ) else listOf(
            listOf("q","w","e","r","t","y","u","ı","o","p"),
            listOf("a","s","d","f","g","h","j","k","l"),
            listOf("⇧","z","x","c","v","b","n","m","⌫")
        )
        rows.forEachIndexed { i, row ->
            val line = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }
            row.forEach { label ->
                val width = if (label == "⇧" || label == "⌫") 1.35f else 1f
                line.addView(key(label, if (label == "⇧" || label == "⌫") special else Color.WHITE, 48) {
                    handleKey(label)
                }, weightParams(width, 48))
            }
            val params = LinearLayout.LayoutParams(-1, 50)
            params.topMargin = 5
            panel.addView(line, params)
        }
        addBottomRow(panel)
    }

    private fun addBottomRow(panel: LinearLayout) {
        val line = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        val items = listOf(
            "123" to 1.25f, "🌐" to 0.9f, "," to 0.7f,
            "English (US)" to 4.2f, "." to 0.7f, "↵" to 1.25f
        )
        items.forEach { (label, weight) ->
            line.addView(key(label, if (label == "↵") blue else special, 48) {
                when (label) {
                    "123" -> { symbols = !symbols; render() }
                    "🌐" -> { emojiMode = !emojiMode; render() }
                    "," -> currentInputConnection?.commitText(",", 1)
                    "." -> currentInputConnection?.commitText(".", 1)
                    "↵" -> currentInputConnection?.performEditorAction(currentInputEditorInfo?.imeOptions ?: 0)
                    else -> currentInputConnection?.commitText(" ", 1)
                }
            }, weightParams(weight, 48))
        }
        val params = LinearLayout.LayoutParams(-1, 50)
        params.topMargin = 5
        panel.addView(line, params)
    }

    private fun handleKey(label: String) {
        val ic = currentInputConnection ?: return
        when (label) {
            "⌫" -> ic.deleteSurroundingText(1, 0)
            "⇧" -> { shifted = !shifted; render() }
            "ABC" -> { emojiMode = false; render() }
            else -> {
                val output = if (shifted) label.uppercase() else label
                ic.commitText(output, 1)
                if (shifted) { shifted = false; render() }
            }
        }
    }

    private fun addRow(panel: LinearLayout, labels: List<String>, height: Int) {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        labels.forEach { label ->
            row.addView(key(label, Color.WHITE, height) { handleKey(label) }, weightParams(1f, height))
        }
        val p = LinearLayout.LayoutParams(-1, height + 2)
        p.topMargin = 5
        panel.addView(row, p)
    }

    private fun key(label: String, color: Int, height: Int, action: () -> Unit): TextView =
        TextView(this).apply {
            text = label
            textSize = if (label.length > 5) 13f else 19f
            gravity = Gravity.CENTER
            setTextColor(if (color == blue) Color.WHITE else keyText)
            typeface = Typeface.DEFAULT
            background = GradientDrawable().apply {
                setColor(color)
                cornerRadius = 8f
            }
            elevation = 1.5f
            setOnClickListener { action() }
            isFocusable = true
            contentDescription = label
        }

    private fun weightParams(weight: Float, height: Int) =
        LinearLayout.LayoutParams(0, height, weight).apply {
            setMargins(2, 1, 2, 1)
        }

    private fun copyBeforeCursor() {
        val text = currentInputConnection?.getTextBeforeCursor(200, 0)?.toString().orEmpty()
        if (text.isNotBlank()) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Keyboard text", text))
        }
    }

    private fun translateText() {
        val ic = currentInputConnection
        val selected = ic?.getSelectedText(0)?.toString()
        val before = ic?.getTextBeforeCursor(300, 0)?.toString().orEmpty()
        val phrase = (selected?.takeIf { it.isNotBlank() } ?: before).trim()
        val url = "https://translate.google.com/?sl=auto&tl=tr&text=" +
            URLEncoder.encode(phrase, "UTF-8") + "&op=translate"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
