package code.a.software.shiftme;

import android.graphics.Color;

public class Helper {
    /**
     * Macht eine Farbe dunkler um einen bestimmten Faktor.
     * @param color Die Originalfarbe (ARGB)
     * @param factor Dunklerungsfaktor: 0.0 = schwarz, 1.0 = Originalfarbe
     * @return Die dunklere Farbe (ARGB)
     */
    public static int darkenColor(int color, float factor) {
        int alpha = Color.alpha(color);
        int red   = (int)(Color.red(color) * factor);
        int green = (int)(Color.green(color) * factor);
        int blue  = (int)(Color.blue(color) * factor);

        // Clamp auf 0-255, falls nötig
        red   = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue  = Math.max(0, Math.min(255, blue));

        return Color.argb(alpha, red, green, blue);
    }

}
