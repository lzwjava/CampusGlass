package lzw.campus.glass;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Util{
	static void print(String format, Object... args) {
		System.out.println(String.format(format, args));
	}
	
	static void log(String format, Object... args) {
		Log.i("lzw",String.format(format, args));
	}
	
	static void intentToClass(Context ctx, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(ctx, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}
}
