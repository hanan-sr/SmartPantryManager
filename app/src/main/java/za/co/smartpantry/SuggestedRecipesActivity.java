package za.co.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import za.co.smartpantry.adapter.RecipeAdapter;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.PantryItem;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.util.IngredientMatcher;
import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends Activity {
    private DatabaseHelper database;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        database = new DatabaseHelper(this);
        List<PantryItem> pantry = database.getAllPantryItems();
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : database.getAllRecipes()) {
            if (IngredientMatcher.canMake(recipe, pantry)) matches.add(recipe);
        }

        ListView list = findViewById(R.id.recipeList);
        TextView empty = findViewById(R.id.emptyText);
        if (matches.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            list.setAdapter(new RecipeAdapter(this, matches));
            list.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(this, RecipeDetailActivity.class);
                intent.putExtra("recipe_id", matches.get(position).getId());
                startActivity(intent);
            });
        }
        ((Button)findViewById(R.id.backButton)).setOnClickListener(v -> finish());
    }
    @Override protected void onDestroy() { super.onDestroy(); if (database != null) database.close(); }
}
