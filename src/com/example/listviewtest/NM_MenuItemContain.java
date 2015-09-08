/**
 * @author tan_zhenq E-mail: tan_zhenqi@163.com
 * @date 创建时间：2015-9-6 下午3:55:34 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
package com.example.listviewtest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;


/**
 * @author lz100
 *
 */
public class NM_MenuItemContain {
	private Context mContext;
	private List<MN_MenuItem> mItems;
	private int mViewType;
	
	public NM_MenuItemContain(Context context)
	{
		mContext = context;
		mItems = new ArrayList<MN_MenuItem>();
	}
	
	public Context getContext()
	{
		return mContext;
	}

	public List<MN_MenuItem> getItems()
	{
		return mItems;
	}
	public int getViewType()
	{
		return mViewType;
	}
	
	public void setViewType(int viewType)
	{
		mViewType = viewType;
	}
	
	public void addMenuItem(MN_MenuItem item)
	{
		mItems.add(item);
	}
	
	public void removeMenuItem(MN_MenuItem item)
	{
		mItems.remove(item);
	}
	
	public MN_MenuItem getMenuItem(int index)
	{
		return mItems.get(index);
	}
}
