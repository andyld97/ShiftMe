package code.a.software.shiftme;

import android.content.SharedPreferences;

public class UserStatistics {
    private Statistics totalStatistics = new Statistics();

    private Statistics levelStatistics[] = {new Statistics(), new Statistics(), new Statistics(), new Statistics(), new Statistics(), new Statistics(), new Statistics(), new Statistics()};

    public static UserStatistics load(SharedPreferences preferences) {
        UserStatistics userStatistics = new UserStatistics();

        userStatistics.totalStatistics = Statistics.load("total", preferences);
        for (int i = 0; i < userStatistics.levelStatistics.length; i++) {
            userStatistics.setLevelStatistics(i, Statistics.load("level" + i, preferences));
        }

        return userStatistics;
    }

    public Statistics getTotalStatistics() {
        return totalStatistics;
    }

    public Statistics getLevelStatistics(int level) {
        return levelStatistics[level];
    }

    public void setLevelStatistics(int level, Statistics statistics) {
        levelStatistics[level] = statistics;
    }

    public void save(SharedPreferences.Editor editor) {
        totalStatistics.save("total", editor);
        for (int i = 0; i < levelStatistics.length; i++) {
            levelStatistics[i].save("level" + i, editor);
        }
    }
}
