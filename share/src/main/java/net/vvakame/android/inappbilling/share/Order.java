package net.vvakame.android.inappbilling.share;

import net.vvakame.android.inappbilling.share.Consts.PurchaseState;
import net.vvakame.util.jsonpullparser.annotation.JsonKey;
import net.vvakame.util.jsonpullparser.annotation.JsonModel;

@JsonModel(decamelize = false, treatUnknownKeyAsError = false, builder = true)
public class Order {

	@JsonKey
	String notificationId;

	@JsonKey
	String orderId = "";

	@JsonKey
	String packageName;

	@JsonKey
	String productId;

	@JsonKey
	long purchaseTime;

	@JsonKey(converter = PurchaseStateConverter.class)
	PurchaseState purchaseState;

	@JsonKey
	String developerPayload;

	/**
	 * @return the notificationId
	 */
	public String getNotificationId() {
		return notificationId;
	}

	/**
	 * @param notificationId
	 *            the notificationId to set
	 */
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the purchaseTime
	 */
	public long getPurchaseTime() {
		return purchaseTime;
	}

	/**
	 * @param purchaseTime
	 *            the purchaseTime to set
	 */
	public void setPurchaseTime(long purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	/**
	 * @return the purchaseState
	 */
	public PurchaseState getPurchaseState() {
		return purchaseState;
	}

	/**
	 * @param purchaseState
	 *            the purchaseState to set
	 */
	public void setPurchaseState(PurchaseState purchaseState) {
		this.purchaseState = purchaseState;
	}

	/**
	 * @return the developerPayload
	 */
	public String getDeveloperPayload() {
		return developerPayload;
	}

	/**
	 * @param developerPayload
	 *            the developerPayload to set
	 */
	public void setDeveloperPayload(String developerPayload) {
		this.developerPayload = developerPayload;
	}
}
