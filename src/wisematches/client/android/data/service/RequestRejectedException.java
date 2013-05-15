package wisematches.client.android.data.service;

import com.foxykeep.datadroid.exception.CustomRequestException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RequestRejectedException extends CustomRequestException {
	private final String code;
	private final String message;

	public RequestRejectedException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
