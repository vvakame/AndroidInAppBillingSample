package net.vvakame.android.inappbilling.device.request;

import net.vvakame.android.inappbilling.device.BillingService;
import net.vvakame.android.inappbilling.device.signature.VerifyBase;
import net.vvakame.android.inappbilling.share.Consts;
import android.os.Bundle;
import android.os.RemoteException;

/**
 * Wrapper class that sends a GET_PURCHASE_INFORMATION message to the server.
 */
public class GetPurchaseInformation extends BillingRequest {

	long mNonce;
	final String[] mNotifyIds;

	public GetPurchaseInformation(BillingService billingService, int startId,
			String[] notifyIds) {
		super(billingService, startId);
		mNotifyIds = notifyIds;
	}

	@Override
	protected long run() throws RemoteException {
		mNonce = VerifyBase.generateNonce();

		Bundle request = makeRequestBundle("GET_PURCHASE_INFORMATION");
		request.putLong(Consts.BILLING_REQUEST_NONCE, mNonce);
		request.putStringArray(Consts.BILLING_REQUEST_NOTIFY_IDS, mNotifyIds);
		Bundle response = BillingService.mService.sendBillingRequest(request);
		logResponseCode("getPurchaseInformation", response);
		return response.getLong(Consts.BILLING_RESPONSE_REQUEST_ID,
				Consts.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}

	@Override
	protected void onRemoteException(RemoteException e) {
		super.onRemoteException(e);
		VerifyBase.removeNonce(mNonce);
	}
}