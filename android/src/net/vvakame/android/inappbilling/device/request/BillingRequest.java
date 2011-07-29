package net.vvakame.android.inappbilling.device.request;

import net.vvakame.android.inappbilling.device.BillingService;
import net.vvakame.android.inappbilling.device.ITag;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public abstract class BillingRequest implements ITag {

	final BillingService mService;
	private final int mStartId;
	protected long mRequestId;

	public BillingRequest(BillingService billingService, int startId) {
		this.mService = billingService;
		mStartId = startId;
	}

	public int getStartId() {
		return mStartId;
	}

	public boolean runRequest() {
		if (runIfConnected()) {
			return true;
		}

		if (mService.bindToMarketBillingService()) {
			// Add a pending request to run when the service is connected.
			BillingService.mPendingRequests.add(this);
			return true;
		}
		return false;
	}

	public boolean runIfConnected() {
		if (Consts.DEBUG) {
			Log.d(TAG, getClass().getSimpleName());
		}
		if (BillingService.mService != null) {
			try {
				mRequestId = run();
				if (Consts.DEBUG) {
					Log.d(TAG, "request id: " + mRequestId);
				}
				if (mRequestId >= 0) {
					BillingService.mSentRequests.put(mRequestId, this);
				}
				return true;
			} catch (RemoteException e) {
				onRemoteException(e);
			}
		}
		return false;
	}

	protected void onRemoteException(RemoteException e) {
		Log.w(TAG, "remote billing service crashed");
		BillingService.mService = null;
	}

	abstract protected long run() throws RemoteException;

	public void responseCodeReceived(ResponseCode responseCode) {
	}

	protected Bundle makeRequestBundle(String method) {
		Bundle request = new Bundle();
		request.putString(Consts.BILLING_REQUEST_METHOD, method);
		request.putInt(Consts.BILLING_REQUEST_API_VERSION, 1);
		request.putString(Consts.BILLING_REQUEST_PACKAGE_NAME,
				mService.getPackageName());
		return request;
	}

	protected void logResponseCode(String method, Bundle response) {
		ResponseCode responseCode = ResponseCode.valueOf(response
				.getInt(Consts.BILLING_RESPONSE_RESPONSE_CODE));
		if (Consts.DEBUG) {
			Log.e(TAG, method + " received " + responseCode.toString());
		}
	}
}