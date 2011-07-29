// Copyright 2010 Google Inc. All Rights Reserved.

package net.vvakame.android.inappbilling.device.signature;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import net.vvakame.android.inappbilling.device.ITag;
import net.vvakame.android.inappbilling.device.R;
import net.vvakame.android.inappbilling.device.util.Base64;
import net.vvakame.android.inappbilling.device.util.Base64DecoderException;
import net.vvakame.android.inappbilling.share.Consts;
import net.vvakame.android.inappbilling.share.Order;
import net.vvakame.android.inappbilling.share.Purchase;
import net.vvakame.android.inappbilling.share.PurchaseGen;
import net.vvakame.util.jsonpullparser.JsonFormatException;
import android.text.TextUtils;
import android.util.Log;

public class VerifyAtDevice extends VerifyBase implements ITag {

	public static VerifyBase getInstance() {
		return new VerifyAtDevice();
	}

	public List<Order> verifyPurchase(String signedData, String signature) {
		if (signedData == null) {
			Log.e(TAG, "data is null");
			return null;
		}
		if (Consts.DEBUG) {
			Log.i(TAG, "signedData: " + signedData);
		}
		boolean verified = false;
		if (!TextUtils.isEmpty(signature)) {
			String base64EncodedPublicKey = sContext
					.getString(R.string.base64_encoded_publickey);

			Log.d(TAG, base64EncodedPublicKey);
			Log.d(TAG, signedData);
			Log.d(TAG, signature);

			PublicKey key = VerifyAtDevice
					.generatePublicKey(base64EncodedPublicKey);
			verified = VerifyAtDevice.verify(key, signedData, signature);
			if (!verified) {
				Log.w(TAG, "BAD, signature does not match data.");
				return null;
			} else {
				Log.d(TAG, "OK, signature does match data.");
			}
		}

		Purchase purchase;
		try {
			purchase = PurchaseGen.get(signedData);
		} catch (IOException e) {
			Log.e(TAG, "raise exception.", e);
			return null;
		} catch (JsonFormatException e) {
			Log.e(TAG, "raise exception.", e);
			return null;
		}

		long nonce = purchase.getNonce();

		if (!VerifyAtDevice.isNonceKnown(nonce)) {
			Log.w(TAG, "Nonce not found: " + nonce);
			return null;
		}

		removeNonce(nonce);
		return purchase.getOrders();
	}

	static PublicKey generatePublicKey(String encodedPublicKey) {
		try {
			byte[] decodedKey = Base64.decode(encodedPublicKey);
			KeyFactory keyFactory = KeyFactory
					.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory
					.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			Log.e(TAG, "Invalid key specification.");
			throw new IllegalArgumentException(e);
		} catch (Base64DecoderException e) {
			Log.e(TAG, "Base64 decoding failed.");
			throw new IllegalArgumentException(e);
		}
	}

	static boolean verify(PublicKey publicKey, String signedData,
			String signature) {
		if (Consts.DEBUG) {
			Log.i(TAG, "signature: " + signature);
		}
		Signature sig;
		try {
			sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (!sig.verify(Base64.decode(signature))) {
				Log.e(TAG, "Signature verification failed.");
				return false;
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "NoSuchAlgorithmException.");
		} catch (InvalidKeyException e) {
			Log.e(TAG, "Invalid key specification.");
		} catch (SignatureException e) {
			Log.e(TAG, "Signature exception.");
		} catch (Base64DecoderException e) {
			Log.e(TAG, "Base64 decoding failed.");
		}
		return false;
	}
}
