package code.a.software.shiftme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import helpers.ThemeHelper;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(ThemeHelper.getSubThemeId(MainActivity.settings.getThemeID()));
        setContentView(R.layout.activity_help);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(getString(R.string.help));
    }
}