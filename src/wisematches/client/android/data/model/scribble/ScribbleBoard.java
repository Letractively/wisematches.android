package wisematches.client.android.data.model.scribble;

import wisematches.client.android.data.model.person.Personality;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoard implements BoardValidator {
	private final long id;
	private final ScribbleStatus status;
	private final ScoreEngine scoreEngine;
	private final ScribbleSettings settings;
	private final ScribbleHand[] players;
	private final ScribbleTile[] handTiles = new ScribbleTile[7];
	private final List<ScribbleMove> moves = new ArrayList<>();

	private final Set<Integer> placedTiles = new HashSet<>();

	private final ScribbleController controller;

	private final SelectionModel selectionModel = new SelectionModel();
	private final List<BoardMoveListener> moveListeners = new ArrayList<>();
	private final List<BoardStateListener> stateListeners = new ArrayList<>();

	public ScribbleBoard(ScribbleController controller, ScribbleSnapshot snapshot) {
		this.controller = controller;

		id = snapshot.getDescriptor().getId();
		final ScribbleDescriptor descriptor = snapshot.getDescriptor();

		status = descriptor.getStatus();
		players = descriptor.getPlayers();
		settings = descriptor.getSettings();
		scoreEngine = snapshot.getScoreEngine();

		for (ScribbleMove move : snapshot.getMoves()) {
			registerGameMove(move);
		}

		final ScribbleTile[] hd = snapshot.getHandTiles();
		System.arraycopy(hd, 0, handTiles, 0, hd.length);
	}


	public void addSelectionListener(SelectionListener l) {
		selectionModel.addSelectionListener(l);
	}

	public void removeSelectionListener(SelectionListener l) {
		selectionModel.removeSelectionListener(l);
	}

	public void addBoardMoveListener(BoardMoveListener l) {
		if (l != null) {
			moveListeners.add(l);
		}
	}

	public void removeBoardMoveListener(BoardMoveListener l) {
		if (l != null) {
			moveListeners.remove(l);
		}
	}

	public void addBoardStateListener(BoardStateListener l) {
		if (l != null) {
			stateListeners.add(l);
		}
	}

	public void removeBoardStateListener(BoardStateListener l) {
		if (l != null) {
			stateListeners.remove(l);
		}
	}

	public long getId() {
		return id;
	}

	public ScribbleStatus getStatus() {
		return status;
	}

	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	public Personality getBoardViewer() {
		return controller.getBoardViewer();
	}

	public boolean isViewerTurn() {
		final Personality viewer = getBoardViewer();
		final ScribbleHand playerTurn = getPlayerTurn();
		return viewer != null && playerTurn != null && viewer.getId() == playerTurn.getPersonality().getId();
	}

	public ScribbleHand getPlayerTurn() {
		final long playerTurn = status.getPlayerTurn();
		if (playerTurn != 0) {
			return getPlayer(playerTurn);
		}
		return null;
	}

	public ScribbleHand getPlayer(long id) {
		for (ScribbleHand player : players) {
			if (player.getPersonality().getId() == id) {
				return player;
			}
		}
		return null;
	}

	public ScribbleHand[] getPlayers() {
		return players;
	}

	public List<ScribbleMove> getMoves() {
		return moves;
	}

	public ScribbleTile[] getHandTiles() {
		return handTiles;
	}


	public boolean isBoardTile(int number) {
		return placedTiles.contains(number);
	}


	public void resign() {
		controller.resign(this);
	}

	public void passTurn() {
		controller.passTurn(this);
	}

	public void makeTurn(ScribbleWord word) {
		controller.makeTurn(word, this);
	}

	public void exchange(Set<ScribbleTile> tiles) {
		controller.exchange(tiles, this);
	}

	@Override
	public void validateBoard(ScribbleChanges changes) {
		status.validate(changes.getStatus());

		final ScribbleScore[] scores = changes.getScores();
		for (int i = 0; i < players.length; i++) {
			players[i].getScores().validate(scores[i]);
		}

		final ScribbleTile[] tiles = changes.getHandTiles();
		Arrays.fill(handTiles, null);
		System.arraycopy(tiles, 0, handTiles, 0, tiles.length);

		final ScribbleMove lastMove = moves.get(moves.size() - 1);
		for (ScribbleMove scribbleMove : changes.getMoves()) {
			if (scribbleMove.getNumber() > lastMove.getNumber()) {
				registerGameMove(scribbleMove);
			}
		}

		for (BoardStateListener stateListener : stateListeners) {
			stateListener.gameStateValidated();
		}
	}

	public SelectionModel getSelectionModel() {
		return selectionModel;
	}

	public ScoreCalculation calculateScore(ScribbleWord word) {
		return scoreEngine.calculateScore(this, word);
	}

	private void registerGameMove(ScribbleMove move) {
		moves.add(move);

		if (move instanceof ScribbleMove.Make) {
			ScribbleMove.Make make = (ScribbleMove.Make) move;
			for (ScribbleTile tile : make.getWord().getTiles()) {
				placedTiles.add(tile.getNumber());
			}
		}

		for (BoardMoveListener moveListener : moveListeners) {
			moveListener.gameMoveDone(move);
		}
	}
}
