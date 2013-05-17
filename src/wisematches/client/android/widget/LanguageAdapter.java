package wisematches.client.android.widget;

import android.content.Context;
import android.view.View;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.widget.ArrayAdapter;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LanguageAdapter extends ArrayAdapter<Language> {
	public LanguageAdapter(Context context, int viewResource, int dropDownResource) {
		super(context, viewResource, dropDownResource, false, Arrays.asList(Language.values()));
	}

	@Override
	protected void populateValueToView(View view, Language value) {
		defaultPopulateValueToView(view, context.getResources().getString(value.getResourceId()));
	}
}
