package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.GyakorlatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.SorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class ActivityModule {
    private static final String PREF_NAME = "beallitasok";

    @Provides
    @Singleton
    GyakorlatViewModel provideGyakorlatViewModel(GyakorlatRepository repository) {
        return new GyakorlatViewModel(repository);
    }

    @Provides
    @Singleton
    SorozatViewModel provideSorozatViewModel(SorozatRepository repository) {
        return new SorozatViewModel(repository);
    }

    @Provides
    @Singleton
    ModelConverter provideModelConverter() {
        return new ModelConverter();
    }

    @Provides
    @Singleton
    DateTimeFormatter dateTimeFormatter() {
        return new DateTimeFormatter();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
