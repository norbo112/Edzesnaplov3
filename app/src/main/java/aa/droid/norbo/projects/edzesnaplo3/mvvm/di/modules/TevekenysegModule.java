package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules;

import java.util.concurrent.ExecutorService;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.SorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.LocalNaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.LocalSorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public class TevekenysegModule {

    @Provides
    @ActivityScoped
    NaploRepository naploRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        return new LocalNaploRepository(database, executorService);
    }

    @Provides
    @ActivityScoped
    SorozatRepository sorozatRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        return new LocalSorozatRepository(database, executorService);
    }
}
