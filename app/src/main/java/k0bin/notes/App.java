package k0bin.notes;

import android.app.Application;

import com.facebook.stetho.Stetho;

import k0bin.notes.model.Database;
import k0bin.notes.util.AsyncHelper;

public class App extends Application {
    private Database db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = Database.build(this);

        AsyncHelper.init();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public Database getDb() {
        return db;
    }
}
