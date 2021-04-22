package com.andforce.circleseekbar.camera.setting.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.andforce.circleseekbar.R;

public class BigCircleSeekBarView extends RelativeLayout {

	public static final String TAG = "BigCircleSeekBarView";
	float cX = 0;
	float cY = 0;
	float viewHeight = 0;
	int devideNumber = 0;
	
	private static final int EXPOSURE = 0;
	private static final int ISO = 1;
	private static final int SHUTTER = 2;
	private static final int MF = 3;
	private static final int WB = 4;
	
	private Bitmap sunBitmap;
	private Bitmap riguangBitmap;
	private Bitmap cludeBitmap;
	private Bitmap yingguangBitmap;
	
	/**
	 * @fields TYPE : 标注当前属于什么控件
	 */
	private int TYPE = -1;
	
	
	private Paint mPaint = new Paint();
	private Paint mTextPaint = new Paint();
	
	
	private Path mPath = new Path();
	private RectF mRectF = new RectF();
	RelativeLayout.LayoutParams mLayoutParams;
	/**
	 * @fields startAngle : 开始的角度
	 */
	private float startAngle = -125;
	/**
	 * @fields sweepAngle : 要过的角度
	 */
	private float sweepAngle = 70;

	private BigBarView mBarView;

	public BigCircleSeekBarView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BigCircleSeekBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		initPaint();
		mLayoutParams = new LayoutParams(context, attrs);
		mLayoutParams.width = LayoutParams.MATCH_PARENT;
		mLayoutParams.height = LayoutParams.MATCH_PARENT;

		TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.BigWheel);
		
		devideNumber = mTypedArray.getInt(R.styleable.BigWheel_bigwheel_devider_number, 0);
		
		TYPE = mTypedArray.getInt(R.styleable.BigWheel_bigwheel_type, -1);

		mBarView = new BigBarView(context);
		mBarView.setDrawableID(mTypedArray.getResourceId(R.styleable.BigWheel_bigwheel_bar, -1));
		mBarView.setPressedDrawableID(mTypedArray.getResourceId(R.styleable.BigWheel_bigwheel_bar_pressed, -1));
		
		addView(mBarView, 0, mLayoutParams);
		
		int sunID = mTypedArray.getResourceId(R.styleable.BigWheel_bigwheel_wb_small_sun, -1);
		
		if (sunID != -1) {
			sunBitmap = BitmapFactory.decodeResource(getResources(), sunID);
		}
		
		int cludeID = mTypedArray.getResourceId(R.styleable.BigWheel_bigwheel_wb_small_clude, -1);
		if (cludeID != -1) {
			cludeBitmap = BitmapFactory.decodeResource(getResources(), cludeID);
		}
		int riguangID = mTypedArray.getResourceId(R.styleable.BigWheel_bigwheel_wb_small_riguang, -1);
		if (riguangID != -1) {
			riguangBitmap = BitmapFactory.decodeResource(getResources(), riguangID);
		}
		int yingguangID = mTypedArray.getResourceId(R.styleable.BigWheel_bigwheel_wb_small_yingguang, -1);
		if (yingguangID != -1) {
			yingguangBitmap = BitmapFactory.decodeResource(getResources(), yingguangID);
		}
		

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onTouch()" + "CX : " + cX + "CY : " + cY);
				
				//v.setY(event.getRawY());

				float touchX = event.getX();
				float touchY = event.getY();
				
				if (touchY > viewHeight) {
					touchY = viewHeight;
				}
				
				if (touchY < 0) {
					touchY = 0;
				}
				
				Log.d(TAG+"onTouch() ", "touch X : " + touchX + "  touch Y : " + touchY + "  bottom : " + viewHeight);
				float rotation = calRotation(cX, cY, touchX, touchY);
				
				if (rotation > 30) {
					rotation = 30;
				}
				
				if (rotation < -30) {
					rotation = -30;
				}
				
				View view = getChildAt(0);
				view.setPivotX(cX);
				view.setPivotY(cY);
				view.setRotation(rotation);
				mBarView.setCurrentState(event.getAction());
				//mBarView.setRotation(30);

				return true;
			}
		});
		
		
		//mBarView.setRotation(40f);
	}

	/**
	 * description: 初始化画笔
	 * 
	 * @creater wangdy
	 * @date Nov 18, 2013 5:54:51 PM
	 * @editer
	 */
	private void initPaint() {
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xFFFFFFFF);
		mPaint.setStrokeWidth(4);
		mPaint.setStyle(Paint.Style.STROKE);
		
		
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(0xFFFFFFFF);
		mTextPaint.setStrokeWidth(1);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setTextSize(20);

		this.setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);// 取消硬件加速
	}

	/**
	 * description: 根据触摸点计算出旋转角度
	 * 
	 * @creater wangdy
	 * @param touch_x
	 * @param touch_y
	 * @return
	 * @date Nov 18, 2013 5:55:35 PM
	 * @editer
	 */
	private float calRotation(float centerX, float centerY, float touch_x,
			float touch_y) {

		float dx = touch_x - centerX;
		float dy = centerY - touch_y;
		float tanR = dx / dy; // Tan数值
		return (float) (Math.atan(tanR) * 180 / Math.PI);
	}

	public BigCircleSeekBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(TAG, "onMeasure() ==  " + "getTop() : " + getTop()
				+ "   getRight() : " + getRight());

		// setMeasuredDimension(getWidth() + 500, getWidth() + 500);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Log.d(TAG, "onDraw() ==  " + "getTop() : " + getTop()
				+ "   getRight() : " + getRight());

		canvas.drawPath(mPath, mPaint);
		
		canvas.drawRect(mRectF, mPaint);
		
		switch (TYPE) {
		case EXPOSURE: {
			for (int i = 0; i < 5; i++) {
				if (i == 0) {
					canvas.drawTextOnPath("" + (i - 2), mPath, 65, -10, mTextPaint);
				}else if (i == 4) {
					canvas.drawTextOnPath("" + (i - 2), mPath, getWidth() - 65, -10, mTextPaint);
				}else {
					canvas.drawTextOnPath("" + (i - 2), mPath, getWidth() / 4 * i, -10, mTextPaint);
				}
			}

			break;
		}
		case ISO: {

			for (int i = 0; i < 6; i++) {
				if (i == 0) {
					canvas.drawTextOnPath("auto", mPath, 65, -10, mTextPaint);
				}
				
				if (i == 1) {
					canvas.drawTextOnPath("100", mPath, getWidth() / 5 * i , -10, mTextPaint);
				}
				if (i == 2) {
					canvas.drawTextOnPath("200", mPath, getWidth() / 5 * i, -10, mTextPaint);
				}
				if (i == 3) {
					canvas.drawTextOnPath("400", mPath, getWidth() / 5 * i, -10, mTextPaint);
				}
				if (i == 4) {
					canvas.drawTextOnPath("800", mPath, getWidth() / 5 * i, -10, mTextPaint);
				}
				if (i == 5) {
					canvas.drawTextOnPath("1600", mPath, getWidth() - 65, -10, mTextPaint);
				}
			}
			
			break;
		}
		case SHUTTER: {

			for (int i = 0; i < 5; i++) {
				if (i == 0) {
					canvas.drawTextOnPath("auto", mPath, 65, -10, mTextPaint);
				}
				
				if (i == 1) {
					canvas.drawTextOnPath("1/800", mPath, getWidth() / 4 * i , -10, mTextPaint);
				}
				if (i == 2) {
					canvas.drawTextOnPath("1/60", mPath, getWidth() / 4 * i, -10, mTextPaint);
				}
				if (i == 3) {
					canvas.drawTextOnPath("1/4", mPath, getWidth() / 4 * i, -10, mTextPaint);
				}
				if (i == 4) {
					canvas.drawTextOnPath("4", mPath, getWidth() - 65, -10, mTextPaint);
				}

			}
			break;
		}
		case MF: {

			for (int i = 0; i < 2; i++) {
				if (i == 0) {
					canvas.drawTextOnPath("0", mPath, 65, -10, mTextPaint);
				}
				
				if (i == 1) {
					canvas.drawTextOnPath("+∞", mPath, getWidth() -65 , -10, mTextPaint);
				}
			}
			break;
		}

		case WB: {
			for (int i = 0; i < 5; i++) {
				
				if (i == 0) {
					canvas.drawTextOnPath("auto", mPath, 65, -10, mTextPaint);
				}
				
				if (i == 1) {
					
					canvas.save();
					canvas.rotate(-15, cX, cY);
					canvas.drawBitmap(yingguangBitmap, (getWidth() - yingguangBitmap.getWidth()) / 2, 210,mPaint);
					canvas.restore();
				}
				if (i == 2) {
					
					canvas.drawBitmap(sunBitmap, (getWidth() - sunBitmap.getWidth()) / 2, 210,mPaint);
				}
				
				if (i == 3) {
					
					canvas.save();
					canvas.rotate(15, cX, cY);
					canvas.drawBitmap(riguangBitmap, (getWidth() - riguangBitmap.getWidth()) / 2, 210,mPaint);
					canvas.restore();
				}
				if (i == 4) {
					
					canvas.save();
					canvas.rotate(30, cX, cY);
					canvas.drawBitmap(cludeBitmap, (getWidth() - cludeBitmap.getWidth()) / 2, 210,mPaint);
					canvas.restore();
				}

			}

			break;
		}

		default:
			break;
		}
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

		Log.d(TAG, "onLayout() ==  " + "getTop() : " + getTop()
				+ "   getRight() : " + getRight() + "   getBottom() : " + getBottom());

		int screenWidth = getWidth();
		
		float offset = (2308 - screenWidth) >> 1;
		
		int topOffset = 250;

		mRectF.set(- offset, 
				topOffset, 
				screenWidth + offset, 
				screenWidth + offset * 2 + topOffset);
		
		mPath.addArc(mRectF, startAngle, sweepAngle);

		cX = mRectF.centerX();
		cY = mRectF.centerY();

		View v = getChildAt(0);
		v.layout(l, t - this.getTop(), r, b);
		
		viewHeight = getHeight();
	}
	
	public interface OnBigWheelBarChangeListener{
		public void onBigWheelBarChanged(int value);
	}
	private OnBigWheelBarChangeListener mOnBigWheelBarChangeListener = null;
	
	/**
	 * description: 设置监听
	 * @creater wangdy
	 * @param mListener
	 * @date Nov 19, 2013 3:28:01 PM
	 * @editer 	
	 */
	public void setOnBigWheelBarChangeListener(OnBigWheelBarChangeListener mListener){
		this.mOnBigWheelBarChangeListener = mListener;
	}
}
