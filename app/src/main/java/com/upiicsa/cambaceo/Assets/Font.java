package com.upiicsa.cambaceo.Assets;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by luis.perez on 11/10/2016.
 */

public class Font {
    public Typeface setAsset(Context context)
    {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "ABEAKRG.TTF");
        return font;
    }
}
