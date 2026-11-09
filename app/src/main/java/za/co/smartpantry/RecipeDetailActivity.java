package za.co.smartpantry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.model.RecipeIngredient;

public class RecipeDetailActivity extends Activity {
    private DatabaseHelper database;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        database = new DatabaseHelper(this);
        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        Recipe recipe = database.getRecipe(recipeId);
        if (recipe == null) { finish(); return; }

        ((TextView)findViewById(R.id.detailName)).setText(recipe.getName());
        StringBuilder ingredients = new StringBuilder("Required ingredients\n");
        for (RecipeIngredient item : recipe.getIngredients()) {
            ingredients.append("\n• ").append(format(item.getQuantity())).append(" ").append(item.getUnit()).append(" ").append(item.getName());
        }
        ((TextView)findViewById(R.id.detailIngredients)).setText(ingredients.toString());
        ((TextView)findViewById(R.id.detailMethod)).setText("Preparation method\n\n" + recipe.getMethod());
        ((Button)findViewById(R.id.detailBackButton)).setOnClickListener(v -> finish());
    }
    private String format(double value) { return value == Math.floor(value) ? String.valueOf((long)value) : String.valueOf(value); }
    @Override protected void onDestroy() { super.onDestroy(); if (database != null) database.close(); }
}
