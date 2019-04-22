package com.car.portal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.car.portal.R;

public class PassEditView extends View {


    private Paint mPaint;
    private InputFinishListener inputFinishListener;//输入完成监听

    private int inputNum=0;//当前输入的密码个数
    private int passwordNum=6;//密码个数

    private int boundWidth=2;//外层框线条粗细
    private int boundColor= R.color.view_blue;//外层框线条颜色
    private int boundRadius=0;//外框圆角半径

    private int deliverWidth=1;//分割线粗细
    private int deliverColor=Color.GRAY;//分割线条颜色
    private int deliverPadding=5;//分割线距离框的大小

    private int circleRadius =15;//密码圆点半径大小
    private int circleColor= Color.BLACK;//密码圆点颜色

    private StringBuilder currentPassword;//用户输入的密码

    public PassEditView(Context context) {
        super(context);
        init();
    }

    public PassEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private void init() {
        currentPassword=new StringBuilder();
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width,height;
        if (widthMode==MeasureSpec.AT_MOST){
            width=dip2px(36)*passwordNum;
        }else {
            width=widthSize;
        }
        if (heightMode==MeasureSpec.AT_MOST){
            height=dip2px(36);
        }else{
            height=heightSize;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float deliverSize=(width-getPaddingLeft()-getPaddingRight())/passwordNum;
        //1.画外框
        drawBound(canvas, width, height);
        //2.画分割线
        drawDeliver(canvas, height, deliverSize);
        //3.输入密码之后显示的图案
        drawCircle(canvas, height, deliverSize);
    }

    private void drawCircle(Canvas canvas, int height, float deliverSize) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleColor);
        for (int i = 0; i < inputNum; i++) {
            canvas.drawCircle(deliverSize*i+getPaddingLeft()+deliverSize/2,(height-getPaddingTop()-getPaddingBottom())/2+getPaddingTop(), circleRadius,mPaint);
        }
    }

    private void drawDeliver(Canvas canvas, int height, float deliverSize) {

        mPaint.setStrokeWidth(deliverWidth);
        mPaint.setColor(deliverColor);
        Path path = new Path();
        for (int i = 1; i < passwordNum; i++) {
            path.reset();
            path.moveTo(deliverSize*i+getPaddingLeft(),0+deliverPadding+getPaddingTop());
            path.lineTo(deliverSize*i+getPaddingLeft(),height-deliverPadding-getPaddingBottom());
            canvas.drawPath(path,mPaint);
        }
    }

    private void drawBound(Canvas canvas, int width, int height) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(boundWidth);
        mPaint.setColor(boundColor);
        RectF rectF = new RectF(getPaddingLeft(),getPaddingTop(),width-getPaddingRight(),height-getPaddingBottom());
        canvas.drawRoundRect(rectF,boundRadius,boundRadius,mPaint);
    }


    public void inputPassword(Object pwd){
        if (inputNum<passwordNum) {
            currentPassword.append(pwd);
            inputNum++;
        }
        invalidate();
        if (inputNum==passwordNum){
            if (inputFinishListener!=null){
                inputFinishListener.onFinish(getPassword());
            }
        }
    }
    //删除一个密码
    public void deletePassword(){
        if (currentPassword.length()>0) {
            currentPassword.deleteCharAt(currentPassword.length() - 1);
            inputNum--;
        }
        invalidate();
    }
    //清空输入的所有密码
    public void cleanInput(){
        inputNum=0;
        currentPassword.delete(0,currentPassword.length());
        invalidate();
    }
    //获取输入的所有密码
    public String getPassword(){
        return currentPassword.toString();
    }

    public void setInputFinishListener(InputFinishListener inputFinishListener) {
        this.inputFinishListener = inputFinishListener;
    }

    public interface InputFinishListener{
        void onFinish(String pwd);
    }
}
