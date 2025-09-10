package code.a.software.shiftme;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import helpers.ThemeHelper;

public class StatisticsActivity extends AppCompatActivity {

    private TextView txtCurrentLevelTime;
    private TextView txtCurrentLevelTimeAVG;
    private TextView txtCurrentLevelMoves;
    private TextView txtCurrentLevelMovesAVG;
    private TextView txtCurrentLevelSolved;

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

        setContentView(R.layout.activity_statistics);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        setTitle(getString(R.string.statistics));

        UserStatistics userStatistics = MainActivity.settings.getUserStatistics();

        TextView txtTotalMoves = findViewById(R.id.txtTotalMoves);
        txtTotalMoves.setText(String.valueOf(userStatistics.getTotalStatistics().getMoves()));

        TextView txtTotalTime = findViewById(R.id.txtTotalTimeValue);
        txtTotalTime.setText(formatTime(userStatistics.getTotalStatistics().getTotalTimePlayed()));

        TextView txtTotalSolvedPuzzles = findViewById(R.id.txtTotalSolvedPuzzles);
        txtTotalSolvedPuzzles.setText(String.valueOf(userStatistics.getTotalStatistics().getPuzzlesSolved()));

        txtCurrentLevelTime = findViewById(R.id.txtCurrentLevelTime);
        txtCurrentLevelTimeAVG = findViewById(R.id.txtCurrentLevelTimeAVG);
        txtCurrentLevelMoves = findViewById(R.id.txtCurrentLevelMoves);
        txtCurrentLevelMovesAVG = findViewById(R.id.txtCurrentLevelMovesAVG);
        txtCurrentLevelSolved = findViewById(R.id.txtCurrentLevelSolved);

        Spinner spinner = findViewById(R.id.spinnerSelectLevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level_items, R.layout.spinner_list);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayLevel(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int index = MainActivity.settings.getDefaultDimension() - 3;
        spinner.setSelection(index);
        displayLevel(index);
    }


    private void displayLevel(int spinnerIndex) {
        Statistics statistics = MainActivity.settings.getUserStatistics().getLevelStatistics(spinnerIndex);

        txtCurrentLevelTime.setText(formatTime(statistics.getTotalTimePlayed()));
        txtCurrentLevelTimeAVG.setText(formatTime(statistics.getAvgTimePerLevel()));
        txtCurrentLevelMoves.setText(String.valueOf(statistics.getMoves()));
        txtCurrentLevelMovesAVG.setText(String.valueOf(statistics.getAvgMovesPerLevel()));
        txtCurrentLevelSolved.setText(String.valueOf(statistics.getPuzzlesSolved()));
    }

    public String formatTime(long seconds) {
        long minutes = (seconds / 60) % 60;
        long hours = (seconds / 60) / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }
}