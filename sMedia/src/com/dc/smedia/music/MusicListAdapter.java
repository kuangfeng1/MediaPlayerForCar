package com.dc.smedia.music;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dc.smedia.R;
import com.dc.smedia.until.Until;

public class MusicListAdapter extends  BaseAdapter{
	Context mContext;
	public  ArrayList<MusicItem> mmusicItems =new ArrayList<MusicItem>();
	public  MusicItem mItems=new MusicItem();
	public MusicListAdapter(Context context){
		mContext=context;
	}
	public void updateListView(ArrayList<MusicItem> mmusicItems){
//		Log.e("tag","MusicListAdapter updateListView()调用");
		this.mmusicItems.clear();
		if(mmusicItems!=null){
			this.mmusicItems =new ArrayList<MusicItem>(mmusicItems);	
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		Log.e("tag","MusicListAdapter getCount()调用"+mmusicItems.size());

		return mmusicItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
//		Log.e("tag","MusicListAdapter getItem()调用");
		return mmusicItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertview, ViewGroup parentView) {
		// TODO Auto-generated method stub
//		Log.e("tag","MusicListAdapter getView()调用");
		ViewHolder mholder =null;
		if(convertview==null){
			convertview =LayoutInflater.from(mContext).inflate(R.layout.musiclistitem, null);
			mholder=new ViewHolder();
			mholder.music_title=(TextView) convertview.findViewById(R.id.musiclist_title);
			
			convertview.setTag(mholder);
		}else{
			mholder=(ViewHolder) convertview.getTag();
		}
//		Bitmap bm=mmusicItems.get(position).getVideo_photo();
//		BitmapDrawable bd=new BitmapDrawable(bm);
//		mholder.video_image.setBackgroundDrawable(bd);
		String title = Until.PathtoTitle(mmusicItems.get(position).getMusic_path());
		mholder.music_title.setText(position+1+"."+title);
//		convertview.setBackgroundResource(R.drawable.item_normal);
//		convertview.getBackground().setAlpha(100);
		return convertview;
	}
	
	class ViewHolder{
		
		TextView  music_title;
	} 

}
