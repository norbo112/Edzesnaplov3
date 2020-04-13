package aa.droid.norbo.projects.edzesnaplo3.uiutils.lists;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

import aa.droid.norbo.projects.edzesnaplo3.NaploActivity;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;

public class SorozatSorter {
    public static void rcGyakSorozatSort(List<NaploActivity.RCViewGyakSorozat> sorozats) {
        NaploActivity.RCViewGyakSorozat[] list = sorozats.toArray(new NaploActivity.RCViewGyakSorozat[0]);
        System.out.println(Arrays.toString(list));
        for (int i = 0; i < list.length-1; i++) {
            if(isSorozatBefore(list[i], list[i+1]) > 0) {
                NaploActivity.RCViewGyakSorozat tmp = list[i];
                list[i] = list[i+1];
                list[i+1] = tmp;
            }
        }

        System.out.println("Rendezés után:");
        System.out.println(Arrays.toString(list));
        sorozats.clear();
        for (int i = 0; i < list.length; i++) {
            sorozats.add(list[i]);
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
            scanner = new Scanner(b.getSorozatList().get(b.getSorozatList().size() - 1).getIsmidopont());
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
            System.out.println();
            System.out.println("A="+ Arrays.toString(a_time) +" B="+ Arrays.toString(b_time));
            System.out.println();
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

    public static void sorozatSortByIsmIdo(List<SorozatWithGyakorlat> sorozats) {
        SorozatWithGyakorlat[] list = sorozats.toArray(new SorozatWithGyakorlat[0]);
        for (int i = 0; i < list.length-1; i++) {
            if(isSorozatBefore(list[i], list[i+1]) > 0) {
                SorozatWithGyakorlat tmp = list[i];
                list[i] = list[i+1];
                list[i+1] = tmp;
            }
        }

        sorozats.clear();
        for (int i = 0; i < list.length; i++) {
            sorozats.add(list[i]);
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return remélhetőleg sosem egyezik a két időpont, 1 ha a nagyobb mint b, -1 ha a kisebb mint b
     */
    private static int isSorozatBefore(SorozatWithGyakorlat a, SorozatWithGyakorlat b) {
        try {
            int[] a_time;
            int[] b_time;
            Scanner scanner = new Scanner(a.sorozat.getIsmidopont());
            scanner.findWithinHorizon("(\\d+:)(\\d+:)(\\d+)", 0);
            MatchResult match = scanner.match();
            scanner = new Scanner(b.sorozat.getIsmidopont());
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
            Date a_date = new Date(Long.parseLong(a.sorozat.getIsmidopont()));
            Date b_date = new Date(Long.parseLong(b.sorozat.getIsmidopont()));
            if(a_date.after(b_date)) return 1;
            else return -1;
        }
        return 0;
    }
}
