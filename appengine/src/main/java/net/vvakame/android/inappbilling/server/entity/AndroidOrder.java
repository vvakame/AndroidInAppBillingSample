package net.vvakame.android.inappbilling.server.entity;

import net.vvakame.android.inappbilling.share.Consts.PurchaseState;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * AndroidのInApp Billingの売買情報.<br>
 * Keyの内容.<br>
 * name=orderId
 * @author vvakame
 */
@Model
public class AndroidOrder {

	@Attribute(primaryKey = true)
	Key key;

	String notificationId;

	String orderId;

	String packageName;

	String productId;

	long purchaseTime;

	PurchaseState purchaseState;


	/**
	 * @return the key
	 * @category accessor
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 * @category accessor
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the notificationId
	 * @category accessor
	 */
	public String getNotificationId() {
		return notificationId;
	}

	/**
	 * @param notificationId the notificationId to set
	 * @category accessor
	 */
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the orderId
	 * @category accessor
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 * @category accessor
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the packageName
	 * @category accessor
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 * @category accessor
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the productId
	 * @category accessor
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 * @category accessor
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the purchaseTime
	 * @category accessor
	 */
	public long getPurchaseTime() {
		return purchaseTime;
	}

	/**
	 * @param purchaseTime the purchaseTime to set
	 * @category accessor
	 */
	public void setPurchaseTime(long purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	/**
	 * @return the purchaseState
	 * @category accessor
	 */
	public PurchaseState getPurchaseState() {
		return purchaseState;
	}

	/**
	 * @param purchaseState the purchaseState to set
	 * @category accessor
	 */
	public void setPurchaseState(PurchaseState purchaseState) {
		this.purchaseState = purchaseState;
	}
}
