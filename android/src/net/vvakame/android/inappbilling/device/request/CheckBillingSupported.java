package net.vvakame.android.inappbilling.device.request;

import net.vvakame.android.inappbilling.device.BillingService;
import net.vvakame.android.inappbilling.device.ResponseHandler;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * Wrapper class that checks if in-app billing is supported.
 */
public class CheckBillingSupported extends BillingRequest {

	public CheckBillingSupported(BillingService billingService) {
		super(billingService, -1);
	}

	@Override
	protected long run() throws RemoteException {
		Bundle request = makeRequestBundle("CHECK_BILLING_SUPPORTED");
		Bundle response = BillingService.mService.sendBillingRequest(request);
		int responseCode = response
				.getInt(Consts.BILLING_RESPONSE_RESPONSE_CODE);
		if (Consts.DEBUG) {
			Log.i(TAG,
					"CheckBillingSupported response code: "
							+ ResponseCode.valueOf(responseCode));
		}
		boolean billingSupported = (responseCode == ResponseCode.RESULT_OK
				.ordinal());
		ResponseHandler.checkBillingSupportedResponse(billingSupported);
		return Consts.BILLING_RESPONSE_INVALID_REQUEST_ID;
	}
}