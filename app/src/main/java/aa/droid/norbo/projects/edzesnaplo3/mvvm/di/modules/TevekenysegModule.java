package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.SorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.LocalNaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.LocalSorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class TevekenysegModule {

    @Provides
    @Singleton
    NaploRepository naploRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        return new LocalNaploRepository(database, executorService);
    }

    @Provides
    @Singleton
    SorozatRepository sorozatRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        return new LocalSorozatRepository(database, executorService);
    }
}
