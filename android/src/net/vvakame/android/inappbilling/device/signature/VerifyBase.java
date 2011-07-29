package net.vvakame.android.inappbilling.device.signature;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.vvakame.android.inappbilling.share.Order;

import android.content.Context;

public abstract class VerifyBase {

	static final String KEY_FACTORY_ALGORITHM = "RSA";
	static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	static final SecureRandom RANDOM = new SecureRandom();

	private static Set<Long> sKnownNonces = new HashSet<Long>();

	static Context sContext;

	public static void setContext(Context con) {
		sContext = con.getApplicationContext();
	}

	public static long generateNonce() {
		long nonce = RANDOM.nextLong();
		sKnownNonces.add(nonce);
		return nonce;
	}

	public static void removeNonce(long nonce) {
		sKnownNonces.remove(nonce);
	}

	public static boolean isNonceKnown(long nonce) {
		return sKnownNonces.contains(nonce);
	}

	public abstract List<Order> verifyPurchase(String signedData,
			String signature);

	public static VerifyBase getInstance() {
		// return VerifyAtDevice.getInstance();
		return VerifyAtServer.getInstance();
	}
}
