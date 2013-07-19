package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleChanges;
import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.scribble.ScribbleChangesParser;

import java.io.UnsupportedEncodingException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardActionOperation extends JSONOperation.Primitive<ScribbleChanges> {
	public static final String PARAM_BOARD_ID = "PARAM_BOARD_ID";
	public static final String PARAM_ACTION_TYPE = "PARAM_ACTION_TYPE";
	public static final String PARAM_WORD = "PARAM_WORD";
	public static final String PARAM_TILE_ITEM = "PARAM_TILE_ITEM";
	public static final String PARAM_TILES_COUNT = "PARAM_TILES_COUNT";

	public static final String ACTION_TYPE_MAKE = "MAKE";
	public static final String ACTION_TYPE_PASS = "PASS";
	public static final String ACTION_TYPE_RESIGN = "RESIGN";
	public static final String ACTION_TYPE_EXCHANGE = "EXCHANGE";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final long id = request.getLong(PARAM_BOARD_ID);
		final String type = request.getString(PARAM_ACTION_TYPE);

		HttpPost post;
		switch (type) {
			case ACTION_TYPE_MAKE:
				post = new HttpPost("/playground/scribble/board/make.ajax?b=" + id);
				final ScribbleWord word = (ScribbleWord) request.getParcelable(PARAM_WORD);

				try {
					final JSONObject o = new JSONObject();

					final JSONArray ts = new JSONArray();
					for (final ScribbleTile tile : word.getTiles()) {
						final JSONObject t = new JSONObject();
						t.put("cost", tile.getCost());
						t.put("number", tile.getNumber());
						t.put("letter", tile.getLetter());

						ts.put(t);
					}

					final JSONObject p = new JSONObject();
					p.put("row", word.getRow());
					p.put("column", word.getColumn());

					o.put("tiles", ts);
					o.put("position", p);
					o.put("direction", word.getDirection().name());

					post.setEntity(new StringEntity(o.toString(), HTTP.UTF_8));
				} catch (JSONException e) {
					throw new DataException("JSON Object can't be created", e);
				} catch (UnsupportedEncodingException e) {
					throw new DataException("Unsupported encoding UTF8?", e);
				}
				break;
			case ACTION_TYPE_PASS:
				post = new HttpPost("/playground/scribble/board/pass.ajax?b=" + id);
				break;
			case ACTION_TYPE_EXCHANGE:
				post = new HttpPost("/playground/scribble/board/exchange.ajax?b=" + id);
				try {

					final JSONArray ts = new JSONArray();
					int count = request.getInt(PARAM_TILES_COUNT);
					for (int i = 0; i < count; i++) {
						final ScribbleTile tile = (ScribbleTile) request.getParcelable(PARAM_TILE_ITEM + "_ " + i);

						final JSONObject t = new JSONObject();
						t.put("cost", tile.getCost());
						t.put("number", tile.getNumber());
						t.put("letter", tile.getLetter());

						ts.put(t);
					}

					post.setEntity(new StringEntity(ts.toString(), HTTP.UTF_8));
				} catch (JSONException e) {
					throw new DataException("JSON Object can't be created", e);
				} catch (UnsupportedEncodingException e) {
					throw new DataException("Unsupported encoding UTF8?", e);
				}
				break;
			case ACTION_TYPE_RESIGN:
				post = new HttpPost("/playground/scribble/board/resign.ajax?b=" + id);
				break;
			default:
				throw new DataException("Unsupported action type: " + type);
		}
		return post;
	}

	@Override
	protected ScribbleChanges createResponse(Object data) throws JSONException {
		return ScribbleChangesParser.parse((JSONObject) data);
	}
}
