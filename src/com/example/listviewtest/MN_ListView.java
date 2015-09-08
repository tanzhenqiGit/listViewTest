/**
 * @author tan_zhenq E-mail: tan_zhenqi@163.com
 * @date 创建时间：2015-9-6 下午3:28:22 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
package com.example.listviewtest;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author lz100
 *
 */
public class MN_ListView extends ListView {

	/**
	 * @param context
	 */
	public MN_ListView(Context context) {
		super(context);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public MN_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MN_ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	public void setAdapter(ListAdapter adapter) {
		  mListItemAdapter = new MN_ListItemAdapter(getContext(), adapter){

			@Override
			public void onItemClickedListener(MN_MenuView menuView,
					NM_MenuItemContain contain, int id) {
				Log.d("test", "id:" + id);
				
			}
			@Override
			public void createMenu(NM_MenuItemContain menu) {
				MN_MenuItem item = new MN_MenuItem(getContext());
				item.setTitle("Item_1");
				item.setId(1);
				item.setBackground(new ColorDrawable(Color.GRAY));
				item.setWidth(100);
				item.setTitleColor(Color.RED);
				menu.addMenuItem(item);
		
				item = new MN_MenuItem(getContext());
				item.setTitle("Item_2");
				item.setId(2);
				item.setTitleColor(Color.RED);
				item.setBackground(new ColorDrawable(Color.RED));
				item.setWidth(100);
				menu.addMenuItem(item);
				
			}
			
		};
		super.setAdapter(mListItemAdapter);
	}

	public Interpolator getOpenInterpolator()
	{
		return mOpenInterpolator;
	}
	
	public void setOpenInterpolator(Interpolator open)
	{
		mOpenInterpolator = open;
	}
	

	public Interpolator getCloseInterpolator()
	{
		return mCloseInterpolator;
	}
	
	public void setCloseInterpolator(Interpolator close)
	{
		mCloseInterpolator = close;
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null) {
			Log.d("test", "onTouch getaction is donw and mTouchView == null.");
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			int oldPos = mTouchPosition;
			mTouchState = TOUCH_STATE_N;
			mTouchPosition = pointToPosition((int) event.getX(), (int) event.getY());
			if (oldPos == mTouchPosition && mTouchView != null && mTouchView.isOpen()) {
				mTouchState = TOUCH_STATE_X;
				mTouchView.onSwipe(event);
				return true;
			}
			View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
			if (mTouchView != null && mTouchView.isOpen()) {
				mTouchView.smoothCloseMenu();
				mTouchView = null;
				return super.onTouchEvent(event);
			}
			if (view instanceof MN_ListItemLayout) {
				mTouchView = (MN_ListItemLayout) view;
			}
			if (mTouchView != null) {
				mTouchView.onSwipe(event);
			}
			
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(event.getX() - mDownX);
			float dy =  Math.abs(event.getY() - mDownY);
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					mTouchView.onSwipe(event);
				}
				getSelector().setState(new int [] { 0 });
				event.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(event);
				return true;
			} else if (mTouchState == TOUCH_STATE_N) {
				if (Math.abs(dy) > MAX_Y) {
					mTouchState = TOUCH_STATE_Y;
				} else if (dx > MAX_X) {
					mTouchState = TOUCH_STATE_X;
					//if (mOnSwipeListener != null) {
					//	mOnSwipeListener.onSwipeStart(mTouchPosition);
					//}
				}
			} else {}
			
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					mTouchView.onSwipe(event);
					if (!mTouchView.isOpen()) {
						mTouchPosition = -1;
						mTouchView = null;
					}
				}
				//if (mOnSwipeListener != null) {
				//	mOnSwipeListener.onSwipeEnd(mTouchPosition);
				//}
				event.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(event);
				return true;
			}
			break;
		default: break;
		}
		return super.onTouchEvent(event);
	}
	
	private MN_ListItemAdapter mListItemAdapter;

	private Interpolator mCloseInterpolator, mOpenInterpolator;
	private int MAX_Y = 5;
	private int MAX_X = 3;
	private float mDownX;
	private float mDownY;
	private static final int TOUCH_STATE_N = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;
	private int mTouchState;
	private int mTouchPosition;
	private MN_ListItemLayout mTouchView;



}
