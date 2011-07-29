package net.vvakame.android.inappbilling.device;

import net.vvakame.android.inappbilling.device.request.RequestPurchase;
import net.vvakame.android.inappbilling.device.request.RestoreTransactions;
import net.vvakame.android.inappbilling.device.signature.VerifyBase;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Consts.PurchaseState;
import net.vvakame.android.inappbilling.share.Consts.ResponseCode;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements ITag {

	final MainActivity self = this;

	private MyPurchaseObserver mPurchaseObserver;
	private Handler mHandler;

	private BillingService mBillingService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i(TAG, "start sample application!");

		VerifyBase.setContext(this);

		mHandler = new Handler();
		mPurchaseObserver = new MyPurchaseObserver(mHandler);

		ResponseHandler.register(mPurchaseObserver);

		Button button = (Button) findViewById(R.id.doit);
		button.setEnabled(false);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				String itemId;
				itemId = "net.vvakame.inappbilling.item1";
				itemId = "android.test.purchased";

				String payload;
				payload = "hoge";

				if (!mBillingService.requestPurchase(itemId, payload)) {
					Log.w(TAG, "request purchase is failed...");
				}
			}
		});

		mBillingService = new BillingService();
		mBillingService.setContext(this);
		mBillingService.checkBillingSupported();
	}

	/**
	 * A {@link PurchaseObserver} is used to get callbacks when Android Market
	 * sends messages to this application so that we can update the UI.
	 */
	private class MyPurchaseObserver extends PurchaseObserver {
		public MyPurchaseObserver(Handler handler) {
			super(MainActivity.this, handler);
		}

		@Override
		public void onBillingSupported(boolean supported) {
			if (Consts.DEBUG) {
				Log.i(TAG, "supported: " + supported);
				findViewById(R.id.doit).setEnabled(true);
			}
		}

		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState,
				String itemId, long purchaseTime, String developerPayload) {
			if (Consts.DEBUG) {
				Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " "
						+ purchaseState);
			}

			if (purchaseState == PurchaseState.PURCHASED) {
				Log.i(TAG, "charin, charin.");
				Toast.makeText(self, "ちゃりんちゃりん！", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestPurchaseResponse(RequestPurchase request,
				ResponseCode responseCode) {
			if (Consts.DEBUG) {
				Log.d(TAG, request.mProductId + ": " + responseCode);
			}
			if (responseCode == ResponseCode.RESULT_OK) {
				if (Consts.DEBUG) {
					Log.i(TAG, "purchase was successfully sent to server");
				}
			} else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
				if (Consts.DEBUG) {
					Log.i(TAG, "user canceled purchase");
				}
			} else {
				if (Consts.DEBUG) {
					Log.i(TAG, "purchase failed");
				}
			}
		}

		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request,
				ResponseCode responseCode) {
			if (responseCode == ResponseCode.RESULT_OK) {
				if (Consts.DEBUG) {
					Log.d(TAG, "completed RestoreTransactions request");
				}
			} else {
				if (Consts.DEBUG) {
					Log.d(TAG, "RestoreTransactions error: " + responseCode);
				}
			}
		}
	}
}