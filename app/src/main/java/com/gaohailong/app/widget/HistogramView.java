package com.gaohailong.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import com.gaohailong.app.R;
import com.gaohailong.app.util.DensityUtils;
import com.gaohailong.app.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaohailong on 2019/1/31.
 */
public class HistogramView extends View {

    private Context context;
    private Paint mLinePaint; //x轴画笔
    private Paint mTextPaint; //绘制文字画笔
    private Paint mRectPaint; //矩形画笔
    private float spaceWidth; //矩形间距
    private float rectWidth = 150; //矩形宽度
    private float textHeight; //绘制的文字高度及上下padding
    private float maxHeight; //可绘制矩形区域的最大高度
    private float maxTopHeight; //绘制x轴上方矩形的最大高度
    private float maxBottomHeight; //绘制x轴下方矩形的最大高度
    private float textPaddingBottom = 30; //文字距离柱状图距离
    private float maxTopValue; //绘制x轴上方矩形的最大数
    private float maxBottomValue; //绘制x轴下方矩形的最大数
    private int mWidth;  //view宽
    private int mHeight; //view高
    private int lineColor; //x轴颜色
    private int textColor; //绘制文字的颜色
    private int[] colorArr = new int[3]; //绘制矩形的颜色
    private String[] textArr = new String[3];
    private String[] defaultTextArr = {"工时收入:+", "补贴:+", "扣款:-"};
    private List<Float> mList = new ArrayList<>();

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView, defStyleAttr, 0);
        int colorOne = typedArray.getColor(R.styleable.HistogramView_colorOne, Color.parseColor("#EF5C78"));
        int colorTwo = typedArray.getColor(R.styleable.HistogramView_colorTwo, Color.parseColor("#F6A10B"));
        int colorThree = typedArray.getColor(R.styleable.HistogramView_colorThree, Color.parseColor("#6CC500"));
        lineColor = typedArray.getColor(R.styleable.HistogramView_lineColor, Color.parseColor("#6CC500"));
        textColor = typedArray.getColor(R.styleable.HistogramView_textColor, Color.parseColor("#6CC500"));
        colorArr[0] = colorOne;
        colorArr[1] = colorTwo;
        colorArr[2] = colorThree;
        typedArray.recycle();

        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(DensityUtils.sp2px(context,12));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setColor(textColor);

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for (int i = 0; i < textArr.length; i++) {
            //获取绘制文字宽高信息
            if (TextUtils.isEmpty(textArr[i])) {
                return;
            }
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(textArr[i], 0, textArr[i].length(), bounds);
            int w = bounds.width();
            int h = bounds.height();
            maxHeight = mHeight - h * 2 - textPaddingBottom * 4;
            textHeight = h + textPaddingBottom * 2;
            maxTopHeight = maxTopValue / (maxTopValue + maxBottomValue) * maxHeight;
            maxBottomHeight = maxHeight - maxTopHeight;
            //x轴Y坐标
            float xHeight = mHeight - textHeight - maxBottomHeight;

            //绘制x轴
            if (i == 0) {
                Path path = new Path();
                path.moveTo(0, xHeight);
                path.lineTo(mWidth, xHeight);
                mLinePaint.setColor(lineColor);
                canvas.drawPath(path, mLinePaint);
            }

            if (mList.size() > 0) {
                //绘制矩形
                float left = spaceWidth * (i + 1) + rectWidth * i;
                float top = (maxTopValue - mList.get(i)) / maxTopValue * maxTopHeight + textHeight;
                float right = spaceWidth * (i + 1) + rectWidth * (i + 1);
                float bottom = xHeight;
                if (i == textArr.length - 1) {
                    top = xHeight;
                    bottom = mHeight - textHeight;
                }
                if (top == bottom) {
                    mLinePaint.setColor(colorArr[i]);
                    canvas.drawLine(left, top, right, bottom, mLinePaint);
                } else {
                    RectF rectF = new RectF(left, top, right, bottom);
                    mRectPaint.setColor(colorArr[i]);
                    canvas.drawRect(rectF, mRectPaint);
                }

                //绘制文字
                float x = spaceWidth * (i + 1) + rectWidth * i - (w - rectWidth) / 2;
                float y = top - textPaddingBottom;
                if (i == textArr.length - 1) {
                    y = mHeight - textPaddingBottom;
                }
                canvas.drawText(textArr[i], x, y, mTextPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("666", w + "");
        mWidth = w;
        mHeight = h;
        spaceWidth = (mWidth - rectWidth * 3) / 4;
    }

    public void setDataList(List<Float> mList) {
        this.mList = mList;
        setTextArr(mList);
        maxTopValue = getMaxTopValue(mList);
        maxBottomValue = mList.get(mList.size() - 1);

        invalidate();
    }

    public void setTextArr(List<Float> mList) {
        for (int i = 0; i < mList.size(); i++) {
            textArr[i] = defaultTextArr[i] + MathUtil.doubleTrans(Double.valueOf(MathUtil.get2(mList.get(i)))) + "元";
        }
    }

    //获取传入集合x轴上方的最大值
    private float getMaxTopValue(List<Float> mList) {
        float max = mList.get(0);
        for (int i = 0; i < mList.size() - 1; i++) {
            if (mList.get(i) > max) {
                max = mList.get(i);
            }
        }
        return max;
    }
}
