package wisematches.client.android.data.service;

import com.foxykeep.datadroid.service.RequestService;
import wisematches.client.android.data.qwe.RequestType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public interface AbstractOperation extends RequestService.Operation {
	RequestType getRequestType();
}
