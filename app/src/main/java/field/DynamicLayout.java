package field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import controls.NumberButton;
import helpers.Helper;

public class DynamicLayout
{
    private ViewGroup layout;
    private NumberButton[] buttons;

    public ViewGroup getLayout()
    {
        return layout;
    }

    public void setLayout(ViewGroup layout)
    {
        this.layout = layout;
    }

    public NumberButton[] getButtons()
    {
        return buttons;
    }

    public void setButtons(NumberButton[] buttons)
    {
        this.buttons = buttons;
    }

    public static DynamicLayout GenerateLayout(Context ctx, int dimension, int distance, int screenWidth, int screenHeight, ColorDrawable defaultBackground)
    {
        RelativeLayout rootView = new RelativeLayout(ctx);
        List<NumberButton> buttons = new ArrayList<>();

        int count = dimension * dimension;
        int buttonWidth =  Math.min((int)((screenWidth - (dimension * distance)) / (double)dimension), (int)((screenHeight - (dimension * distance)) / (double)dimension));
        int buttonHeight = buttonWidth; // square

        int y = 0;
        int x = 0;

        for (int b = 0; b < count; b++)
        {
            NumberButton btn = new NumberButton(ctx, defaultBackground);
            buttons.add(btn);
            int value = b + 1;
            btn.setText(String.valueOf(value));
            btn.setWidth(buttonWidth);

            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);

            param.leftMargin = (x * buttonWidth) + (x * distance);
            param.topMargin = y;

            if (value % dimension == 0)
            {
                y += buttonWidth + distance;
                x = 0;
            }
            else {
                x++;
            }

            rootView.addView(btn, param);
        }

        DynamicLayout dl = new DynamicLayout();
        dl.setButtons(Helper.convert(buttons));
        dl.setLayout(rootView);

        return dl;
    }
}
