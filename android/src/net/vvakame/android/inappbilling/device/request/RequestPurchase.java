package net.vvakame.android.inappbilling.device.request;

import net.vvakame.android.inappbilling.device.BillingService;
import net.vvakame.android.inappbilling.device.ResponseHandler;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * Wrapper class that requests a purchase.
 */
public class RequestPurchase extends BillingRequest {

	public final String mProductId;
	public final String mDeveloperPayload;

	public RequestPurchase(BillingService billingService, String itemId) {
		this(billingService, itemId, null);
	}

	public RequestPurchase(BillingService billingService, String itemId,
			String developerPayload) {
		super(billingService, -1);
		mProductId = itemId;
		mDeveloperPayload = developerPayload;
	}

	@Override
	protected long run() throws RemoteException {
		Bundle request = makeRequestBundle("REQUEST_PURCHASE");
		request.putString(Consts.BILLING_REQUEST_ITEM_ID, mProductId);
		// Note that the developer payload is optional.
		if (mDeveloperPayload != null) {
			request.putString(Consts.BILLING_REQUEST_DEVELOPER_PAYLOAD,
					mDeveloperPayload);
		}
		Bundle response = BillingService.mService.sendBillingRequest(request);
		PendingIntent pendingIntent = response
				.getParcelable(Consts.BILLING_RESPONSE_PURCHASE_INTENT);
		if (pendingIntent == null) {
			Log.e(BillingService.TAG, "Error with requestPurchase");
			return Consts.BILLING_RESPONSE_INVALID_REQUEST_ID;
		}

		Intent intent = new Intent();
		ResponseHandler.buyPageIntentResponse(pendingIntent, intent);
		return response.getLong(Consts.BILLING_RESPONSE_REQUEST_ID,
				Consts.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}

	@Override
	public void responseCodeReceived(ResponseCode responseCode) {
		ResponseHandler.responseCodeReceived(this.mService, this,
				responseCode);
	}
}