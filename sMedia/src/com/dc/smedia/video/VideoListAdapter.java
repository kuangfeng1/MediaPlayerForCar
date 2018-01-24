package com.dc.smedia.video;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dc.smedia.R;
import com.dc.smedia.until.Until;

public class VideoListAdapter extends  BaseAdapter{
	Context mContext;
	public  ArrayList<VideoItem> mvideoItems =new ArrayList<VideoItem>();
	public  VideoItem mItems=new VideoItem();
	public VideoListAdapter(Context context){
		mContext=context;
	}
	public void updateListView(ArrayList<VideoItem> mvideoItems){
//		Log.e("tag","MusicListAdapter updateListView()调用");
		this.mvideoItems.clear();
		if(mvideoItems!=null){
			this.mvideoItems =new ArrayList<VideoItem>(mvideoItems);	
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		Log.e("tag","VideoListAdapter getCount()调用"+mvideoItems.size());

		return mvideoItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
//		Log.e("tag","VideoListAdapter getItem()调用");
		return mvideoItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parentView) {
		// TODO Auto-generated method stub
//		Log.e("tag","VideoListAdapter getView()调用");
		ViewHolder mholder =null;
		if(convertview==null){
			convertview =LayoutInflater.from(mContext).inflate(R.layout.videolistitem, null);
			mholder=new ViewHolder();
			mholder.video_title=(TextView) convertview.findViewById(R.id.videolist_title);
//			mholder.video_image =(ImageView) convertview.findViewById(R.id.video_image);
			convertview.setTag(mholder);
		}else{
			mholder=(ViewHolder) convertview.getTag();
		}
//		if(mvideoItems.get(position).getVideo_photo() ==null){
//			mholder.video_image.setBackgroundResource(R.drawable.ic_launcher);
//		}else{
//			Bitmap bm=mvideoItems.get(position).getVideo_photo();
//			BitmapDrawable bd=new BitmapDrawable(bm);
//			mholder.video_image.setBackgroundDrawable(bd);
//		}
		
		String title = Until.PathtoTitle(mvideoItems.get(position).getVideo_path());
		mholder.video_title.setText((position+1)+"."+title);
		convertview.setBackgroundResource(R.drawable.item_normal);
		convertview.getBackground().setAlpha(100);
		return convertview;
	}
	
	class ViewHolder{
		
		TextView  video_title;
//		ImageView video_image;
	} 

}
