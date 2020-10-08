package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.converters;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListOfIntegers {
    @TypeConverter
    public static List<Integer> fromStringlist(String listofintegers) {
        String[] split = listofintegers.split(",");
        List<Integer> szamok = new ArrayList<>();
        for(String s: split) {
            szamok.add(Integer.parseInt(s));
        }
        return szamok;
    }

    @TypeConverter
    public static String toStringlist(List<Integer> szamok) {
        StringBuilder sb = new StringBuilder();
        for(Integer i: szamok) {
            sb.append(i).append(",");
        }
        return sb.toString().substring(0, sb.toString().lastIndexOf(","));
    }
}
