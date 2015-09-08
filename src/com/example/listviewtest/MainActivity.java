package com.example.listviewtest;

import java.util.List;



import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
   


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Log.d("test", "action_settings");
			if (MN_ListItemLayout.getMode() == MN_ListItemLayout.MODE_NORMAL) {
				MN_ListItemLayout.setMode(MN_ListItemLayout.MODE_EDIT);
			} else {
				MN_ListItemLayout.setMode(MN_ListItemLayout.MODE_NORMAL);
			}
			mList.setAdapter(mAdapter);
			break;
		default: break;
		}
		return super.onOptionsItemSelected(item);
	}


	private void initialize()
    {
    	mList = (MN_ListView) findViewById(R.id.list_view);
    	mAppList = getPackageManager().getInstalledApplications(0);
    	mAdapter = new AppAdapter();
    	mList.setAdapter(mAdapter);
    	mList.setOnItemClickListener(mListener);
    	
    }
    
    class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_list_app, null);
				ViewHolder viewholder = new ViewHolder(convertView);
				convertView.setTag(viewholder);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ApplicationInfo item = getItem(position);
			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.tv_name.setText(item.loadLabel(getPackageManager()));
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
			}
		}
	}
    
    
	private List<ApplicationInfo> mAppList;
	private AppAdapter mAdapter;
	private MN_ListView mList;
	
	private OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long id) {
	
			Log.d("test", "pos:" + pos + ",id:" + id);
		}
	};
    
}
