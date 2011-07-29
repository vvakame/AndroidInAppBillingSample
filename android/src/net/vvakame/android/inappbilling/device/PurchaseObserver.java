// Copyright 2010 Google Inc. All Rights Reserved.

package net.vvakame.android.inappbilling.device;

import java.lang.reflect.Method;

import net.vvakame.android.inappbilling.device.request.RequestPurchase;
import net.vvakame.android.inappbilling.device.request.RestoreTransactions;
import net.vvakame.android.inappbilling.share.Consts.PurchaseState;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Handler;
import android.util.Log;

/**
 * An interface for observing changes related to purchases. The main application
 * extends this class and registers an instance of that derived class with
 * {@link ResponseHandler}. The main application implements the callbacks
 * {@link #onBillingSupported(boolean)} and
 * {@link #onPurchaseStateChange(PurchaseState, String, int, long)}. These
 * methods are used to update the UI.
 */
public abstract class PurchaseObserver implements ITag {

	private final Activity mActivity;
	private final Handler mHandler;
	private Method mStartIntentSender;
	private Object[] mStartIntentSenderArgs = new Object[5];
	private static final Class<?>[] START_INTENT_SENDER_SIG = new Class[] {
			IntentSender.class, Intent.class, int.class, int.class, int.class };

	public PurchaseObserver(Activity activity, Handler handler) {
		mActivity = activity;
		mHandler = handler;
		initCompatibilityLayer();
	}

	public abstract void onBillingSupported(boolean supported);

	public abstract void onPurchaseStateChange(PurchaseState purchaseState,
			String itemId, long purchaseTime,
			String developerPayload);

	public abstract void onRequestPurchaseResponse(RequestPurchase request,
			ResponseCode responseCode);

	public abstract void onRestoreTransactionsResponse(
			RestoreTransactions request, ResponseCode responseCode);

	private void initCompatibilityLayer() {
		try {
			mStartIntentSender = mActivity.getClass().getMethod(
					"startIntentSender", START_INTENT_SENDER_SIG);
		} catch (SecurityException e) {
			mStartIntentSender = null;
		} catch (NoSuchMethodException e) {
			mStartIntentSender = null;
		}
	}

	void startBuyPageActivity(PendingIntent pendingIntent, Intent intent) {
		if (mStartIntentSender != null) {
			// This is on Android 2.0 and beyond. The in-app buy page activity
			// must be on the activity stack of the application.
			try {
				// This implements the method call:
				// mActivity.startIntentSender(pendingIntent.getIntentSender(),
				// intent, 0, 0, 0);
				mStartIntentSenderArgs[0] = pendingIntent.getIntentSender();
				mStartIntentSenderArgs[1] = intent;
				mStartIntentSenderArgs[2] = Integer.valueOf(0);
				mStartIntentSenderArgs[3] = Integer.valueOf(0);
				mStartIntentSenderArgs[4] = Integer.valueOf(0);
				mStartIntentSender.invoke(mActivity, mStartIntentSenderArgs);
			} catch (Exception e) {
				Log.e(TAG, "error starting activity", e);
			}
		} else {
			// This is on Android version 1.6. The in-app buy page activity must
			// be on its
			// own separate activity stack instead of on the activity stack of
			// the application.
			try {
				pendingIntent.send(mActivity, 0 /* code */, intent);
			} catch (CanceledException e) {
				Log.e(TAG, "error starting activity", e);
			}
		}
	}

	void postPurchaseStateChange(final PurchaseState purchaseState,
			final String itemId, final long purchaseTime, final String developerPayload) {
		mHandler.post(new Runnable() {
			public void run() {
				onPurchaseStateChange(purchaseState, itemId,
						purchaseTime, developerPayload);
			}
		});
	}
}
