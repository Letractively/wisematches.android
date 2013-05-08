package wisematches.client.android.data.service;

import com.foxykeep.datadroid.service.RequestService;
import wisematches.client.android.data.qwe.RequestType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractRequestService extends RequestService {
	private final AbstractOperation[] operations;

	protected AbstractRequestService(AbstractOperation... operations) {
		final RequestType[] values = RequestType.values();

		this.operations = new AbstractOperation[values.length];
		for (AbstractOperation operation : operations) {
			this.operations[operation.getRequestType().code()] = operation;
		}

		for (RequestType value : values) {
			if (this.operations[value.code()] == null) {
				throw new IllegalArgumentException("There is no operation for request: " + value);
			}
		}
	}

	@Override
	public Operation getOperationForType(int requestType) {
		return this.operations[requestType];
	}
}
