package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DateTimeFormatter {
    @Inject
    public DateTimeFormatter() {
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timer = new SimpleDateFormat("mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat dateformatter = new SimpleDateFormat("MM.dd.");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat naplodateformatter = new SimpleDateFormat("yyyy.MM.dd. EEE. HH:mm:ss");
    private static final String TAG = DateTimeFormatter.class.getSimpleName();

    public String getTime(String datetimestr) {
        String time;
        try {
            long ld = Long.parseLong(datetimestr);
            Date d = new Date(ld);
            time = timeformatter.format(d);
        } catch (NumberFormatException e) {
            try {
                Date d = new Date(datetimestr);
                time = timeformatter.format(d);
            } catch (Exception ex) {
                Scanner scanner = new Scanner(datetimestr);
                scanner.findWithinHorizon("(\\d+:\\d+:\\d+ )", 0);
                time = scanner.match().group();
            }
        }

        return time;
    }

    public String getTime(long timestamp) {
        return timeformatter.format(new Date(timestamp));
    }

    public String getTimeStp(long timestamp) {
        return timer.format(timestamp);
    }

    public static String getDate(String datetimestr) {
        String date;
        try {
            long ld = Long.parseLong(datetimestr);
            Date d = new Date(ld);
            date = dateformatter.format(d);
        } catch (NumberFormatException e) {
            Log.i(TAG, "getDate: numbeformat");
            try {
                Date d = new Date(datetimestr);
                date = dateformatter.format(d);
            } catch (Exception ex) {
                Scanner scanner = new Scanner(datetimestr);
                scanner.findWithinHorizon("(\\D+ )(\\D+ )(\\d+ )", 0);
                MatchResult match = scanner.match();
                date = match.group(2) + match.group(3);
            }
        }
        return date;
    }

    public String getNaploListaDatum(String datetimestr) {
        String date;
        try {
            long ld = Long.parseLong(datetimestr);
            Date d = new Date(ld);
            date = naplodateformatter.format(d);
        } catch (NumberFormatException e) {
            try {
                Date d = new Date(datetimestr);
                date = naplodateformatter.format(d);
            } catch (Exception ex) {
                date = datetimestr;
            }
        }
        return date;
    }

    public String getNaploDatum(long timestamp) {
        return naplodateformatter.format(new Date(timestamp));
    }

    public String getNaploShortDatum(long naplodatum) {
        return dateformatter.format(new Date(naplodatum));
    }
}
