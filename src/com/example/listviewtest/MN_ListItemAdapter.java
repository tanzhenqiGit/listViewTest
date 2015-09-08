/**
 * @author tan_zhenq E-mail: tan_zhenqi@163.com
 * @date 创建时间：2015-9-6 下午3:37:21 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
package com.example.listviewtest;

import com.example.listviewtest.MN_MenuView.OnMyMenuItemClickListener;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

/**
 * @author lz100
 *
 */

abstract public class MN_ListItemAdapter implements WrapperListAdapter  {

	@Override
	public boolean areAllItemsEnabled() {
		return mAdapter.areAllItemsEnabled();
	}


	@Override
	public boolean isEnabled(int arg0) {
		return mAdapter.isEnabled(arg0);
	}


	@Override
	public int getCount() {
		if (mAdapter == null) {
			return 0;
		} else {
			return mAdapter.getCount();
		}
	}


	@Override
	public Object getItem(int arg0) {
		if (mAdapter == null) {	
			return null;
		} else {
			return mAdapter.getItem(arg0);
		}
	}


	@Override
	public long getItemId(int arg0) {
		if (mAdapter == null) {	
			return 0;
		} else {
			return mAdapter.getItemId(arg0);
		}
	}


	@Override
	public int getItemViewType(int arg0) {
		return mAdapter.getItemViewType(arg0);
	}


	@Override
	public View getView(int postion, View layView, ViewGroup parent) {
		MN_ListItemLayout layout = null;
		if (layView == null) {
			View view = mAdapter.getView(postion, layView, parent);
			NM_MenuItemContain contain = new NM_MenuItemContain(mContext);
			contain.setViewType(mAdapter.getItemViewType(postion));
			createMenu(contain);
			MN_MenuView menuView = new MN_MenuView(contain, (MN_ListView) parent);
			menuView.setMenuItemClickListener(mMenuItemListener);
			MN_ListView listview = (MN_ListView) parent;
			layout = new MN_ListItemLayout(view, menuView, listview.getOpenInterpolator(), 
					listview.getCloseInterpolator());
			layout.setPosition(postion);
		} else {
			layout = (MN_ListItemLayout) layView;
			layout.closeMenu();
			layout.setPosition(postion);
			mAdapter.getView(postion, layout.getContentView(), parent);
		}
		
		return layout;
	}
	@Override
	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount();
	}


	@Override
	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}


	@Override
	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}


	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		mAdapter.registerDataSetObserver(arg0);
	}


	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		mAdapter.unregisterDataSetObserver(arg0);
	}


	@Override
	public ListAdapter getWrappedAdapter() {
		return mAdapter;
	}

	public MN_ListItemAdapter(Context context, ListAdapter adapter)
	{
		mAdapter = adapter;
		mContext = context;
	}
	

	private OnMyMenuItemClickListener mMenuItemListener = new OnMyMenuItemClickListener() {
		
		@Override
		public void onItemClickedListener(MN_MenuView menuView,
				NM_MenuItemContain contain, int id) {
			MN_ListItemAdapter.this.onItemClickedListener(menuView, contain, id);
			
		}
	};
	
	abstract public void  onItemClickedListener(MN_MenuView menuView, NM_MenuItemContain contain, int id);
	abstract public void createMenu(NM_MenuItemContain menu);
	
	private ListAdapter mAdapter;
	private Context mContext;

}
