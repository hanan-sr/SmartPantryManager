package za.co.smartpantry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import za.co.smartpantry.adapter.PantryAdapter;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.PantryItem;
import java.util.List;

public class MainActivity extends Activity {
    private DatabaseHelper database;
    private List<PantryItem> pantryItems;
    private PantryAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DatabaseHelper(this);
        Button add = findViewById(R.id.addButton);
        Button suggestions = findViewById(R.id.suggestionsButton);
        Button settings = findViewById(R.id.settingsButton);
        add.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));
        suggestions.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        ListView list = findViewById(R.id.pantryList);
        pantryItems = database.getAllPantryItems();
        adapter = new PantryAdapter(this, pantryItems);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> showItemOptions(pantryItems.get(position)));
    }

    @Override protected void onResume() {
        super.onResume();
        if (database != null) refreshPantry();
    }

    private void refreshPantry() {
        pantryItems.clear();
        pantryItems.addAll(database.getAllPantryItems());
        adapter.notifyDataSetChanged();
        ((TextView)findViewById(R.id.countText)).setText(pantryItems.size() + (pantryItems.size() == 1 ? " ingredient" : " ingredients"));
    }

    private void showItemOptions(PantryItem item) {
        new AlertDialog.Builder(this).setTitle(item.getName())
            .setItems(new String[]{"Edit", "Delete", "Cancel"}, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent(this, AddEditIngredientActivity.class);
                    intent.putExtra("ingredient_id", item.getId());
                    startActivity(intent);
                } else if (which == 1) {
                    new AlertDialog.Builder(this).setTitle("Delete ingredient?")
                        .setMessage("Remove " + item.getName() + " from your pantry?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", (d, w) -> {
                            database.deletePantryItem(item.getId());
                            refreshPantry();
                            Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                        }).show();
                }
            }).show();
    }

    @Override protected void onDestroy() { super.onDestroy(); if (database != null) database.close(); }
}
