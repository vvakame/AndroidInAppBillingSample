package net.vvakame.android.inappbilling.share;

import java.io.IOException;
import java.io.Writer;

import net.vvakame.android.inappbilling.share.Consts.PurchaseState;
import net.vvakame.util.jsonpullparser.JsonFormatException;
import net.vvakame.util.jsonpullparser.JsonPullParser;
import net.vvakame.util.jsonpullparser.JsonPullParser.State;
import net.vvakame.util.jsonpullparser.util.JsonUtil;
import net.vvakame.util.jsonpullparser.util.OnJsonObjectAddListener;
import net.vvakame.util.jsonpullparser.util.TokenConverter;

public class PurchaseStateConverter extends TokenConverter<PurchaseState> {

	static PurchaseStateConverter conv = null;

	/**
	 * インスタンスの取得
	 * 
	 * @return インスタンス
	 * @author vvakame
	 */
	public static PurchaseStateConverter getInstance() {
		if (conv == null) {
			conv = new PurchaseStateConverter();
		}
		return conv;
	}

	@Override
	public PurchaseState parse(JsonPullParser parser,
			OnJsonObjectAddListener listener) throws IOException,
			JsonFormatException {

		State state = parser.lookAhead();

		if (state == State.VALUE_NULL) {
			return null;
		}

		state = parser.getEventType();
		if (state != State.VALUE_LONG) {
			throw new JsonFormatException(
					"expected state is VALUE_LONG, but get=" + state);
		}

		return PurchaseState.valueOf((int) parser.getValueLong());
	}

	@Override
	public void encodeNullToNull(Writer writer, PurchaseState obj)
			throws IOException {
		if (obj == null) {
			writer.write("null");
			return;
		}

		JsonUtil.put(writer, obj.ordinal());
	}
}
