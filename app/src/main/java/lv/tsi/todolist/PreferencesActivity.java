package lv.tsi.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    public void setWhite(View view) {
        saveColor(getResources().getColor(R.color.white, getTheme()));
    }

    public void setPink(View view) {
        saveColor(getResources().getColor(R.color.pink, getTheme()));
    }

    private void saveColor(int color) {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(MainActivity.LIST_COLOR, color);
        editor.apply();
        Toast.makeText(PreferencesActivity.this, "Color changed", Toast.LENGTH_SHORT).show();
    }

}
