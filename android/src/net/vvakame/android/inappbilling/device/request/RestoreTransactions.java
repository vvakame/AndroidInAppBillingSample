package net.vvakame.android.inappbilling.device.request;

import net.vvakame.android.inappbilling.device.BillingService;
import net.vvakame.android.inappbilling.device.ResponseHandler;
import net.vvakame.android.inappbilling.device.signature.VerifyBase;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.os.Bundle;
import android.os.RemoteException;

/**
 * Wrapper class that sends a RESTORE_TRANSACTIONS message to the server.
 */
public class RestoreTransactions extends BillingRequest {

	long mNonce;

	public RestoreTransactions(BillingService billingService) {
		super(billingService, -1);
	}

	@Override
	protected long run() throws RemoteException {
		mNonce = VerifyBase.generateNonce();

		Bundle request = makeRequestBundle("RESTORE_TRANSACTIONS");
		request.putLong(Consts.BILLING_REQUEST_NONCE, mNonce);
		Bundle response = BillingService.mService.sendBillingRequest(request);
		logResponseCode("restoreTransactions", response);
		return response.getLong(Consts.BILLING_RESPONSE_REQUEST_ID,
				Consts.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}

	@Override
	protected void onRemoteException(RemoteException e) {
		super.onRemoteException(e);
		VerifyBase.removeNonce(mNonce);
	}

	@Override
	public void responseCodeReceived(ResponseCode responseCode) {
		ResponseHandler.responseCodeReceived(this.mService, this,
				responseCode);
	}
}