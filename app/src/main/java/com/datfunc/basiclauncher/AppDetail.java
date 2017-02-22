package com.datfunc.basiclauncher;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by dela on 2/17/17.
 */

public class AppDetail {
    CharSequence label;
    CharSequence name;
    Drawable icon;


    public CharSequence getName()
    {
        return name;
    }


    public CharSequence getLabel()
    {
        return label;
    }
}
