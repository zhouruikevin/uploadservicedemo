package com.andhradroid.android.uploadservice.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.kymjs.kjframe.utils.L;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import com.andhradroid.android.uploadservice.events.UploadingPausedStateChangedEvent;
import com.andhradroid.android.uploadservice.model.PhotoUpload;

import de.greenrobot.event.EventBus;

public class UploadImgService {
	public static final String TAG = UploadImgService.class.getName();
	private EventBus bus = EventBus.getDefault();
	private RequestParams params;
	private static String token = "abcdefg";
	private static String userId = "4003";
	private static String sign = getMD5Str(userId + token, 0, 32);
	public final static String API_SERVER_FILE = "http://testfileservice.4sonline.net:88/";// 图片服务器测试路径
	public final static String UPLOAD = API_SERVER_FILE
			+ "fileService/upload.action";

	private void setRequestParams(PhotoUpload upload) {
		if (params == null) {
			params = new RequestParams(UPLOAD);
			params.addBodyParameter("sign", sign);
			params.addBodyParameter("sessionToken", "");
			params.addBodyParameter("fid", "t_pic");
			params.addBodyParameter("uid", "4003");
		} else {
			params.removeParameter("f");
		}
		try {
			params.addBodyParameter("f", new FileInputStream(upload.path),
					"image/jpeg", "1.jpg");
		} catch (FileNotFoundException e) {
			L.d("NOT FIND");
			e.printStackTrace();
		}
	}

	public void upload(final PhotoUpload upload) {
		setRequestParams(upload);
		upload.setUploadState(PhotoUpload.STATE_UPLOAD_IN_PROGRESS);
		// 先保存状态
		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String result) {
				upload.setUploadState(PhotoUpload.STATE_UPLOAD_COMPLETED);
				L.d(result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				L.d(ex.toString());
				upload.setUploadState(PhotoUpload.STATE_UPLOAD_WAITING);
			}

			@Override
			public void onCancelled(CancelledException cex) {
				// upload.setUploadState(PhotoUpload.STATE_UPLOAD_ERROR);
				upload.setUploadState(PhotoUpload.STATE_UPLOAD_WAITING);
			}

			@Override
			public void onFinished() {
				bus.post(new UploadingPausedStateChangedEvent());
			}

		});
	}

	public static String getMD5Str(String str, int offset, int len) {
		MessageDigest messageDigest = null;
		if (str == null)
			return "";

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] bytes = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
		}

		return md5StrBuff.substring(offset, len).toString();
	}
}
