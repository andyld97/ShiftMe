package code.a.software.shiftme;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import controls.NumberButton;
import field.DynamicLayout;
import helpers.Helper;
import helpers.ImageHelper;
import helpers.NumberHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout rootLayout = null;
    private RelativeLayout containerLayout = null;
    private ImageView backgroundImageView = null;
    private TextView txtMoves = null;
    private TextView txtTimer = null;
    private ImageButton playButton = null;
    private ImageButton settingsButton = null;
    private ImageButton helpButton = null;
    private ImageButton aboutButton = null;
    public static Settings settings;

    private boolean isGameOver = false;
    private boolean isGameStarted = false;
    private boolean isPaused = false;

    private int currentMoves = 0;
    private int currentDim = 0;
    private final int distance = 5;
    private int[][] gameState;
    private List<Integer> combination;
    private ColorDrawable finishColor;
    private ColorDrawable buttonBackgroundColor;
    private Stopwatch gameTimer = null;
    private final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1F, 0.2F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finishColor = new ColorDrawable(getColor(R.color.colorFinishRow));
        buttonBackgroundColor = new ColorDrawable(getColor(R.color.colorTransparentBlueBackground));

        setContentView(R.layout.activity_main);

        // Initialize views
        rootLayout = findViewById(R.id.rootLayout);
        containerLayout = findViewById(R.id.containerLayout);
        backgroundImageView = findViewById(R.id.backgroundImageView);
        playButton = findViewById(R.id.btnPlay);
        playButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(this);
        helpButton = findViewById(R.id.btnHelp);
        helpButton.setOnClickListener(this);
        aboutButton = findViewById(R.id.btnAbout);
        aboutButton.setOnClickListener(this);
        txtMoves = findViewById(R.id.txtMoves);
        txtTimer = findViewById(R.id.txtTimer);

        // Create initial state if theres nothing saved
        if (savedInstanceState == null) {

            gameTimer = new Stopwatch();
            containerLayout.post(() -> {
                startNewGame(3);
            });
        }

        // Start refresh timer
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(() -> txtTimer.setText(String.format(getString(R.string.time), gameTimer.toString())));
            }
        }, 0, 500);


        settings = Settings.readSettings(this);
        refreshBackground();
    }

    private void refreshBackground()
    {
        if (!settings.getBackgroundImagePath().equals(""))
            backgroundImageView.setImageDrawable(new BitmapDrawable(getResources(), ImageHelper.loadBitmapWithEXIFOrientation(settings.getBackgroundImagePath())));
        else
            backgroundImageView.setImageResource(R.drawable.background);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isGameOver && isGameStarted && !isPaused)
            gameTimer.resume();

        settings = Settings.readSettings(this);
        refreshBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (gameTimer.isRunning() && isGameStarted && !isPaused)
            gameTimer.pause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("dim", currentDim);
        outState.putInt("moves", currentMoves);
        outState.putBoolean("isGameOver", isGameOver);
        outState.putBoolean("isGameStarted", isGameStarted);
        outState.putBoolean("isPaused", isPaused);
        outState.putIntegerArrayList("combination", (ArrayList<Integer>) combination);

        for (int i = 0; i < currentDim; i++)
            outState.putIntArray("state" + i, gameState[i]);

        gameTimer.pause();
        outState.putParcelable("timer", gameTimer);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null)
            return;

        currentDim = savedInstanceState.getInt("dim");
        currentMoves = savedInstanceState.getInt("moves");
        isGameOver = savedInstanceState.getBoolean("isGameOver");
        isPaused = savedInstanceState.getBoolean("isPaused");
        isGameStarted = savedInstanceState.getBoolean("isGameStarted");
        combination = savedInstanceState.getIntegerArrayList("combination");

        if (gameState == null)
            gameState = new int[currentDim][currentDim];

        for (int i = 0; i < currentDim; i++) {
            int[] sub = savedInstanceState.getIntArray("state" + i);
            System.arraycopy(sub,0, gameState[i],0, sub.length);
        }

        gameTimer = savedInstanceState.getParcelable("timer");
        if (!isGameOver && isGameStarted)
            gameTimer.resume();

        containerLayout.post(() -> {
            generateGameField(true);
            DisplayMoves();
        });
    }

    private void DisplayMoves()
    {
        String result = String.format(getString(R.string.moves), String.valueOf(currentMoves));
        txtMoves.setText(result);
    }

    public void resetCombination()
    {
        generateGameField(false);
        gameTimer.stop();
        gameTimer.reset();
        currentMoves = 0;
        DisplayMoves();
        isGameOver = false;
        isGameStarted = false;
        isPaused = false;
    }

    public void generateGameField(boolean restore)
    {
        int screenWidth = containerLayout.getWidth();
        int screenHeight = containerLayout.getHeight();

        // Generate layout
        final DynamicLayout result = DynamicLayout.GenerateLayout(this, currentDim, distance, screenWidth, screenHeight, buttonBackgroundColor);

        int xCounter = 0;
        int yCounter = 0;
        int coloring = -1;

        if (restore)
            coloring = Helper.calculateColoring(gameState);

        // Assign buttons
        for (int i = 0; i < currentDim * currentDim; i++)
        {
            final int currentNumber = (restore ? gameState[yCounter][xCounter++] : combination.get(i));
            if (!restore)
                gameState[yCounter][xCounter++] = currentNumber;

            final NumberButton nb = result.getButtons()[i];

            if ((i + 1) % currentDim == 0)
            {
                xCounter = 0;
                yCounter++;
            }

            if (currentNumber == 0)
                nb.setVisibility(View.INVISIBLE);
            else
                nb.setVisibility(View.VISIBLE);

            if (currentNumber <= coloring && coloring != -1)
                nb.setBackground(finishColor);
            else
                nb.setBackground(buttonBackgroundColor);

            nb.setNumber(currentNumber);
            nb.setOnClickListener(view -> {
                // Disable moving buttons if GameIsOver
                if (isGameOver)
                    return;

                if (isPaused)
                {
                    isPaused = false;
                    gameTimer.resume();
                }

                if (!gameTimer.isRunning())
                {
                    gameTimer.start();
                    isGameStarted = true;
                }

                int index = Helper.indexOf(result.getButtons(), nb);

                // Check index in matrix
                // index - 1
                // index + 1
                // index + dim
                // index - dim

                int probe = index + 1;
                int minus = index - 1;
                int plus = index + 1;

                /* Ensure that a tile cannot move (if it's at the end of the row) to the next row */
                if (probe % currentDim == 0)
                    plus = -1;

                /* Ensure that a tile cannot move (if it's at the start of the row) to the previous row */
                if (index % currentDim == 0)
                    minus = -1;

                int[] indices = new int[] { minus, plus, index - currentDim, index + currentDim};
                for (int dex : indices)
                {
                    if (dex < 0 && dex > currentDim *  currentDim - 1)
                        continue;

                    int value = Helper.getValueOfMatrixByIndex(gameState, dex);
                    if (value == 0)
                    {
                        NumberButton nbSearched = result.getButtons()[dex];
                        NumberButton nbClicked = (NumberButton)view;

                        // Swap positions on screen
                        RelativeLayout.LayoutParams nbSearchedLayoutParams = (RelativeLayout.LayoutParams)nbSearched.getLayoutParams();
                        RelativeLayout.LayoutParams nbClickedLayoutParams = (RelativeLayout.LayoutParams)nbClicked.getLayoutParams();

                        int oldLeft = nbClickedLayoutParams.leftMargin;
                        int oldTop = nbClickedLayoutParams.topMargin;

                        nbClickedLayoutParams.leftMargin = nbSearchedLayoutParams.leftMargin;
                        nbClickedLayoutParams.topMargin = nbSearchedLayoutParams.topMargin;

                        nbSearchedLayoutParams.leftMargin = oldLeft;
                        nbSearchedLayoutParams.topMargin = oldTop;

                        result.getLayout().updateViewLayout(nbSearched, nbSearchedLayoutParams);
                        result.getLayout().updateViewLayout(nbClicked, nbClickedLayoutParams);

                        // Swap in button-array
                        result.getButtons()[dex] = nbClicked;
                        result.getButtons()[index] = nbSearched;

                        // Swap in matrix
                        Helper.swapMatrix(dex, index, gameState);

                        // Refresh coloring
                        int colorAmount = Helper.calculateColoring(gameState);

                        Arrays.stream(result.getButtons()).forEach(p -> p.setBackground(buttonBackgroundColor));
                        Arrays.stream(result.getButtons()).filter(p -> p.getNumber() <= colorAmount).forEach(b -> b.setBackground(finishColor));

                        currentMoves++;
                        DisplayMoves();

                        if (Helper.isMatrix123n(gameState))
                        {
                            gameTimer.stop();
                            isGameOver = true;
                            isGameStarted = false;
                            Toast.makeText(MainActivity.this, getString(R.string.wonMessage), Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            });
        }

        // Clear and then add the view
        containerLayout.removeAllViews();
        containerLayout.addView(result.getLayout());
    }

    public void generateField()
    {
        // Reset counters
        DisplayMoves();
        currentMoves = 0;
        isGameOver = false;

        // Load combinations
        final List<List<Integer>> currentCombinations = NumberHelper.GeneratePossibleCombinations(currentDim, 100);
        Collections.shuffle(currentCombinations);

        combination = currentCombinations.get(0);
        combination.add(0);

        // Create matrix
        gameState = new int[currentDim][currentDim];

        generateGameField(false);
    }

    @Override
    public void onClick(View v)
    {
        v.startAnimation(buttonClickAnimation);

        if (v.equals(playButton))
            showPopup(v);
        else if (v.equals(helpButton))
        {
            if (!isGameOver && isGameStarted && !isPaused)
            {
                gameTimer.pause();
                isPaused = true;
            }

            Intent helpIntent = new Intent(this, HelpActivity.class);
            startActivity(helpIntent);
        }
        else if (v.equals(aboutButton))
        {
            if (!isGameOver && isGameStarted && !isPaused)
            {
                gameTimer.pause();
                isPaused = true;
            }

            Intent aboutIntent = new Intent(this, InfoActivity.class);
            startActivity(aboutIntent);
        }
        else if (v.equals(settingsButton))
        {
            if (!isGameOver && isGameStarted && !isPaused)
            {
                gameTimer.pause();
                isPaused = true;
            }

            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
    }

    private void showPopup(View v)
    {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        MenuItem mi = popup.getMenu().findItem(R.id.menuStartPause);
        if (isGameOver || !isGameStarted || isPaused)
            mi.setVisible(false);
        else if (isGameStarted)
            mi.setTitle(getString(R.string.pause));

        // Setup menu item selection
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.menuStartPause:

                    if (!isPaused)
                    {
                        isPaused = true;
                        gameTimer.pause();
                    }

                    return true;
                case R.id.menuReset:
                    resetCombination();
                    return true;
                case R.id.menu3x3:
                    startNewGame(3);
                    return true;
                case R.id.menu4x4:
                    startNewGame(4);
                    return true;
                case R.id.menu5x5:
                    startNewGame(5);
                    return true;
                case R.id.menu6x6:
                    startNewGame(6);
                    return true;
                case R.id.menu7x7:
                    startNewGame(7);
                    return true;
                case R.id.menu8x8:
                    startNewGame(8);
                    return true;
                case R.id.menu9x9:
                    startNewGame(9);
                    return true;
                case R.id.menu10x10:
                    startNewGame(10);
                    return true;
                default:
                    return false;
            }
        });


        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    private void startNewGame(int dim)
    {
        currentDim = dim;
        currentMoves = 0;
        DisplayMoves();
        gameTimer.stop();
        gameTimer.reset();
        generateField();
        isGameStarted = false;
        isGameOver = false;
        isPaused = false;
    }
}