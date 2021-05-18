package code.a.software.shiftme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private String backgroundImagePath = "";
    private Activity activity;


    private Settings(Activity activity)
    {
        this.activity = activity;
    }

    public void setBackgroundImagePath(String backgroundImagePath)
    {
        this.backgroundImagePath = backgroundImagePath;
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
        editor.apply();
    }


    public static Settings readSettings(Activity activity)
    {
        Settings settings = new Settings(activity);
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        settings.setBackgroundImagePath(sharedPref.getString("background", ""));
        return settings;
    }

}
