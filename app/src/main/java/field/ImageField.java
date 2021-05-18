package field;

import android.content.Context;

public abstract class ImageField extends Field
{
    public ImageField(Context ctx)
    {
        super(ctx);
    }

    public abstract void SetShowNumbers(boolean value);

    public abstract void Initalize (boolean showNumbers);
}
