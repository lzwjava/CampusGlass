package lzw.util;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SendCallback;

public class GlassUtil{
	public static void throwGlass(String words,String toUser){
		AVUser user=AVUser.getCurrentUser();
		String id=user.getString("installationId");
		AVObject glass=new AVObject("Glass");
		glass.put("installationId",id);
		glass.put("username",user.getUsername());
		if(toUser!=null){
			glass.put("toUser",toUser);
		}
		glass.put("words",words);
		glass.saveInBackground();
	}
	
	public static void pushToId(String reply, String id) {
		AVQuery pushQuery = AVInstallation.getQuery();
		pushQuery.whereEqualTo("installationId", id);
		AVPush.sendMessageInBackground(reply, pushQuery,
				new SendCallback() {
					@Override
					public void done(AVException e) {
					}
				});
	}
}
