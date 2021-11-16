package net.dixq.overlaypainter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.WindowManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.view.Gravity

class MainActivity : AppCompatActivity() {

    var isEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Settings.canDrawOverlays(this)) {
            setupPaint()
        } else {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            val launcher = registerForActivityResult(
                StartActivityForResult()
            ) { _ -> // 結果を受け取る関数
                if (Settings.canDrawOverlays(this)) {
                    setupPaint()
                } else {
                    Toast.makeText(this, "権限がないので終了します", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            launcher.launch(intent)
        }
    }

//    private fun startPaintService(){
//        val intent = Intent(application, PaintService::class.java)
//        startForegroundService(intent)
//    }

    fun setupPaint(){
        val layoutInflater = LayoutInflater.from(this)
        val typeLayer = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        val windowManager = applicationContext
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            typeLayer,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT)
        params.gravity = Gravity.TOP or Gravity.LEFT
        val paintLayer = layoutInflater.inflate(R.layout.paint_layer, null) as ViewGroup
        val paintView = paintLayer.findViewById<PaintView>(R.id.view_paint)
        val red = paintLayer.findViewById<Button>(R.id.btn_red)
        red.setOnClickListener {
            paintView.setColor(Color.RED)
        }
        val blue = paintLayer.findViewById<Button>(R.id.btn_blue)
        blue.setOnClickListener {
            paintView.setColor(Color.BLUE)
        }
        val green = paintLayer.findViewById<Button>(R.id.btn_green)
        green.setOnClickListener {
            paintView.setColor(Color.GREEN)
        }
        val change = paintLayer.findViewById<Button>(R.id.btn_change)
        change.setOnClickListener {
            if(isEnabled){
                paintLayer.removeView(paintView)
            } else {
                paintLayer.addView(paintView)
            }
            isEnabled = !isEnabled
        }
        windowManager.addView(paintLayer, params)

        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this@MainActivity.startActivity(homeIntent)
    }

}