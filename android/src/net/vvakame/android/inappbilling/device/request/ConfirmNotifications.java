package net.vvakame.android.inappbilling.device.request;

import net.vvakame.android.inappbilling.device.BillingService;
import net.vvakame.android.inappbilling.share.Consts;
import android.os.Bundle;
import android.os.RemoteException;

/**
 * Wrapper class that confirms a list of notifications to the server.
 */
public class ConfirmNotifications extends BillingRequest {
	final String[] mNotifyIds;

	public ConfirmNotifications(BillingService billingService, int startId,
			String[] notifyIds) {
		super(billingService, startId);
		mNotifyIds = notifyIds;
	}

	@Override
	protected long run() throws RemoteException {
		Bundle request = makeRequestBundle("CONFIRM_NOTIFICATIONS");
		request.putStringArray(Consts.BILLING_REQUEST_NOTIFY_IDS, mNotifyIds);
		Bundle response = BillingService.mService.sendBillingRequest(request);
		logResponseCode("confirmNotifications", response);
		return response.getLong(Consts.BILLING_RESPONSE_REQUEST_ID,
				Consts.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}
}