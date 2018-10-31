package com.temofey.loftcoin;

import android.app.Application;

import com.temofey.loftcoin.data.api.Api;
import com.temofey.loftcoin.data.api.ApiInitializer;
import com.temofey.loftcoin.data.prefs.Prefs;
import com.temofey.loftcoin.data.prefs.PrefsImpl;
import com.temofey.loftcoin.data.db.Database;
import com.temofey.loftcoin.data.db.DatabaseInitializer;
import com.temofey.loftcoin.data.db.realm.DatabaseImplRealm;

public class App extends Application {

    private Api api;
    private Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PrefsImpl(this);
        api = new ApiInitializer().init();
        new DatabaseInitializer().init(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }


    public Api getApi() {
        return api;
    }

    public Database getDatabase() {
        return new DatabaseImplRealm();
    }
}
