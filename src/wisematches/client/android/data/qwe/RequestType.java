package wisematches.client.android.data.qwe;

import com.foxykeep.datadroid.requestmanager.Request;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum RequestType {
	AUTHENTICATION;

	RequestType() {
	}

	public final int code() {
		return ordinal();
	}

	public final Request create() {
		return new Request(code());
	}

	public ResponseParser getResponseParser() {
		return null;
	}
}
