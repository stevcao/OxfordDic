package com.clz.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayUtils
{
  public static float convertDpToPixel(Context context, float dp)
  {
    return dp * (context.getResources().getDisplayMetrics().densityDpi / 160.0F);
  }
}


/* Location:           C:\Users\stevcao\Desktop\OxfordDic\classes-dex2jar.jar
 * Qualified Name:     com.clz.util.DisplayUtils
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */