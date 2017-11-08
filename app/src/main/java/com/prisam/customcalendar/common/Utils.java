package com.prisam.customcalendar.common;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.afollestad.materialdialogs.MaterialDialog;
import com.prisam.customcalendar.R;

/**
 * Created by vamsi on 10/12/2017.
 */

public class Utils {

    /**
     * Checks if there is a active internet connection
     * @param context
     * @return - true if there is a connection otherwise false
     */
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

    static MaterialDialog mLoadingDialog;
    public static void showLoadingDialog(Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.backgroundColor(Color.WHITE);
        builder.titleColor(context.getResources().getColor(R.color.charcoal));
        builder.contentColor(context.getResources().getColor(R.color.charcoal));
        mLoadingDialog = builder
                .content(R.string.no_network)
                .progress(true, 0)
                .autoDismiss(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    public static void hideLoadingDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }
}
