package wisematches.client.android.app.account.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityView extends LinearLayout {
	public PersonalityView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.personality, this, true);
	}

	public void setPersonality(Personality personality) {
		((TextView) findViewById(R.id.playerNickname)).setText(personality.getNickname());

		final ImageView state = (ImageView) findViewById(R.id.playerState);
		state.setVisibility(personality.isOnline() ? View.VISIBLE : View.GONE);
		invalidate();
	}
}
