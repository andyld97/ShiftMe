package helpers;

import android.content.res.Resources;
import android.util.TypedValue;
import code.a.software.shiftme.R;

public class ThemeHelper {

    public static int getThemeColor(int attr, Resources.Theme currentTheme) {
        TypedValue typedValue = new TypedValue();
        currentTheme.resolveAttribute(attr, typedValue,true);
        return typedValue.data;
    }

    public static int getSubThemeId(int appTheme) {
        if (appTheme == R.style.AppTheme_Red)
            return R.style.SubTheme_Red;
        else if (appTheme == R.style.AppTheme_Green)
            return R.style.SubTheme_Green;
        else if (appTheme == R.style.AppTheme_Purple)
            return R.style.SubTheme_Purple;
        else if (appTheme == R.style.AppTheme_Gray)
            return R.style.SubTheme_Gray;

        return R.style.SubTheme; // fallback
    }
}