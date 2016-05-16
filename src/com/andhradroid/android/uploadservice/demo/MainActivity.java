package com.andhradroid.android.uploadservice.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.andhradroid.android.uploadservice.R;
import com.andhradroid.android.uploadservice.app.MyApp;
import com.andhradroid.android.uploadservice.model.PhotoUpload;
import com.andhradroid.android.uploadservice.model.PhotoUploadController;
import com.andhradroid.android.uploadservice.service.UploadImgService;

public class MainActivity extends Activity {
	UploadImgService uploadService = new UploadImgService();
	private String[] paths = {
			"/storage_int/0/Pictures/JPEG_20160516_180508_330271806.jpg",
			"/storage_int/0/Pictures/JPEG_20160516_180518_830100306.jpg",
			"/storage_int/0/Pictures/JPEG_20160516_180528_-1019184101.jpg",
			"/storage_int/0/Pictures/JPEG_20160516_180536_1144503901.jpg" };

	private Button btnUpload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnUpload = (Button) findViewById(R.id.BtnUpload);
		btnUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PhotoUpload selection = new PhotoUpload();
				selection.setName("任务上传中:");
				selection.path = paths[1];
				uploadService.upload(selection);
			}
		});
		MyApp app = (MyApp) getApplication();
		PhotoUploadController con = app.getPhotoUploadController();

		for (int i = 0; i < 4; i++) {
			PhotoUpload selection = new PhotoUpload();
			selection.setName("任务上传中:" + (i + 1) + "/" + 4);
			selection.path = paths[i];
			// uploadService.upload(selection);
			con.addUpload(selection);
		}//

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean isPaused = prefs.getBoolean("isPaused", false);
		menu.findItem(R.id.action_settings).setTitle(
				isPaused ? "Resume" : "Pause");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			boolean isPaused = prefs.getBoolean("isPaused", false);
			prefs.edit().putBoolean("isPaused", !isPaused).commit();
			if (isPaused) {
				startService(MyApp.createExplicitFromImplicitIntent(this,
						new Intent("INTENT_SERVICE_UPLOAD_ALL")));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
