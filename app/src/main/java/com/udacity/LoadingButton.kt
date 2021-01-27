package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates
import androidx.core.content.ContextCompat.getColor

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private lateinit var paint:Paint
    private var valueAnimator = ValueAnimator()
    private var progressW:Int=0
    private val BTNbackgroundColor = Color.LTGRAY

    private var circleR:Float = 0f
    private var circleM :Float=0f
    private var angle:Float=0f
    private var radius:Float=50f

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                valueAnimator=ValueAnimator.ofInt(0,widthSize).apply {
                    addUpdateListener {
                        progressW = animatedValue as Int
                        valueAnimator.repeatCount=ValueAnimator.INFINITE
                        valueAnimator.repeatMode=ValueAnimator.REVERSE
                        Log.d("buttonState",it.animatedValue.javaClass.name)
                        var deger =  it.animatedValue.toString().toFloat()
                        angle=360f*deger as Float
                        invalidate()
                    }
                    duration=3000
                    start()
                }
            }

            ButtonState.Completed-> {
                valueAnimator.cancel()
                progressW=0
                invalidate()
            }
            // buttonState.Clicked end
        }
    }


    init {
        isClickable=true
        paint = Paint().apply {
            textAlign=Paint.Align.CENTER
            color= ContextCompat.getColor(context, R.color.colorPrimary)
            textSize=resources.getDimension(R.dimen.default_text_size)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize= w.toFloat().toInt()
        circleR=widthSize-radius
        circleM=(h/2)-radius


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(BTNbackgroundColor)
        canvas.drawRect(0f,0f, widthSize.toFloat(), heightSize.toFloat(),paint)
        paint.color= getColor(context,R.color.colorPrimaryDark)
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat()*progressW/100, paint)
        paint.color= ContextCompat.getColor(context,R.color.colorAccent)
        paint.textSize=60f
        paint.textAlign=Paint.Align.CENTER

        val btnText = when(buttonState) {
            ButtonState.Completed->"Download"
            ButtonState.Clicked->"Clicked OK!"
            ButtonState.Loading->"Loading"
        }
        canvas.drawText(btnText,(widthSize/2).toFloat(),(heightSize/2).toFloat(),paint)
        paint.color=Color.YELLOW
        canvas.drawArc(circleR,circleM,widthSize-radius,(heightSize/2)+radius,0f,angle,true,paint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
    fun setState (nState:ButtonState) {
        buttonState=nState
    }

    override fun performClick(): Boolean {
        if (super.performClick()) {
            return true
        }
        invalidate()
        return true
    }

}

