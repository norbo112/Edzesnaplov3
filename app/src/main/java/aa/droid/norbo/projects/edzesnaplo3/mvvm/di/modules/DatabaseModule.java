package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.GyakorlatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.LocaleGyakorlatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.utils.AdatFeltoltes;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {
    private static final int EXECUTOR_THREAD_NUMBERS = 5;

    @Singleton
    @Provides
    EdzesNaploDatabase getDatabase(Application application) {
        return Room.databaseBuilder(application,
                EdzesNaploDatabase.class, EdzesNaploDatabase.DBNAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    ExecutorService provideExecutorService() {
        return Executors.newFixedThreadPool(EXECUTOR_THREAD_NUMBERS);
    }

    @Singleton
    @Provides
    AdatFeltoltes provideAdatFeltoltes(ExecutorService executorService, EdzesNaploDatabase edzesNaploDatabase, @ApplicationContext Context context) {
        return new AdatFeltoltes(executorService, edzesNaploDatabase, context);
    }

    @Singleton
    @Provides
    GyakorlatRepository provideGyakorlatRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        return new LocaleGyakorlatRepository(database, executorService);
    }
}
