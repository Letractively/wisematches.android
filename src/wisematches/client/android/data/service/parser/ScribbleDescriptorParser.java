package wisematches.client.android.data.service.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.model.scribble.ScribbleScore;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptorParser {
	public static ScribbleDescriptor parse(JSONObject jsonGame) throws JSONException {
		long boarId = jsonGame.getLong("boardId");
		String title = jsonGame.getString("title");
		String startedDate = jsonGame.optString("startedDate");
		String finishedData = jsonGame.optString("finishedData");
		String language = jsonGame.getString("language");
		String resolution = jsonGame.getString("resolution");
		int movesCount = jsonGame.getInt("movesCount");
		long playerTurn = jsonGame.getInt("playerTurn");

		int playerTurnIndex = -1;
		JSONArray jsonPlayers = jsonGame.getJSONArray("players");
		Personality[] players = new Personality[jsonPlayers.length()];
		for (int j = 0; j < jsonPlayers.length(); j++) {
			final Personality parse = PersonalityParser.parse(jsonPlayers.getJSONObject(j));
			if (parse != null && parse.getId() == playerTurn) {
				playerTurnIndex = j;
			}
			players[j] = parse;
		}

		JSONArray jsonScores = jsonGame.getJSONArray("scores");
		ScribbleScore[] scores = new ScribbleScore[jsonScores.length()];
		for (int j = 0; j < jsonScores.length(); j++) {
			scores[j] = ScribbleScoreParser.parse(jsonScores.getJSONObject(j));
		}
		return new ScribbleDescriptor(boarId, title, startedDate, finishedData, language, resolution, movesCount, playerTurnIndex, players, scores);
	}
}
