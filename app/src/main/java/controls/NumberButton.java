package controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatButton;

public class NumberButton extends androidx.appcompat.widget.AppCompatButton {

    private int number = 0;

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int n)
    {
        this.number = n;
        this.setText(number + "");
    }

    public NumberButton(Context ctx, ColorDrawable background)
    {
        super(ctx);
        setBackground(background);
        setAutoSizeTextTypeUniformWithConfiguration(5,50,1, TypedValue.COMPLEX_UNIT_SP);
    }
}
