package za.co.smartpantry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.PantryItem;

public class AddEditIngredientActivity extends Activity {
    private static final String[] UNITS = {"pcs", "g", "kg", "ml", "l", "slices", "cloves", "can"};
    private DatabaseHelper database;
    private EditText nameInput, quantityInput, expiryInput;
    private Spinner unitSpinner;
    private int ingredientId = -1;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        database = new DatabaseHelper(this);
        nameInput = findViewById(R.id.nameInput);
        quantityInput = findViewById(R.id.quantityInput);
        expiryInput = findViewById(R.id.expiryInput);
        unitSpinner = findViewById(R.id.unitSpinner);
        TextView title = findViewById(R.id.formTitle);
        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UNITS));

        ingredientId = getIntent().getIntExtra("ingredient_id", -1);
        if (ingredientId != -1) {
            title.setText("Edit ingredient");
            PantryItem item = database.getPantryItem(ingredientId);
            if (item != null) {
                nameInput.setText(item.getName());
                quantityInput.setText(String.valueOf(item.getQuantity()));
                expiryInput.setText(item.getExpiryDate());
                for (int i = 0; i < UNITS.length; i++) if (UNITS[i].equals(item.getUnit())) unitSpinner.setSelection(i);
            }
        }

        ((Button)findViewById(R.id.saveButton)).setOnClickListener(v -> saveIngredient());
        ((Button)findViewById(R.id.cancelButton)).setOnClickListener(v -> finish());
    }

    private void saveIngredient() {
        String name = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();
        String expiry = expiryInput.getText().toString().trim();
        TextView error = findViewById(R.id.errorText);
        error.setText("");

        if (name.isEmpty()) { error.setText("Please enter an ingredient name."); nameInput.requestFocus(); return; }
        if (!name.matches("[A-Za-z][A-Za-z ]{0,49}")) { error.setText("Use letters and spaces only for the ingredient name."); nameInput.requestFocus(); return; }
        if (quantityText.isEmpty()) { error.setText("Please enter a quantity."); quantityInput.requestFocus(); return; }
        double quantity;
        try { quantity = Double.parseDouble(quantityText); }
        catch (NumberFormatException e) { error.setText("Enter a valid number."); return; }
        if (quantity <= 0 || quantity > 1000000) { error.setText("Quantity must be greater than zero."); return; }
        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            error.setText("Expiry date must use YYYY-MM-DD, or be left blank."); return;
        }

        long result;
        if (ingredientId == -1) result = database.insertPantryItem(name, quantity, unitSpinner.getSelectedItem().toString(), expiry);
        else result = database.updatePantryItem(ingredientId, name, quantity, unitSpinner.getSelectedItem().toString(), expiry);

        if (result == -1) {
            error.setText("Could not save. This ingredient may already exist in your pantry.");
        } else {
            Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override protected void onDestroy() { super.onDestroy(); if (database != null) database.close(); }
}
