package net.vvakame.android.inappbilling.server.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import net.vvakame.android.inappbilling.server.entity.AndroidOrder;
import net.vvakame.android.inappbilling.server.service.AndroidOrderService;
import net.vvakame.android.inappbilling.server.util.Verify;
import net.vvakame.android.inappbilling.share.Purchase;
import net.vvakame.android.inappbilling.share.PurchaseGen;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

/**
 * InAppBilling署名検証用コントローラ
 * @author vvakame
 */
public class IndexController extends Controller {

	Logger logger = Logger.getLogger(IndexController.class.getCanonicalName());


	@Override
	protected Navigation run() throws Exception {

		String json = asString("data");
		String signature = asString("signature");

		logger.log(Level.FINEST, "data=" + json);
		logger.log(Level.FINEST, "signature=" + signature);

		if (!Verify.checkSignature(json, signature)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		Purchase purchase = PurchaseGen.get(json);
		List<AndroidOrder> orders = AndroidOrderService.conv(purchase);
		if (orders.size() != 0) {
			Datastore.put(orders);
		}

		return null;
	}
}
