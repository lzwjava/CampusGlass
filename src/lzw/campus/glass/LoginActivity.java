package lzw.campus.glass;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

public class LoginActivity extends Activity {
	EditText userName,password;
	boolean isDebug=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setActionBarDrawable(R.drawable.bar); 
		
		AVAnalytics.trackAppOpened(getIntent());
    
		userName=(EditText)findViewById(R.id.userName);
		password=(EditText)findViewById(R.id.password);
	}
	
	void setActionBarDrawable(int id) {
		ActionBar actionBar=getActionBar();
		Drawable drawable=this.getResources().getDrawable(id);
	  actionBar.setBackgroundDrawable(drawable);
	}
	
	void toast(String txt){
		Toast.makeText(LoginActivity.this,txt,
				Toast.LENGTH_SHORT).show();
	}
	
	void intentToClass(Context ctx, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(ctx, cls);
		startActivity(intent);
	}
	
	boolean isEmpty(String str,String prompt){
		if(str.isEmpty()){
			toast(prompt);
			return true;
		}
		return false;
	}
	
	public void login(View v){
		final String userNameStr=userName.getText().toString();
		if(isEmpty(userNameStr,"请输入用户名")){
			return;
		}
		final String passwordStr = password.getText().toString();
		if(isEmpty(passwordStr,"请输入密码")){
			return;
		}
		AVUser.logInInBackground(userNameStr, passwordStr, new LogInCallback() {
			public void done(AVUser user, AVException e) {
				if (user != null) {
					// 登录成功
					intentToClass(LoginActivity.this, GlassRoomActivity.class);
				} else {
					// 登录失败
					toast("未联网或者用户名账号错误");
				}
			}	
		});
	}
	
	public void register(View v){
		final String userNameStr=userName.getText().toString();
		if(userNameStr.isEmpty()){
			toast("请输入账号");
			return;
		}
		
		final String passwordStr=password.getText().toString();
		if(passwordStr.isEmpty()){
			toast("请输入密码");
			return;
		}

		AVUser user = new AVUser();
		user.setUsername(userNameStr);
		user.setPassword(passwordStr);
		user.put("installationId", AVInstallation.getCurrentInstallation().
				getInstallationId());
		// 其他属性可以像其他AVObject对象一样使用put方法添加
		user.signUpInBackground(new SignUpCallback() {
			public void done(AVException e) {
				if (e == null) {
					// successfully
					AlertDialog.Builder builder = new 
						AlertDialog.Builder(LoginActivity.this);
					builder.setTitle("提示").setMessage("注册成功")
							.setPositiveButton("确定", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									intentToClass(getApplicationContext(), GlassRoomActivity.class);
								}
							}).create().show();
				} else {
					// failed
					toast("用户名已经被注册了");
				}
			}
		});
	}
	
	public void login1(View v){
		final String userNameStr=userName.getText().toString();
		if(userNameStr.isEmpty()){
			toast("请输入账号");
			return;
		}
		
		final String passwordStr=password.getText().toString();
		if(passwordStr.isEmpty()){
			toast("请输入密码");
			return;
		}
		
		if(NetworkUtil.isConnect(LoginActivity.this)){
			new AsyncTask<Void,Void,Integer>(){
				protected Integer doInBackground(Void... params){
					int result=Login.checkLogin(userNameStr, passwordStr);
					return result;
				}
				protected void onPreExecute(){
				}
				protected void onPostExecute(Integer result){
					if(result==Constant.notConnectSchool){
						toast("没有连接校网");
					}else if(result==Constant.notConnectInet){
						toast("网络信号不好");
					}else if(result==Constant.loginSuccess){
						intentToClass(getApplicationContext(), GlassRoomActivity.class);
					}else if(result==Constant.wrongPassword){
						toast("用户名或密码错误");
					}else if(result==Constant.InetException){
						toast("网络异常");
					}
				}
			}.execute();
			//isLogin=Login.checkLogin(userNameStr, passwordStr);
		}else{
			toast("没有连接3G网络或Wifi");
		}
	}
}
