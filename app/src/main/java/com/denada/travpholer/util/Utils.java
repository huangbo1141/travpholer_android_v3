package com.denada.travpholer.util;

import android.content.Context;

/**
 * Created by Administrator on 12/15/2015.
 */
public class Utils
{
    public static String getSN(Context paramContext) {

        String sn = productSN(MacTools.getDefaultNetMacAddress(paramContext));
        return sn;
    }
    public static String productSN(String paramString)
    {
        //Log.i("yy", "MS=" + paramString + "=");
        String[] arrayOfString;
        int[] arrayOfInt;
        if (paramString.trim().length() == 17)
        {
            arrayOfString = paramString.split(":");
        }
        else {
            int i = paramString.trim().length();
            arrayOfString = null;
            if (i == 12){
                arrayOfString = new String[6];
                for (int j = 0; j < 6; j++)
                    arrayOfString[j] = paramString.substring(j * 2, 2 + j * 2);
            }
        }
        arrayOfInt = new int[6];
        if (arrayOfString!=null){
            for (int k = 0;k<arrayOfString.length ; k++)
            {
                arrayOfInt[k] = Integer.parseInt(arrayOfString[k].trim(), 16);
            }
            //return String.valueOf(352321536 + (arrayOfInt[2] << 24 | arrayOfInt[3] << 16 | arrayOfInt[4] << 8 | arrayOfInt[5]));
            return String.valueOf(300021500 + (arrayOfInt[2] << 24 | arrayOfInt[3] << 16 | arrayOfInt[4] << 8 | arrayOfInt[5]));
        }
        return String.valueOf(0x15000000);
        //return paramString;
    }
}
