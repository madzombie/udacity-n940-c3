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
    private var BTNbackgroundColor = Color.LTGRAY
    private var COMPLETED:Long =0L
    private var CLICKED:Long=1L
    private var LOADING:Long=2L
    private var circleR:Float = 0f
    private var circleM :Float=0f
    private var angle:Float=0f
    private var radius:Float=50f
    private var btnText="Download"
    private var circleAnimator = ValueAnimator()

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
                        angle=360f*deger
                        invalidate()
                    }
                    duration=500
                    start()
                }

                circleAnimator = ValueAnimator.ofInt(0, 360).apply {
                    duration  = 2000
                    addUpdateListener { valueAnimator ->
                        angle = (valueAnimator.animatedValue as Int).toFloat()
                        valueAnimator.repeatCount = ValueAnimator.INFINITE
                        invalidate()
                    }
                    start()
                }
            }

            ButtonState.Completed-> {
                valueAnimator.cancel()
                btnText="Download"
                angle=0f
                progressW=0

                circleAnimator.end()

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
        circleR=h*0.8f
        circleM=h* 0.2f


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

        btnText = when(buttonState) {
            ButtonState.Completed->"Download"
            ButtonState.Clicked->"Clicked OK!"
            ButtonState.Loading->"Loading"
        }
        canvas.drawText(btnText,(widthSize/2).toFloat(),(heightSize/2).toFloat(),paint)
        paint.color=Color.YELLOW
        canvas.drawArc(circleR,circleM,widthSize-circleM,heightSize - circleM,0f,angle,true,paint)


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
    private fun setAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )

        with(typedArray) {
            BTNbackgroundColor = getColor(
                R.styleable.LoadingButton_BTNbackgroundColor,
                BTNbackgroundColor
            )
        }

        buttonState= when(typedArray.getInt(R.styleable.LoadingButton_state,COMPLETED.toInt()).toLong()) {
            LOADING -> ButtonState.Loading
            CLICKED->ButtonState.Clicked
            else -> ButtonState.Completed
        }

        typedArray.recycle()
    }

}

