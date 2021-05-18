package field.fields;

import android.content.Context;

import controls.NumberButton;

public class ImageField extends field.ImageField {

    public ImageField(Context ctx)
    {
        super(ctx);
    }

    @Override
    public void SetShowNumbers(boolean value) {

    }

    @Override
    public void Initalize(boolean showNumbers) {

    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public NumberButton[] getNumberButtons() {
        return new NumberButton[0];
    }

    @Override
    public void SetNumberToButton(int n, int index) {

    }

    @Override
    public void GameOver() {

    }
}
