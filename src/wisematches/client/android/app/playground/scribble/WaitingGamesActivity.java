package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.MenuFactory;
import wisematches.client.android.app.OnResultListener;
import wisematches.client.android.app.playground.scribble.board.ScribbleBoardActivity;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.scribble.CriterionViolation;
import wisematches.client.android.data.model.scribble.ScribbleProposal;
import wisematches.client.android.data.model.scribble.WaitingGames;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesActivity extends WiseMatchesActivity {
	private ListView proposalsList;
	private TextView globalJoinError;

	public WaitingGamesActivity() {
		super("Ожидающие игры", R.layout.playground_waiting_games, true);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		proposalsList = (ListView) findViewById(R.id.playgroundGamesList);
		globalJoinError = (TextView) findViewById(R.id.playgroundJoinErr);

		loadWaitingGames();
	}

	private void loadWaitingGames() {
		getRequestManager().getWaitingGames(new SmartDataResponse<WaitingGames>(this) {
			@Override
			protected void onData(WaitingGames data) {
				validateWaitingGames(data);
			}

			@Override
			protected void onCancel() {
				// TODO: show warning message
			}

			@Override
			protected void onRetry() {
				loadWaitingGames();
			}
		});
	}

	private void validateWaitingGames(WaitingGames data) {
		final ScribbleProposal[] proposals = data.getProposals();
		final CriterionViolation[] globalViolations = data.getGlobalViolations();

		proposalsList.setAdapter(new WaitingGamesAdapter(WaitingGamesActivity.this, proposals, globalViolations.length != 0, "Присоединиться",
				new WaitingGamesAdapter.ProposalClickListener() {
					@Override
					public void onAccept(long id, OnResultListener<Boolean> resultListener) {
						acceptWaitingGame(id, true, resultListener);
					}

					@Override
					public void onReject(long id, OnResultListener<Boolean> resultListener) {
						acceptWaitingGame(id, false, resultListener);
					}
				}
		));

		if (globalViolations.length != 0) {
			globalJoinError.setText(Html.fromHtml(globalViolations[0].getLongDescription()));
			globalJoinError.setVisibility(View.VISIBLE);
		} else {
			globalJoinError.setText("");
			globalJoinError.setVisibility(View.GONE);
		}
	}

	private void acceptWaitingGame(final long proposalId, final boolean accept, final OnResultListener<Boolean> resultListener) {
		getRequestManager().processWaitingGame(proposalId, accept, new SmartDataResponse<Id>(this) {
			@Override
			protected void onData(Id data) {
				if (data != null) {
					startActivity(ScribbleBoardActivity.createIntent(WaitingGamesActivity.this, data.getId()));
				} else {
					loadWaitingGames();
				}
				resultListener.onResult(true);
			}

			@Override
			protected void onRetry() {
				acceptWaitingGame(proposalId, accept, resultListener);
			}

			@Override
			protected void onCancel() {
				resultListener.onResult(false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuFactory.addMenuItem(menu, 1, 1, MenuFactory.Type.CREATE_GAME, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public static Intent createIntent(Context context) {
		return new Intent(context, WaitingGamesActivity.class);
	}
}
