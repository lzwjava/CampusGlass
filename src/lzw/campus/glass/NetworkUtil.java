package lzw.campus.glass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;

public class NetworkUtil{
	static	boolean isConnect(Context cxt){
		ConnectivityManager conMan=(ConnectivityManager)
			cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile=conMan.getNetworkInfo(ConnectivityManager
			.TYPE_MOBILE).getState();
		State wifi=conMan.getNetworkInfo(ConnectivityManager
			.TYPE_WIFI).getState();
		if(mobile==State.CONNECTED ||
			 wifi==State.CONNECTED){
			return true;
		}else return false;
	}
	
	static void promptConnect(final Context cxt){
		AlertDialog.Builder builder=new 
			AlertDialog.Builder(cxt);
		//Looper.prepare();
		builder.setTitle("������ʾ")
		  .setMessage("�����������Ա��ȡ��Ϣ")
		  .setNegativeButton("��������",new DialogInterface.OnClickListener(){
		  	public void onClick(DialogInterface dialog,
		  	 int which){
		  		cxt.startActivity(new Intent(
		  		  Settings.ACTION_WIRELESS_SETTINGS));
		  	}
		  })
		  .setPositiveButton("����",null)
		  .create()
		  .show();
		//Looper.loop();
	}
}