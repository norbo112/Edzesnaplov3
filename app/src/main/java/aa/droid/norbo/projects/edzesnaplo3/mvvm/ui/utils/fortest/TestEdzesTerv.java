package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.fortest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;

public class TestEdzesTerv {
    public static EdzesTerv getEdzesTerv(String edzestervNev) {
        EdzesTerv edzesTerv = new EdzesTerv(edzestervNev);
        Edzesnap edzesnap = new Edzesnap("1.nap");

        Csoport csoport = new Csoport("Mell");
        csoport.addGyakorlat(new GyakorlatTerv("Fekvenyomás", gl(4,2), gl(10,6)));
        csoport.addGyakorlat(new GyakorlatTerv("Tárogatás fekve", gl(4,3), gl(12,8)));
        Csoport csoport2 = new Csoport("Hát");
        csoport.addGyakorlat(new GyakorlatTerv("Húzódzkodás", gl(3,2), gl(15,10)));
        csoport.addGyakorlat(new GyakorlatTerv("Egykezes evezés", gl(2,2), gl(8,6)));

        edzesnap.addCsoport(csoport);
        edzesnap.addCsoport(csoport2);
        edzesTerv.addEdzesNap(edzesnap);
        return edzesTerv;
    }

    private static List<Integer> gl(Integer... adatok) {
        return Stream.of(adatok).collect(Collectors.toList());
    }
}
