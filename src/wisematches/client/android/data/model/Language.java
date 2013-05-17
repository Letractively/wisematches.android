package wisematches.client.android.data.model;

import wisematches.client.android.R;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum Language {
	RU(R.string.language_ru),
	EN(R.string.language_en);

	private final String code;
	private final int resourceId;

	private Language(int resourceId) {
		this.code = name().toLowerCase();
		this.resourceId = resourceId;
	}

	public String getCode() {
		return code;
	}

	public int getResourceId() {
		return resourceId;
	}
}
