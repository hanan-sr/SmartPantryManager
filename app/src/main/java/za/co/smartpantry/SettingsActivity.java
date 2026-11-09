package za.co.smartpantry;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends Activity {
    private static final String PREFS = "smart_pantry_preferences";
    private static final String EXPIRY_ALERTS = "expiry_alerts";
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        Switch expirySwitch = findViewById(R.id.expirySwitch);
        expirySwitch.setChecked(preferences.getBoolean(EXPIRY_ALERTS, true));
        expirySwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
            preferences.edit().putBoolean(EXPIRY_ALERTS, isChecked).apply());
        ((Button)findViewById(R.id.settingsBackButton)).setOnClickListener(v -> finish());
    }
}
