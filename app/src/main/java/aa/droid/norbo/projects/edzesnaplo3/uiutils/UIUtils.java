package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import java.util.Scanner;
import java.util.regex.MatchResult;

public class UIUtils {
    public static String getIdopontStr(String datumstr) {
        Scanner scanner = new Scanner(datumstr);
        scanner.findWithinHorizon("(\\d+:\\d+:\\d+ )", 0);
        MatchResult matchResult = scanner.match();
        return matchResult.group();
    }

    public static String getNapDatumStr(String datumstr) {
        Scanner scanner = new Scanner(datumstr);
        scanner.findWithinHorizon("(\\w+ \\w+ \\d+ )", 0);
        MatchResult matchResult = scanner.match();
        return matchResult.group();
    }
}
