// Copyright 2010 Google Inc. All Rights Reserved.

package net.vvakame.android.inappbilling.device.signature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.vvakame.android.inappbilling.device.ITag;
import net.vvakame.android.inappbilling.device.R;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Order;
import net.vvakame.android.inappbilling.share.Purchase;
import net.vvakame.android.inappbilling.share.PurchaseGen;
import net.vvakame.util.jsonpullparser.JsonFormatException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;
import android.util.Log;

public class VerifyAtServer extends VerifyBase implements ITag {

	public static VerifyBase getInstance() {
		return new VerifyAtServer();
	}

	public List<Order> verifyPurchase(String signedData, String signature) {
		if (signedData == null) {
			Log.e(TAG, "data is null");
			return null;
		}
		if (Consts.DEBUG) {
			Log.i(TAG, "signedData: " + signedData);
		}
		boolean verified = false;
		if (!TextUtils.isEmpty(signature)) {
			String base64EncodedPublicKey = sContext
					.getString(R.string.base64_encoded_publickey);

			Log.d(TAG, base64EncodedPublicKey);
			Log.d(TAG, signedData);
			Log.d(TAG, signature);

			verified = verify(signedData, signature);
			if (!verified) {
				Log.w(TAG, "BAD, signature does not match data.");
				return null;
			} else {
				Log.d(TAG, "OK, signature does match data.");
			}
		}

		Purchase purchase;
		try {
			purchase = PurchaseGen.get(signedData);
		} catch (IOException e) {
			Log.e(TAG, "raise exception.", e);
			return null;
		} catch (JsonFormatException e) {
			Log.e(TAG, "raise exception.", e);
			return null;
		}

		long nonce = purchase.getNonce();

		if (!VerifyAtServer.isNonceKnown(nonce)) {
			Log.w(TAG, "Nonce not found: " + nonce);
			return null;
		}

		removeNonce(nonce);
		return purchase.getOrders();
	}

	static boolean verify(String signedData, String signature) {

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("data", signedData);
			params.put("signature", signature);

			int status = post("http://inappbilling.vvakame.appspot.com/",
					params);
			if (status == 200) {
				return true;
			} else {
				Log.w(TAG, "http request failed... status=" + status);
			}

		} catch (IOException e) {
			Log.e(TAG, "raise exception.", e);
			return false;
		}

		return false;
	}

	static int post(String url, Map<String, String> params) throws IOException {

		Log.i(TAG, "start connect to " + url + " , params=" + params);

		if (params == null) {
			params = new HashMap<String, String>();
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			pairs.add(new BasicNameValuePair(key, params.get(key)));
		}
		post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
		HttpResponse response = client.execute(post);

		Log.i(TAG, "http status, " + response.getStatusLine().getReasonPhrase());

		return response.getStatusLine().getStatusCode();
	}
}
