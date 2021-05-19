package code.a.software.shiftme;

import android.content.SharedPreferences;

public class Statistics {

    private int puzzlesSolved = 0;
    private long totalTimePlayed = 0;
    private long moves = 0;

    public long getPuzzlesSolved() {
        return puzzlesSolved;
    }

    public long getTotalTimePlayed() {
        return totalTimePlayed;
    }

    public long getAvgTimePerLevel() {
        if (puzzlesSolved == 0)
            return 0;
        return totalTimePlayed / puzzlesSolved;
    }

    public long getAvgMovesPerLevel() {
        if (puzzlesSolved == 0)
            return 0;

        return moves / puzzlesSolved;
    }

    public long getMoves() {
        return moves;
    }

    public void incrementPuzzlesSolved(long n) {
        puzzlesSolved += n;
    }

    public void incrementTotalTimePlayed(long n) {
        totalTimePlayed += n;
    }

    public void incrementMoves(long n) {
        moves += n;
    }

    public static Statistics load(String prefix, SharedPreferences preferences) {
        Statistics statistics = new Statistics();
        statistics.incrementPuzzlesSolved(preferences.getLong(prefix + "puzzlesSolved", 0));
        statistics.incrementTotalTimePlayed(preferences.getLong(prefix + "totalTimePlayed", 0));
        statistics.incrementMoves(preferences.getLong(prefix + "moves", 0));
        return statistics;
    }

    public void save(String prefix, SharedPreferences.Editor editor) {
        editor.putLong(prefix + "puzzlesSolved",  puzzlesSolved);
        editor.putLong(prefix + "totalTimePlayed", totalTimePlayed);
        editor.putLong(prefix + "moves", moves);
    }
}
