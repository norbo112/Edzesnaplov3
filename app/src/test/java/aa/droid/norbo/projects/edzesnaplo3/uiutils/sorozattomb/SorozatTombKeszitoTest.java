package aa.droid.norbo.projects.edzesnaplo3.uiutils.sorozattomb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SorozatTombKeszitoTest {
    private SorozatTombKeszito underTest;

    @Parameterized.Parameter(value = 0)
    public String[] params;
    @Parameterized.Parameter(value = 1)
    public long expected;

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {new String[]{"1587316360048","1587316583403"}, 5},
                {new String[]{"1587316760236","1587317144758"}, 6},
                {new String[]{"1587317272674","1587317738030"}, 7},
                {new String[]{"1587319094929","1587319540632"}, 7},
        });
    }

    @Before
    public void setUp() {
        underTest = new SorozatTombKeszito();
    }

    @Test
    public void GetElteltIdoSzamitas_jo_ertekkel_ter_vissza() {
        long actual = underTest.getEltelIdoSzamitas(params);
        Assert.assertEquals(expected, actual);
    }
}
