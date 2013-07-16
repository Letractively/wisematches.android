package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.scribble.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryWidget extends AbstractBoardWidget {
	private Language language;

	private Button dictionaryAction;
	private EditText dictionaryField;
	private TimerTask dictionaryChecker;
	private ProgressBar dictionaryProgress;

	private final Timer scribbleBoardTimer = new Timer("ScribbleBoardTimer");
	private final WordCheckerRunnable wordChecker = new WordCheckerRunnable();
	private final TheSelectionListener selectionListener = new TheSelectionListener();

	public DictionaryWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_dict, "Словарь");

		dictionaryField = (EditText) findViewById(R.id.scribbleBoardDictField);
		dictionaryAction = (Button) findViewById(R.id.scribbleBoardDictAction);
		dictionaryProgress = (ProgressBar) findViewById(R.id.scribbleBoardDictProgress);

		dictionaryField.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(final Editable s) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dictionaryProgress.setVisibility(View.INVISIBLE);

				final boolean empty = s.length() <= 1;

				dictionaryAction.setEnabled(!empty);

				if (dictionaryChecker != null) {
					dictionaryChecker.cancel();
					dictionaryChecker = null;
				}

				if (!empty) {
					dictionaryChecker = new TimerTask() {
						@Override
						public void run() {
							((WiseMatchesActivity) getContext()).runOnUiThread(wordChecker);
						}
					};
					scribbleBoardTimer.schedule(dictionaryChecker, 3000);
				}
			}
		});

		dictionaryAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dictionaryChecker != null) {
					dictionaryChecker.cancel();
					dictionaryChecker = null;
				}
				wordChecker.run();
			}
		});
	}

	private void processSelectionChange(ScribbleWord word) {
		if (word == null) {
			dictionaryField.setText("");
		} else {
			dictionaryField.setText(word.getText());
		}
	}

	@Override
	public void boardInitialized(ScribbleBoard board) {
		language = board.getSettings().getLanguage();

		board.addSelectionListener(selectionListener);
	}

	@Override
	public void boardTerminated(ScribbleBoard board) {
		board.removeSelectionListener(selectionListener);
		language = null;
	}

	private class WordCheckerRunnable implements Runnable {
		@Override
		public void run() {
			dictionaryAction.setEnabled(false);
			dictionaryProgress.setVisibility(View.VISIBLE);
			dictionaryProgress.getIndeterminateDrawable().setColorFilter(null);

			final String lang = language.getCode();
			final Editable text = dictionaryField.getText();

			final WiseMatchesActivity context = (WiseMatchesActivity) getContext();
			final DataRequestManager requestManager = context.getRequestManager();

			requestManager.getWordEntry(text.toString(), lang, new DataRequestManager.DataResponse<WordEntry>() {
				@Override
				public void onSuccess(WordEntry data) {
					if (data != null) {
						dictionaryProgress.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
					} else {
						dictionaryProgress.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
					}

					finish(false);
				}

				@Override
				public void onFailure(String code, String message) {
					finish(true);
				}

				@Override
				public void onDataError() {
					finish(true);
				}

				@Override
				public void onConnectionError(int code) {
					finish(true);
				}

				private void finish(boolean b) {
					dictionaryChecker = null;
					dictionaryAction.setEnabled(true);

					if (b) {
						dictionaryProgress.setVisibility(View.INVISIBLE);
					}
				}
			});
		}
	}

	private class TheSelectionListener implements SelectionListener {
		@Override
		public void onSelectionChanged(ScribbleWord word, ScribbleTile[] tiles) {
			processSelectionChange(word);
		}
	}

}
