/**
 * @author tan_zhenq E-mail: tan_zhenqi@163.com
 * @date 创建时间：2015-9-6 下午4:39:12 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
package com.example.listviewtest;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author lz100
 *
 */
public class MN_MenuView extends LinearLayout {

	/**
	 * @param context
	 */
	public MN_MenuView(Context context) {
		super(context);
	}
	
	public MN_MenuView(NM_MenuItemContain contain, MN_ListView listView)
	{
		super(contain.getContext());
		mMenuContain = contain;
		mListView = listView;
		List<MN_MenuItem> list = mMenuContain.getItems();
		int id = 0;
		Log.d("test", "MN_MenuView size:" + list.size());
		for (MN_MenuItem item : list) {
			addItem(item, id);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void addItem(MN_MenuItem item, int id)
	{
		LayoutParams layoutParams = new LayoutParams(item.getWidth(), LayoutParams.MATCH_PARENT);
		LinearLayout parent = new LinearLayout(getContext());
		parent.setId(item.getId());
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setGravity(Gravity.CENTER);
		parent.setLayoutParams(layoutParams);
		parent.setBackgroundDrawable(item.getBackground());
		if (item.getIcon() != null) {
			parent.addView(createIcon(item));
		}
		
		if (!TextUtils.isEmpty(item.getTitle())) {
			parent.addView(creatText(item));
		} 
		
		parent.setOnClickListener(mListener);
		
		addView(parent);
	}
	
	private ImageView createIcon(MN_MenuItem item)
	{
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}
	
	private TextView creatText(MN_MenuItem item)
	{
		TextView tv = new TextView(getContext());
		tv.setText(item.getTitle());
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	
	private OnClickListener mListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (mMenuClickListener != null) {
				mMenuClickListener.onItemClickedListener(MN_MenuView.this, mMenuContain, v.getId());
			}
		}
	};
	
	public interface OnMyMenuItemClickListener
	{
		void onItemClickedListener(MN_MenuView menuView, NM_MenuItemContain contain, int id);
	}
	
	public void setMenuItemClickListener(OnMyMenuItemClickListener listener)
	{
		mMenuClickListener = listener;
	}
	public OnMyMenuItemClickListener getMenuItemClickListener()
	{
		return mMenuClickListener;
	}
	
	public void setMenuLayout(MN_ListItemLayout layout)
	{
		mMenuLayout = layout;
	}
	
	public MN_ListItemLayout getMenuLayout()
	{
		return mMenuLayout;
	}
	
	public void setPosition(int position)
	{
		mPosition = position;
	}
	
	public int getPosition()
	{
		return mPosition;
	}
	public MN_ListView mListView;
	private NM_MenuItemContain mMenuContain;
	private MN_ListItemLayout mMenuLayout;
	private OnMyMenuItemClickListener mMenuClickListener;
	private int mPosition;
	

}
