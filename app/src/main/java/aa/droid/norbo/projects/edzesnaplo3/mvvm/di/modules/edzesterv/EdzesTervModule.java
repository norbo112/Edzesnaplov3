package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules.edzesterv;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesTervDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class EdzesTervModule {
    @Singleton
    @Provides
    EdzesTervDatabase provideEdzesTervDatabase(Application application) {
        return Room.databaseBuilder(application, EdzesTervDatabase.class, EdzesTervDatabase.DBNAME).build();
    }
}
