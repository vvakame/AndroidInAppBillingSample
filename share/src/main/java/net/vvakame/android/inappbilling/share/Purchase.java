package net.vvakame.android.inappbilling.share;

import java.util.List;

import net.vvakame.util.jsonpullparser.annotation.JsonKey;
import net.vvakame.util.jsonpullparser.annotation.JsonModel;

@JsonModel(decamelize = false, treatUnknownKeyAsError = false, builder = true)
public class Purchase {

	@JsonKey
	long nonce;

	@JsonKey
	List<Order> orders;

	/**
	 * @return the nonce
	 */
	public long getNonce() {
		return nonce;
	}

	/**
	 * @param nonce
	 *            the nonce to set
	 */
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}

	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
