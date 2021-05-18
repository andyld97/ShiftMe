package field;

import android.content.Context;
import android.widget.GridLayout;

import controls.NumberButton;

public abstract class Field extends GridLayout {

    public Field(Context ctx)
    {
        super(ctx);
    }

    public abstract int getDimension();

    public abstract NumberButton[] getNumberButtons();

    public abstract void SetNumberToButton(int n, int index);

    public abstract void GameOver();
}
