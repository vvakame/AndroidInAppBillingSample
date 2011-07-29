package net.vvakame.android.inappbilling.server.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import net.vvakame.android.inappbilling.server.entity.AndroidOrder;
import net.vvakame.android.inappbilling.share.Consts.PurchaseState;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

/**
 * {@link IndexController} のテストケース.
 * @author vvakame
 */
public class IndexControllerTest extends ControllerTestCase {

	static String PATH = "/";


	/**
	 * 動作確認
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws ServletException
	 * @author vvakame
	 */
	@Test
	public void run_正常() throws NullPointerException, IllegalArgumentException, IOException,
			ServletException {

		// vvakame@gmail.com の公開鍵を利用
		String json =
				"{\"nonce\":-518127950800654345,\"orders\":[{\"notificationId\":\"android.test.purchased\",\"orderId\":\"transactionId.android.test.purchased\",\"packageName\":\"net.vvakame.android.inappbilling.device\",\"productId\":\"android.test.purchased\",\"purchaseTime\":1307350058273,\"purchaseState\":0}]}";
		String signature =
				"igc911CRaX4yFGsLjyeQYmizwrzQVe/dOP4BZS85w6E4ixUF+Cecdg8rV/rTiAqaRCzVWo+g+xjicftphVzepRr2kq9tyOfYZBKuLEdWsOop3+GFw4PEsos9NGByXU207DcmF4LDk6wpiHJUGCE/8dtWkTTlKwgUzrCce8okjtM=";

		tester.request.addParameter("data", json);
		tester.request.addParameter("signature", signature);
		tester.start(PATH);
		assertThat(tester.response.getStatus(), is(equalTo(HttpServletResponse.SC_OK)));

		assertThat(tester.count(AndroidOrder.class), is(1));
		AndroidOrder order = Datastore.query(AndroidOrder.class).limit(1).asSingle();

		assertThat(order.getKey().getName(), is("transactionId.android.test.purchased"));
		assertThat(order.getNotificationId(), is("android.test.purchased"));
		assertThat(order.getOrderId(), is("transactionId.android.test.purchased"));
		assertThat(order.getPackageName(), is("net.vvakame.android.inappbilling.device"));
		assertThat(order.getProductId(), is("android.test.purchased"));
		assertThat(order.getPurchaseTime(), is(1307350058273L));
		assertThat(order.getPurchaseState(), is(PurchaseState.valueOf(0)));
	}

	/**
	 * 動作確認
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws ServletException
	 * @author vvakame
	 */
	@Test
	public void run_ダメ署名() throws NullPointerException, IllegalArgumentException, IOException,
			ServletException {

		String json =
				"{\"nonce\":-216507106860535358,\"orders\":[{\"notificationId\":\"android.test.purchased\",\"orderId\":\"transactionId.android.test.purchased\",\"packageName\":\"jp.co.topgate.inappbilling\",\"productId\":\"android.test.purchased\",\"purchaseTime\":1307093327093,\"purchaseState\":0}]}";
		String signature = "だめ";

		json = URLEncoder.encode(json, "utf-8");
		signature = URLEncoder.encode(signature, "utf-8");

		tester.request.addParameter("data", json);
		tester.request.addParameter("signature", signature);
		tester.start(PATH);
		assertThat(tester.response.getStatus(), is(equalTo(HttpServletResponse.SC_BAD_REQUEST)));
	}
}
