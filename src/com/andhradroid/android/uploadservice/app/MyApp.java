package com.andhradroid.android.uploadservice.app;

import java.util.List;

import org.xutils.x;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.andhradroid.android.uploadservice.model.PhotoUploadController;

public class MyApp extends Application {

	private PhotoUploadController mPhotoController;

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化XUtils3
		x.Ext.init(this);
		// 设置是否输出debug
		x.Ext.setDebug(true);
		mPhotoController = new PhotoUploadController(this);
		startService(createExplicitFromImplicitIntent(this, new Intent(
				"INTENT_SERVICE_UPLOAD_ALL")));
	}

	public static MyApp getApplication(Context context) {
		return (MyApp) context.getApplicationContext();
	}

	public PhotoUploadController getPhotoUploadController() {
		return mPhotoController;
	}

	public static Intent createExplicitFromImplicitIntent(Context context,
			Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
				0);

		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}

		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);

		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);

		// Set the component to be explicit
		explicitIntent.setComponent(component);

		return explicitIntent;
	}
}
