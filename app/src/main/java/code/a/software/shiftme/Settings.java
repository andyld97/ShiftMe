package code.a.software.shiftme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private String backgroundImagePath = "";
    private final Activity activity;
    private UserStatistics userStatistics = new UserStatistics();


    private Settings(Activity activity)
    {
        this.activity = activity;
    }

    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

    public UserStatistics getUserStatistics() {
        return userStatistics;
    }

    public String getBackgroundImagePath()
    {
        return backgroundImagePath;
    }

    public void saveSettings()
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("background", backgroundImagePath);
        userStatistics.save(editor);
        editor.apply();
    }

    public static Settings readSettings(Activity activity)
    {
        Settings settings = new Settings(activity);
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        settings.setBackgroundImagePath(sharedPref.getString("background", ""));
        settings.userStatistics = UserStatistics.load(sharedPref);
        return settings;
    }
}