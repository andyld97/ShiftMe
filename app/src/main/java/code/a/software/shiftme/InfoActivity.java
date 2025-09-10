package code.a.software.shiftme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import helpers.ThemeHelper;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(ThemeHelper.getSubThemeId(MainActivity.settings.getThemeID()));
        Resources.Theme curTheme = getTheme();

        final int color = ThemeHelper.getThemeColor(R.attr.colorTransparentBackground, curTheme);
        final int nonTransparentColor = code.a.software.shiftme.Helper.darkenColor((color & 0x00FFFFFF) | 0xFF000000, 0.8F);

        View decorView = getWindow().getDecorView();
        ViewCompat.setOnApplyWindowInsetsListener(decorView, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            v.setPadding(0, 0, 0, 0);
            v.setBackground(new ColorDrawable(nonTransparentColor));
            return insets;
        });
        ViewCompat.requestApplyInsets(decorView);


        setContentView(R.layout.activity_info);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        setTitle(getString(R.string.about));
    }
}