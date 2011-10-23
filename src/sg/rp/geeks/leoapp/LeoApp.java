package sg.rp.geeks.leoapp;

import greendroid.app.GDApplication;

public class LeoApp extends GDApplication {
    @Override
    public Class<?> getHomeActivityClass() {
        return DashboardActivity.class;
    }

}
