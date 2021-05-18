package field.fields;

import android.content.Context;
import controls.NumberButton;
import field.Field;

public class ButtonField extends Field
{
    private int dimension = 0;
    private NumberButton[] btns = null;

    public ButtonField(Context ctx, int dimensions, NumberButton[] btns)
    {
        super(ctx);
        this.dimension = dimensions;
        this.btns = btns;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public NumberButton[] getNumberButtons() {
        return btns;
    }

    @Override
    public void SetNumberToButton(int n, int index) {
        NumberButton nb = getNumberButtons()[index];
        nb.setText(n + "");
        nb.setNumber(n);
    }

    @Override
    public void GameOver() {
        // Do nothing here
    }
}
