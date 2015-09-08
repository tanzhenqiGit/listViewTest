/**
 * @author tan_zhenq E-mail: tan_zhenqi@163.com
 * @date 创建时间：2015-9-6 下午4:17:29 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
package com.example.listviewtest;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author lz100
 * 
 */
public class MN_ListItemLayout extends FrameLayout {

	
	/**
	 * @param context
	 */
	public MN_ListItemLayout(Context context) {
		super(context);
	}
	
	public MN_ListItemLayout(View view, MN_MenuView menuview)
	{
		this(view, menuview, null, null);
	}	
	
	public MN_ListItemLayout(View view, MN_MenuView menuview, Interpolator open, Interpolator close)
	{
		super(view.getContext());
		mCloseInterpolator = close;
		mOpenInterpolator = open;
		mContentView = view;
		mMenuView = menuview;
		initialize();
	}
	
	public View getContentView()
	{
		return mContentView;
	}
	
	public MN_MenuView getMenuView()
	{
		return mMenuView;
	}
	
	private void initialize()
	{
		// setlayout width match parent, height warp content
//		mContentView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if (isOpen()) {
//					smoothCloseMenu();
//				} else {
//					smoothOpenMenu();
//				}
//			}
//		});
		setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		// set gesture detector
		mGestureDetectorCompat = new GestureDetectorCompat(getContext(), mGestureListener);
		
		if (mCloseInterpolator != null) {
			mCloseScroller = ScrollerCompat.create(getContext(), mCloseInterpolator);
		} else {
			mCloseScroller = ScrollerCompat.create(getContext());
		}

		if (mOpenInterpolator != null) {
			mOpenScroller = ScrollerCompat.create(getContext(), mOpenInterpolator);
		} else {
			mOpenScroller = ScrollerCompat.create(getContext());
		}
		
		mContentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mContentView.setId(CONTENT_VIEW_ID);
		
		mMenuView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT));
		mMenuView.setId(MENU_VIEW_ID);
		
		mButton = new Button(getContext());
		mButton.setText("test");
		mButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d("test", "button onclicked");
				if (isOpen()) {
					smoothCloseMenu();
				} else {
					smoothOpenMenu();
				}
			}
		});
		if (MODE == MODE_NORMAL) {
			mButton.setVisibility(View.GONE);
		} else {
			mButton.setVisibility(View.VISIBLE);
		}
		addView(mButton);
		addView(mContentView);
		addView(mMenuView);

	}
	
	public void setPosition(int position)
	{
		mPosition = position;
		mMenuView.setPosition(position);
	}
	
	public int getPosition()
	{
		return mPosition;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mButton.layout(0, 0, mButton.getMeasuredWidth(), mButton.getMeasuredHeight());
		
		mContentView.layout(mButton.getMeasuredWidth() , 0, getMeasuredWidth()
				, mContentView.getMeasuredHeight());
		mMenuView.layout(getMeasuredWidth(), 0,
			getMeasuredWidth() + mMenuView.getMeasuredWidth(), mMenuView.getMeasuredHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
			MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
	}

	

	@Override
	public void computeScroll() {
		if (mState == STATE_OPEN) {
			if (mOpenScroller.computeScrollOffset()) {
				swipe(mOpenScroller.getCurrX());
				postInvalidate();
			}
		} else {
			if (mCloseScroller.computeScrollOffset()) {
				swipe(mBaseX - mCloseScroller.getCurrX());
				postInvalidate();
			}
		}
	}
	
	
	public boolean onSwipe(MotionEvent event)
	{
		mGestureDetectorCompat.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDown = (int) event.getX();
			mIsFling = false;
		case MotionEvent.ACTION_MOVE:
			int distance = mDown - (int)event.getX();
			if (mState == STATE_OPEN) {
				distance += mMenuView.getWidth();
			}
			swipe(distance);
			break;
		case MotionEvent.ACTION_UP:
			if (mIsFling || mDown - (int) event.getX() > mMenuView.getWidth() / 2) {
				smoothOpenMenu();
			} else {
				smoothCloseMenu();
			}
			break;
		default:
			break;
		}
		return true;
	}

	public boolean isOpen()
	{
		return mState == STATE_OPEN;
	}
	
	public void smoothOpenMenu()
	{
		Log.d("test", "smoothOpenMenu:" + mContentView.getLeft());
		mState = STATE_OPEN;
		mOpenScroller.startScroll(-mButton.getLeft(), 0, mMenuView.getWidth(), 0, 350);
		postInvalidate();
	}
	
	public void smoothCloseMenu()
	{
		Log.d("test", "smoothCloseMenu:" + mContentView.getLeft());
		mState = STATE_CLOSE;
		//mBaseX = - mContentView.getLeft();
		mBaseX = - mButton.getLeft();
		mCloseScroller.startScroll(0, 0, mBaseX, 0, 350);
		postInvalidate();
	}
	
	public void openMenu()
	{
		if (mState == STATE_CLOSE) {
			mState = STATE_OPEN;
			swipe(mMenuView.getWidth());
		}
	}
	
	public void closeMenu()
	{
		if (mCloseScroller.computeScrollOffset()) {
			mCloseScroller.abortAnimation();
		}
		if (mState == STATE_OPEN) {
			mState = STATE_CLOSE;
			swipe(0);
		}
	}
	
	private void swipe(int distance)
	{
		Log.d("test", "swipe dis:" + distance +
				",width:" + mMenuView.getWidth() + "content:w=" + mContentView.getWidth() +
				",mW:"+ mButton.getWidth() + "mWw:" + mButton.getMeasuredWidth());
		if (distance > mMenuView.getWidth()) {
			distance = mMenuView.getWidth();
		}
		if (distance < 0) {
			distance = 0;
		}
		
		mButton.layout(-distance, mButton.getTop(), mButton.getWidth() - distance, mButton.getBottom());
		
		mContentView.layout(mButton.getWidth() - distance, mContentView.getTop(),
				mContentView.getWidth() + mButton.getWidth() - distance, getMeasuredHeight());
		
		mMenuView.layout(mButton.getWidth() + mContentView.getWidth() - distance, mMenuView.getTop(),
				mButton.getWidth() + mContentView.getWidth() + mMenuView.getWidth() - distance,
				mMenuView.getBottom());
		
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}
	
	private OnGestureListener mGestureListener = new SimpleOnGestureListener()
	{
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() > MIN_FLING && velocityX < MAX_VELOCITYX) {
				mIsFling = true;
			}
			Log.d("test", "e1:X:" + e1.getX() + ",e2:X:" + e2.getX() + ",velocityX:" + velocityX);
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			mIsFling = false;
			return true;
		}
		
	};
	
	public static int getMode()
	{
		return MODE;
	}
	
	public static void setMode(int mode) {
		MODE = mode;
	}
	
	private final int CONTENT_VIEW_ID = 1;
	private final int MENU_VIEW_ID = 2;
	private final int STATE_CLOSE = 0;
	private final int STATE_OPEN = 1;
	private int mState = STATE_CLOSE;
	private GestureDetectorCompat mGestureDetectorCompat;
	private Interpolator mCloseInterpolator, mOpenInterpolator;
	private ScrollerCompat mCloseScroller, mOpenScroller;
	private View mContentView;
	private MN_MenuView mMenuView;
	private boolean mIsFling = false;
	private int MIN_FLING = dp2px(15);
	private int MAX_VELOCITYX= -dp2px(500);
	private int mPosition;
	private int mDown;
	private int mBaseX;
	private Button mButton;
	public final static int MODE_NORMAL = 0;
	public final static int MODE_EDIT = 1;
	private static int MODE = MODE_NORMAL;
	

	

}
