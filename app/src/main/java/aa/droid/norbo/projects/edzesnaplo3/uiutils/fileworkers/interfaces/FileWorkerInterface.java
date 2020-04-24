package aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.interfaces;

import android.net.Uri;

import java.io.InputStream;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;

public interface FileWorkerInterface {
    /**
     * Elkészíti a listát json formátumba, majd visszaadja a fájl uriját
     * @param list
     * @return
     */
    Uri makeJsonFile(List<SorozatWithGyakorlat> list, String filename);

    /**
     * Visszaadja a contentprovider-ből megnyitott inputstreamből a napló adatokat
     * sajnos ez itt T - vel nekem nem müködött
     * @param inputStream
     * @return Sorozat És Gyakorlat lista naplódátumra szürve
     */
    List<SorozatWithGyakorlat> loadJsonFile(InputStream inputStream);
}
