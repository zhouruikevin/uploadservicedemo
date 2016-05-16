package com.andhradroid.android.uploadservice.model;

import org.xutils.db.annotation.Column;

import com.andhradroid.android.uploadservice.events.UploadStateChangedEvent;
import com.andhradroid.android.uploadservice.events.UploadsModifiedEvent;

import de.greenrobot.event.EventBus;

public class PhotoUpload {

	//
	public static final int STATE_UPLOAD_COMPLETED = 5;
	public static final int STATE_UPLOAD_ERROR = 4;
	public static final int STATE_UPLOAD_IN_PROGRESS = 3;
	public static final int STATE_UPLOAD_WAITING = 2;
	public static final int STATE_SELECTED = 1;
	public static final int STATE_NONE = 0;
	private int mState;
	private String name;
	@Column(name = "id", isId = true, autoGen = true)
	public int id;
	@Column(name = "vehicleId")
	public String vehicleId;// bean对象的vehicleId 车辆ID
	@Column(name = "turId")
	public String turId;// bean对象的orderId 订单ID
	@Column(name = "path")
	public String path;// 图片的本地路径
	@Column(name = "url")
	public String url;// 图片上传成功后的Url
	@Column(name = "state")
	public int state = 0;// 是否已完成上传 int类型预留状态给没上传和已上传意外的情况

	public int getUploadState() {
		return mState;
	}

	public void reset() {
		mState = STATE_NONE;
	}

	public void setUploadState(final int state) {
		if (mState != state) {
			mState = state;

			switch (state) {
			case STATE_UPLOAD_ERROR:
			case STATE_UPLOAD_COMPLETED:
				EventBus.getDefault().post(new UploadsModifiedEvent());
				break;
			case STATE_SELECTED:
			case STATE_UPLOAD_WAITING:
				break;
			}
			notifyUploadStateListener();
		}
	}

	private void notifyUploadStateListener() {
		EventBus.getDefault().post(new UploadStateChangedEvent(this));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
