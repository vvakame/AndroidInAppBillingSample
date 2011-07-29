// Copyright 2010 Google Inc. All Rights Reserved.

package net.vvakame.android.inappbilling.device;

import net.vvakame.android.inappbilling.device.request.RequestPurchase;
import net.vvakame.android.inappbilling.device.request.RestoreTransactions;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Consts.PurchaseState;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This class contains the methods that handle responses from Android Market.
 * The implementation of these methods is specific to a particular application.
 * The methods in this example update the database and, if the main application
 * has registered a {@llink PurchaseObserver}, will also update the UI. An
 * application might also want to forward some responses on to its own server,
 * and that could be done here (in a background thread) but this example does
 * not do that.
 * 
 * You should modify and obfuscate this code before using it.
 */
public class ResponseHandler implements ITag {

	private static PurchaseObserver sPurchaseObserver;

	public static synchronized void register(PurchaseObserver observer) {
		sPurchaseObserver = observer;
	}

	public static synchronized void unregister(PurchaseObserver observer) {
		sPurchaseObserver = null;
	}

	public static void checkBillingSupportedResponse(boolean supported) {
		if (sPurchaseObserver != null) {
			sPurchaseObserver.onBillingSupported(supported);
		}
	}

	public static void buyPageIntentResponse(PendingIntent pendingIntent,
			Intent intent) {
		if (sPurchaseObserver == null) {
			if (Consts.DEBUG) {
				Log.d(TAG, "UI is not running");
			}
			return;
		}
		sPurchaseObserver.startBuyPageActivity(pendingIntent, intent);
	}

	public static void purchaseResponse(final Context context,
			final PurchaseState purchaseState, final String productId,
			final String orderId, final long purchaseTime,
			final String developerPayload) {

		new Thread(new Runnable() {
			public void run() {

				synchronized (ResponseHandler.class) {
					if (sPurchaseObserver != null) {
						sPurchaseObserver.postPurchaseStateChange(
								purchaseState, productId, purchaseTime,
								developerPayload);
					}
				}
			}
		}).start();
	}

	public static void responseCodeReceived(Context context,
			RequestPurchase request, ResponseCode responseCode) {
		if (sPurchaseObserver != null) {
			sPurchaseObserver.onRequestPurchaseResponse(request, responseCode);
		}
	}

	public static void responseCodeReceived(Context context,
			RestoreTransactions request, ResponseCode responseCode) {
		if (sPurchaseObserver != null) {
			sPurchaseObserver.onRestoreTransactionsResponse(request,
					responseCode);
		}
	}
}
