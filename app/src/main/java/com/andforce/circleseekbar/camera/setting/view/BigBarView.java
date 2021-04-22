package com.andforce.circleseekbar.camera.setting.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

public class BigBarView extends AppCompatImageView {

	/**
	 * @fields mBitmap : 控制按钮的图片
	 */
	private Bitmap mBitmap = null;
	private Paint mPaint;

	private int CURRENT_STATE = -1;
	
	//private List<Bitmap> mSelector = new ArrayList<Bitmap>();
	private Bitmap[] mSelector = new Bitmap[2];
	
	private static final int RES_ID = 0;
	private static final int PRESSED_RED_ID = 1;
	private int[] resID = new int[2];
	

	public BigBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public BigBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		// this.setBackgroundColor(0xFFFF0000);

	}

	public BigBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setPressedDrawableID(int pressedid){
		resID[PRESSED_RED_ID] = pressedid;
		Bitmap pressedBitmap = BitmapFactory.decodeResource(getResources(),pressedid);
		mSelector[PRESSED_RED_ID] = pressedBitmap;
		invalidate();// 更新UI
	}

	public void setDrawableID(int resid){
		resID[RES_ID] = resid;
		Bitmap resBitmap = BitmapFactory.decodeResource(getResources(),resid);
		mSelector[RES_ID] = resBitmap;
		invalidate(); //更新UI
	}
	
	public void setCurrentState(int state){
		if (state == CURRENT_STATE) {
			return;
		}else {
			CURRENT_STATE = state;
			invalidate();// 更新UI
		}
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mSelector[RES_ID] == null || mSelector[RES_ID].isRecycled()) {
			return;
		}
		
		if (mSelector[PRESSED_RED_ID] == null || mSelector[PRESSED_RED_ID].isRecycled()) {
			return;
		}
		
		
		if (CURRENT_STATE == MotionEvent.ACTION_DOWN || CURRENT_STATE == MotionEvent.ACTION_MOVE) {
			mBitmap = mSelector[PRESSED_RED_ID];
		} else {
			mBitmap = mSelector[RES_ID];
		}
		canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2, 200,mPaint);
	}

}
