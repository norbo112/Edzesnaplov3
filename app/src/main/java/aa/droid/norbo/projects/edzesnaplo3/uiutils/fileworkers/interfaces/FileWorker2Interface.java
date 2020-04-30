package aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.interfaces;

import android.net.Uri;

import java.io.InputStream;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.models.GyakorlatCsomag;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.models.NaploAll;

public interface FileWorker2Interface {
    Uri makeFile(List<SorozatWithGyakorlat> sorozatWithGyakorlats, Naplo naplom);

    NaploAll loadFile(InputStream inputStream);
}
