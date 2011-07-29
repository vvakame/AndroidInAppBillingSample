package net.vvakame.android.inappbilling.server.service;

import java.util.List;

import net.vvakame.android.inappbilling.server.entity.AndroidOrder;
import net.vvakame.android.inappbilling.share.Order;
import net.vvakame.android.inappbilling.share.Purchase;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

/**
 * {@link AndroidOrder} のサービス
 * @author vvakame
 */
public class AndroidOrderService {

	static Key createKey(String orderId) {
		return Datastore.createKey(AndroidOrder.class, orderId);
	}

	/**
	 * 注文の一覧を {@link AndroidOrder} に変換する.
	 * @param purchase
	 * @return {@link AndroidOrder} のリスト
	 * @author vvakame
	 */
	public static List<AndroidOrder> conv(Purchase purchase) {
		List<AndroidOrder> orders = Lists.newArrayList();

		for (Order order : purchase.getOrders()) {
			AndroidOrder andOrder = new AndroidOrder();
			andOrder.setKey(createKey(order.getOrderId()));
			andOrder.setNotificationId(order.getNotificationId());
			andOrder.setOrderId(order.getOrderId());
			andOrder.setPackageName(order.getPackageName());
			andOrder.setProductId(order.getProductId());
			andOrder.setPurchaseTime(order.getPurchaseTime());
			andOrder.setPurchaseState(order.getPurchaseState());

			orders.add(andOrder);
		}

		return orders;
	}
}
