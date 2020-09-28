package aa.droid.norbo.projects.edzesnaplo3.mvvm.di.modules;

import android.content.Context;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.NaploDetailsRcViewAdapterFactory;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public class RCViewModule {
    @Provides
    @ActivityScoped
    NaploDetailsRcViewAdapterFactory provideNaploDetailsRcViewAdapterFactory(@ActivityContext Context context) {
        return new NaploDetailsRcViewAdapterFactory(context);
    }
}
