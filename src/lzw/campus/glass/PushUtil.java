package lzw.campus.glass;

import org.json.JSONException;
import org.json.JSONObject;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.SendCallback;

public class PushUtil{
	static void pushAll(){
		AVPush push=new AVPush();
		JSONObject obj=new JSONObject();
		try{
		  obj.put("alert","new");
		}catch(JSONException e){
			e.printStackTrace();
		}
		push.setPushToAndroid(true);
		push.setData(obj);
		push.sendInBackground(new SendCallback(){
			public void done(AVException e){
				if(e==null){
					
				}else{
					
				}
			}
		});
	}
}