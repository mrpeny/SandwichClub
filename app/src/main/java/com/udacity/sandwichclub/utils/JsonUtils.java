package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that supports JSON parsing into Java objects.
 */
public class JsonUtils {
    /* Type: JsonObject */
    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    /* Type: JsonArray */
    private static final String ALSO_KNOWN_AS = "alsoKnownAs";

    private static final String PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_URL = "image";

    /* Type: JsonArray */
    private static final String INGREDIENTS = "ingredients";

    /**
     * Parses a JSON string to a Sandwich object.
     *
     * @param json the string containing the JSON object to be parsed
     * @return the {@link Sandwich} object parsed from the given string
     */
    public static Sandwich parseSandwichJson(String json) throws JSONException {
        JSONObject name;
        String mainName = "";
        JSONArray alsoKnownAs = null;
        JSONArray ingredients = null;
        List<String> alsoKnownAsList = new ArrayList<>();
        List<String> ingredientsList = new ArrayList<>();

        JSONObject sandwichJson = new JSONObject(json);

        if (sandwichJson.has(NAME)) {
            name = sandwichJson.getJSONObject(NAME);
            mainName = name.optString(MAIN_NAME);
            if (name.has(ALSO_KNOWN_AS)) {
                alsoKnownAs = name.getJSONArray(ALSO_KNOWN_AS);
            }
        }

        String placeOfOrigin = sandwichJson.optString(PLACE_OF_ORIGIN);
        String description = sandwichJson.optString(DESCRIPTION);
        String imageUrl = sandwichJson.optString(IMAGE_URL);

        if (sandwichJson.has(INGREDIENTS)) {
            ingredients = sandwichJson.getJSONArray(INGREDIENTS);
        }

        if (alsoKnownAs != null) {
            for (int i = 0; i < alsoKnownAs.length(); i++) {
                alsoKnownAsList.add(alsoKnownAs.getString(i));
            }
        }

        if (ingredients != null) {
            for (int i = 0; i < ingredients.length(); i++) {
                ingredientsList.add(ingredients.getString(i));
            }
        }

        return new Sandwich(mainName, alsoKnownAsList, placeOfOrigin, description, imageUrl,
                ingredientsList);
    }
}
