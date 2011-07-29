package net.vvakame.android.inappbilling.share;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import net.vvakame.android.inappbilling.share.Order;
import net.vvakame.android.inappbilling.share.Purchase;
import net.vvakame.android.inappbilling.share.Consts.PurchaseState;
import net.vvakame.util.jsonpullparser.JsonFormatException;

public class PurchaseTest {

	@Test
	public void run() throws IOException, JsonFormatException {
		String json = "{\"nonce\":-4891196084070180799,\"orders\":[{\"notificationId\":\"android.test.purchased\",\"orderId\":\"transactionId.android.test.purchased\",\"packageName\":\"jp.co.topgate.inappbilling\",\"productId\":\"android.test.purchased\",\"purchaseTime\":1307087461593,\"purchaseState\":0}]}";

		Purchase purchase = PurchaseGen.get(json);
		assertThat(purchase, notNullValue());

		assertThat(purchase.getNonce(), not(0L));
		assertThat(purchase.getOrders().size(), is(1));

		Order order = purchase.getOrders().get(0);
		assertThat(order.getNotificationId(), notNullValue());
		assertThat(order.getOrderId(), notNullValue());
		assertThat(order.getPackageName(), notNullValue());
		assertThat(order.getProductId(), notNullValue());
		assertThat(order.getPurchaseTime(), not(0L));
		assertThat(order.getPurchaseState(), is(PurchaseState.valueOf(0)));
	}
}
