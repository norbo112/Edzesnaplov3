package aa.droid.norbo.projects.edzesnaplo3.uiutils.lists;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

import aa.droid.norbo.projects.edzesnaplo3.NaploActivity;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

public class SorozatSorter {
    public static void rcGyakSorozatSort(List<NaploActivity.RCViewGyakSorozat> sorozats) {
        NaploActivity.RCViewGyakSorozat[] list = sorozats.toArray(new NaploActivity.RCViewGyakSorozat[0]);
        boolean csere;
        int i = list.length - 1;
        do {
            csere = false;
            for (int j = 0; j < i; j++) {
                if(isSorozatBefore(list[j], list[j+1]) > 0) {
                    NaploActivity.RCViewGyakSorozat tmp = list[j];
                    list[j] = list[j+1];
                    list[j+1] = tmp;
                    csere = true;
                }
            }
            i--;
        } while (csere);

        sorozats.clear();
        for (int k = 0; k < list.length; k++) {
            sorozats.add(list[k]);
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return remélhetőleg sosem egyezik a két időpont, 1 ha a nagyobb mint b, -1 ha a kisebb mint b
     */
    private static int isSorozatBefore(NaploActivity.RCViewGyakSorozat a, NaploActivity.RCViewGyakSorozat b) {
        try {
            int[] a_time;
            int[] b_time;
            Scanner scanner = new Scanner(a.getSorozatList().get(a.getSorozatList().size() - 1).getIsmidopont());
            scanner.findWithinHorizon("(\\d+:)(\\d+:)(\\d+)", 0);
            MatchResult match = scanner.match();
            scanner = new Scanner(b.getSorozatList().get(0).getIsmidopont());
            scanner.findWithinHorizon("(\\d+:)(\\d+:)(\\d+)", 0);
            MatchResult match2 = scanner.match();
            a_time = new int[]{
                    Integer.parseInt(match.group(1).substring(0, match.group(1).lastIndexOf(':'))),
                    Integer.parseInt(match.group(2).substring(0, match.group(1).lastIndexOf(':'))),
                    Integer.parseInt(match.group(3))};
            b_time = new int[]{
                    Integer.parseInt(match2.group(1).substring(0, match2.group(1).lastIndexOf(':'))),
                    Integer.parseInt(match2.group(2).substring(0, match2.group(1).lastIndexOf(':'))),
                    Integer.parseInt(match2.group(3))};
            for (int i = 0; i < a_time.length; i++) {
                if (a_time[i] == b_time[i]) continue;
                if (a_time[i] > b_time[i]) return 1;
                else return -1;
            }
        } catch (Exception e) {
            //feltételezem hogy akkor longba lett megadva
            Date a_date = new Date(Long.parseLong(a.getSorozatList().get(0).getIsmidopont()));
            Date b_date = new Date(Long.parseLong(b.getSorozatList().get(b.getSorozatList().size()-1).getIsmidopont()));
            if(a_date.after(b_date)) return 1;
            else return -1;
        }
        return 0;
    }

    public static void intsShorter(int[] nums) {
        boolean csere;
        int i = nums.length - 1;
        do {
            csere = false;
            for (int j = 0; j < i; j++) {
                if(nums[j] > nums[j+1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = tmp;
                    csere = true;
                }
            }
            i--;
        } while (csere);
    }

    public static void rcSorozatSorter(List<Sorozat> sorozats) {
        Sorozat[] list = sorozats.toArray(new Sorozat[0]);
        boolean csere;
        int i = list.length - 1;
        do {
            csere = false;
            for (int j = 0; j < i; j++) {
                if(isDateAfter(list[j].getIsmidopont(), list[j+1].getIsmidopont()) > 0) {
                    Sorozat tmp = list[j];
                    list[j] = list[j+1];
                    list[j+1] = tmp;
                    csere = true;
                }
            }
            i--;
        } while (csere);

        sorozats.clear();
        for (int k = 0; k < list.length; k++) {
            sorozats.add(list[k]);
        }
    }

    public static int isDateAfter(String pA, String pB) {
        Date a = new Date(Long.parseLong(pA));
        Date b = new Date(Long.parseLong(pB));

        if(a.after(b)) {
            return 1;
        } else if(a.before(b)) {
            return -1;
        } else {
            return 0;
        }
    }
}
