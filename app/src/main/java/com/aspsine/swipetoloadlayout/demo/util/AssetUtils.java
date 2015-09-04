package com.aspsine.swipetoloadlayout.demo.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Aspsine on 2015/4/15.
 */
public class AssetUtils {

    public static String getStringFromAsset(Context context, String filePath){
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
