package k0bin.notes;

import android.app.Application;

import k0bin.notes.model.Database;

public class App extends Application {
	private Database db;

	@Override
	public void onCreate() {
		super.onCreate();

		db = Database.build(this);
		app = this;
	}

	public Database getDb() {
		return db;
	}

	private static App app;
	public static App get() {
		return app;
	}
}
