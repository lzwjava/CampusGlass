package lzw.campus.glass;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class CampusGlassApp extends Application {
    private static final String LOGTAG = CampusGlassApp.class.getName();
    @Override
    public void onCreate() {
        super.onCreate(); 
        AVOSCloud.useAVCloudCN();
        AVOSCloud.initialize(this, "cf7jivx0e2iqa9iieqowbiqzparou16k8moz03t3opgvitox",
    	    "ow95gi3zpx8nb34rphwggu6m0dlgay1wknedwskm0v20f207");
    }
}
