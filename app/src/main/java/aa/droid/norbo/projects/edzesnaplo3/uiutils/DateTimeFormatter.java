package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class DateTimeFormatter {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateformatter = new SimpleDateFormat("MM.dd.");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat naplodateformatter = new SimpleDateFormat("yyyy.MM.dd. EEE. HH:mm:ss");

    public static String getTime(String datetimestr) {
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

    public static String getDate(String datetimestr) {
        String date;
        try {
            long ld = Long.parseLong(datetimestr);
            Date d = new Date(ld);
            date = dateformatter.format(d);
        } catch (NumberFormatException e) {
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

    public static String getNaploListaDatum(String datetimestr) {
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
}