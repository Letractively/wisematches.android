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
	private TextView nicknameView;
	private ImageView state;

	public PersonalityView(Context context) {
		this(context, null);
	}

	public PersonalityView(Context context, AttributeSet attrs) {
		this(context, attrs, null);
	}

	public PersonalityView(Context context, AttributeSet attrs, Personality personality) {
		super(context, attrs);
		setOrientation(HORIZONTAL);
		LayoutInflater.from(context).inflate(R.layout.personality, this, true);

		nicknameView = (TextView) findViewById(R.id.playerNickname);
		state = (ImageView) findViewById(R.id.playerState);

		if (personality != null) {
			setPersonality(personality);
		}
	}

	public void setPersonality(Personality personality) {
		setPersonality(personality, true);
	}

	public void setPersonality(Personality personality, boolean invalidate) {
		nicknameView.setText(personality.getNickname());
		state.setVisibility(personality.isOnline() ? View.VISIBLE : View.GONE);
		if (invalidate) {
			invalidate();
		}
	}
}
