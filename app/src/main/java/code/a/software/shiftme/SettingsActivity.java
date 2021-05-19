package code.a.software.shiftme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import helpers.ThemeHelper;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private final int PICK_IMAGE = 666;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 665;

    private RadioButton radioButtonThemeBlue;
    private RadioButton radioButtonThemeRed;
    private RadioButton radioButtonThemeGreen;
    private RadioButton radioButtonThemePurple;
    private RadioButton radioButtonThemeGray;

    private RadioButton[] themeButtons;
    private int[] themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(ThemeHelper.getSubThemeId(MainActivity.settings.getThemeID()));
        setContentView(R.layout.activity_settings);

        Button btnChooseBackground = findViewById(R.id.btnChooseBackground);
        Button btnClearBackground = findViewById(R.id.btnClearBackground);
        btnClearBackground.setOnClickListener(v -> {
            MainActivity.settings.setBackgroundImagePath("");
            MainActivity.settings.saveSettings();

            Toast.makeText(this, getString(R.string.removedBackground), Toast.LENGTH_SHORT).show();
        });

        radioButtonThemeBlue = findViewById(R.id.radioButtonThemeBlue);
        radioButtonThemeRed = findViewById(R.id.radioButtonThemeRed);
        radioButtonThemeGreen = findViewById(R.id.radioButtonThemeGreen);
        radioButtonThemePurple = findViewById(R.id.radioButtonThemePurple);
        radioButtonThemeGray = findViewById(R.id.radioButtonThemeGray);

        themeButtons = new RadioButton[]{radioButtonThemeBlue, radioButtonThemeRed, radioButtonThemeGreen, radioButtonThemePurple, radioButtonThemeGray};
        themes = new int[]{R.style.AppTheme, R.style.AppTheme_Red, R.style.AppTheme_Green, R.style.AppTheme_Purple, R.style.AppTheme_Gray};

        int currentTheme = MainActivity.settings.getThemeID();
        if (currentTheme == -1)
            currentTheme = R.style.AppTheme;

        // Find theme index
        int index = 0;
        for (int i = 0; i < themes.length; i++) {
            if (themes[i] == currentTheme) {
                index = i;
                break;
            }
        }
        themeButtons[index].setChecked(true);

        for (RadioButton btn : themeButtons)
            btn.setOnCheckedChangeListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(getString(R.string.settings));

        // Request for WRITE_EXTERNAL_STORAGE permission (required for background-images)
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

        btnChooseBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.selectImage));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (data == null)
                return;

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            MainActivity.settings.setBackgroundImagePath(picturePath);
            MainActivity.settings.saveSettings();
            Toast.makeText(this, getString(R.string.addedBackground), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            // Disable all other radio boxes
            for (RadioButton btn : themeButtons) {
                if (!btn.equals(buttonView))
                    btn.setChecked(false);
            }

            int themeIndex = themes[Arrays.asList(themeButtons).indexOf(buttonView)];
            setTheme(themeIndex);
            MainActivity.settings.setThemeID(themeIndex);
            MainActivity.settings.saveSettings();

            Toast.makeText(this, getString(R.string.restartApp), Toast.LENGTH_SHORT).show();
        }
    }
}