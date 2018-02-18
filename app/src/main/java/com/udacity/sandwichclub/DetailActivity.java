package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int DEFAULT_POSITION = -1;

    private ImageView mSandwichImageView;
    private TextView mAlsoKnownAsLabelTextView, mAlsoKnownAsTextView, mIngredientsLabelTextView,
            mIngredientsTextView, mOriginLabelTextView, mOriginTextView, mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mSandwichImageView = findViewById(R.id.image_iv);
        mAlsoKnownAsTextView = findViewById(R.id.also_known_tv);
        mAlsoKnownAsLabelTextView = findViewById(R.id.also_known_label_tv);
        mIngredientsLabelTextView = findViewById(R.id.ingredients_label_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);
        mOriginLabelTextView = findViewById(R.id.origin_label_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = 0;
        if (intent != null) {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            Log.w(TAG, e.getMessage());
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
        } else {
            populateUI(sandwich);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        setTitle(sandwich.getMainName());
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mSandwichImageView);

        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        if (alsoKnownAsList.isEmpty()) {
            hideAlsoKnownAsSection();
        } else {
            mAlsoKnownAsTextView.setText(getListAsString(alsoKnownAsList));
        }

        List<String> ingredientsList = sandwich.getIngredients();
        if (ingredientsList.isEmpty()) {
            hideIngredientsSection();
        } else {
            mIngredientsTextView.setText(getListAsString(ingredientsList));
        }

        String placeOfOriginString = sandwich.getPlaceOfOrigin();
        if (TextUtils.isEmpty(placeOfOriginString)) {
            hidePlaceOfOriginSection();
        } else {
            mOriginTextView.setText(placeOfOriginString);
        }
        mDescriptionTextView.setText(sandwich.getDescription());
    }

    private void hidePlaceOfOriginSection() {
        mOriginLabelTextView.setVisibility(View.GONE);
        mOriginTextView.setVisibility(View.GONE);
    }

    private void hideIngredientsSection() {
        mIngredientsLabelTextView.setVisibility(View.GONE);
        mIngredientsTextView.setVisibility(View.GONE);
    }

    private void hideAlsoKnownAsSection() {
        mAlsoKnownAsLabelTextView.setVisibility(View.GONE);
        mAlsoKnownAsTextView.setVisibility(View.GONE);
    }

    private String getListAsString(List<String> list) {
        String unformattedListString = list.toString();
        // Cuts "[" and "]" characters from the beginning and end of the string
        return list.toString().substring(1, unformattedListString.length() - 1);
    }
}
