package lzw.campus.glass;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMsgViewAdapter extends BaseAdapter{
	public static interface IMsgViewType{
		int IMVT_COM_MSG=0;
		int IMVT_TO_MSG=1;
	}
	
	String TAG=ChatMsgViewAdapter.class.getSimpleName(); 
	List<ChatMsgEntity>coll;
	Context ctx;
	LayoutInflater mInflater;
	public ChatMsgViewAdapter(Context context,List<ChatMsgEntity>
	 coll){
		ctx=context;
		this.coll=coll;
		mInflater=LayoutInflater.from(context);
	}
	
	public int getCount(){
		return coll.size();
	}
	
	public Object getItem(int position){
		return coll.get(position);
	}
	
	public long getItemId(int position){
		return position;
	}
	
	public int getItemViewType(int position){
		ChatMsgEntity entity=coll.get(position);
		if(entity.getMsgType()){
			return IMsgViewType.IMVT_COM_MSG;
		}else return IMsgViewType.IMVT_TO_MSG;
	}
	
	public int getViewTypeCount(){
		return 2;
	}
	
	public View getView(int position,View 
	 convertView,ViewGroup parent){
		ChatMsgEntity entity=coll.get(position);
		boolean isComMsg=entity.getMsgType();
		ViewHolder viewHolder=null;
		if(convertView==null){
			if(isComMsg){
				convertView=mInflater.inflate(
				  R.layout.chatting_item_msg_text_left,null);
			}else{
				convertView=mInflater.inflate(
					R.layout.chatting_item_msg_text_right,null);
			}
			
			viewHolder=new ViewHolder();
			viewHolder.tvSendTime=(TextView)
			  convertView.findViewById(R.id.tv_sendTime);
			viewHolder.tvUserName=(TextView)
				convertView.findViewById(R.id.tv_userName);
			viewHolder.tvContent=(TextView)
				convertView.findViewById(R.id.tv_chatContent);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		
		viewHolder.tvSendTime.setText(entity.date);
		viewHolder.tvUserName.setText(entity.name);
		viewHolder.tvContent.setText(entity.text);
		return convertView;
	}
	
	static class ViewHolder{
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public boolean isComMsg=true;
	}
}