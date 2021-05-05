package com.youniform.android.BaseClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.youniform.android.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static String BASE_URL = "https://ndapak.org/uniform/wp-json/";
    public static String SECOND_BASE_URL = "https://ndapak.org/uniform/wp-json/bdpwr/v1/";
    public static String THIRD_BASE_URL = "https://ndapak.org/uniform/API/";
    public static String LOGIN_TYPE = "";
    public static int LOGIN_ID = 0;
    public static int _ID = 0;
    public static int CAT_ID = 0;
    public static int SUB_ID = 0;
    public static int POSITION = 0;

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) dialog.show();
    }

    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static void showToastMessage(String message, Context context) {
        if (message == null || message.equals("")) return;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void changeFormate(TextView textView, String starttime) {
//       provided format  2019-06-10T16:00:00
//       provided result  Mon 10 Jun 2019 04:00 PM
        if (starttime != null && !starttime.equals("")) {

            String dateStr = starttime;
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat writeFormat = new SimpleDateFormat("EEE dd MMM yyyy  hh:mm a");
            readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = readFormat.parse(dateStr);
                String formattedDate = "";
                if (date != null) {
                    formattedDate = writeFormat.format(date);
                }

                System.out.println(formattedDate);
                System.out.println("Date: " + formattedDate.substring(0, formattedDate.indexOf(' ')));
                System.out.println("Time: " + formattedDate.substring(formattedDate.indexOf(' ') + 1));
//            String day =new SimpleDateFormat("EEEE").format(starttime);
                textView.setText(formattedDate.substring(0, formattedDate.indexOf(' ')) + " " + formattedDate.substring(formattedDate.indexOf(' ') + 1));
            } catch (ParseException e) {
                e.printStackTrace();
                textView.setText("-");
            }
        }
    }


    public static void SlideUP(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_down));
    }

    public static void SlideDown(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_up));
    }


}
