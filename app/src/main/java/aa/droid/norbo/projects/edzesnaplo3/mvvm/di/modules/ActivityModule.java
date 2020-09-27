package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.GyakorlatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {

    @Provides
    @ActivityContext
    GyakorlatViewModel provideGyakorlatViewModel(GyakorlatRepository repository) {
        return new GyakorlatViewModel(repository);
    }

    @Provides
    @ActivityContext
    ModelConverter provideModelConverter() {
        return new ModelConverter();
    }
}
