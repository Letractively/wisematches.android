package wisematches.client.android.data.qwe.parser;

import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.qwe.ResponseParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityParser implements ResponseParser<Personality> {
    @Override
    public Personality parseResponse(JSONObject object) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }
}
