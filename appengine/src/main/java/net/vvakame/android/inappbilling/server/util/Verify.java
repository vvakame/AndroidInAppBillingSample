package net.vvakame.android.inappbilling.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;
import java.util.logging.Logger;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;

/**
 * データと署名を検証する.
 * @author vvakame
 */
public class Verify {

	static final Logger logger = Logger.getLogger(Verify.class.getName());

	private static final String KEY_FACTORY_ALGORITHM = "RSA";

	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

	static final Properties PROPERTIES;

	static {
		try {
			PROPERTIES = new Properties();
			InputStream is = Verify.class.getResourceAsStream("/publickey.properties");
			PROPERTIES.load(is);
		} catch (IOException e) {
			throw new RuntimeException("/makesynccall.properties required", e);
		}
	}


	/**
	 * シグネチャをチェックする.
	 * @param signedData
	 * @param signature
	 * @return 有効なデータか否か
	 * @author vvakame
	 */
	public static boolean checkSignature(String signedData, String signature) {
		logger.finest("signature: " + signature);

		PublicKey publicKey = generatePublicKey();

		Signature sig;
		try {
			sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (!sig.verify(Base64.decode(signature))) {
				logger.warning("Signature verification failed.");
				return false;
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			logger.warning("NoSuchAlgorithmException.");
		} catch (InvalidKeyException e) {
			logger.warning("Invalid key specification.");
		} catch (SignatureException e) {
			logger.warning("Signature exception.");
		} catch (Base64DecoderException e) {
			logger.warning("Base64 decoding failed.");
		}
		return false;
	}

	static PublicKey generatePublicKey() {
		String encodedPublicKey = PROPERTIES.getProperty("publickey");
		try {
			byte[] decodedKey = Base64.decode(encodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			logger.warning("Invalid key specification.");
			throw new IllegalArgumentException(e);
		} catch (Base64DecoderException e) {
			logger.warning("Base64 decoding failed.");
			throw new IllegalArgumentException(e);
		}
	}
}
