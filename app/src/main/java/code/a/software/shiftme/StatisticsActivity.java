package code.a.software.shiftme;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {

    private TextView txtCurrentLevelTime;
    private TextView txtCurrentLevelTimeAVG;
    private TextView txtCurrentLevelMoves;
    private TextView txtCurrentLevelMovesAVG;
    private TextView txtCurrentLevelSolved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Statistik");

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

        displayLevel(0);
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