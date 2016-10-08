package com.tspoon.drawtastic.ui.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*


val TOUCH_TOLERANCE = 4f
val GRADIENT_COLORS = intArrayOf(
        Color.BLUE,
        Color.CYAN,
        Color.GREEN,
        Color.YELLOW,
        Color.RED,
        Color.MAGENTA
)

val RANDOM = Random()

class DrawingView : View {


    val mPaint = Paint()
    val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    var mCanvas: Canvas? = null
    var mBitmap: Bitmap? = null
    var mCurrentPath = Path()
    val mCirclePath = Path()

    val mShaderMatrix = Matrix()

    var mX: Float = 0f
    var mY: Float = 0f

    var mCanvasWidth: Int = 0
    var mCanvasHeight: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {

        mPaint.color = Color.BLUE
        mPaint.isAntiAlias = true
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.MITER
        mPaint.strokeWidth = 16f

    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        mCanvasWidth = w
        mCanvasHeight = h
        clear()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mX = x
                mY = y
                mPaint.shader = getRandomShader()
                mCurrentPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(x - mX)
                val dy = Math.abs(y - mY)
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mCurrentPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
                    mX = x
                    mY = y

                    mCirclePath.reset()
                    mCirclePath.addCircle(mX, mY, 30f, Path.Direction.CW)
                }
            }
            MotionEvent.ACTION_UP -> {
                mCurrentPath.lineTo(x, y)
                mCirclePath.reset()
                mCanvas!!.drawPath(mCurrentPath, mPaint)
                //mPaths.add(mCurrentPath)
                mCurrentPath.reset()
            }
            else -> return false
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.drawPath(mCurrentPath, mPaint)
        canvas.drawPath(mCirclePath, mPaint)
    }

    fun clear() {
        mBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        invalidate()
    }

    private fun getRandomShader(): Shader {
        val randomRadius = RANDOM.nextFloat() * (mCanvasWidth / 2) + mCanvasWidth / 2 - 100f
        val shader = RadialGradient(RANDOM.nextFloat() * mCanvasWidth, RANDOM.nextFloat() * mCanvasHeight, randomRadius, GRADIENT_COLORS, null, Shader.TileMode.MIRROR)
        mShaderMatrix.setRotate((Math.random() * 360).toFloat())
        shader.setLocalMatrix(mShaderMatrix)
        return shader
    }
}
