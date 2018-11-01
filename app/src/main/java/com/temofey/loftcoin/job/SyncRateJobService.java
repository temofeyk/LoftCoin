package com.temofey.loftcoin.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.temofey.loftcoin.App;
import com.temofey.loftcoin.R;
import com.temofey.loftcoin.data.api.Api;
import com.temofey.loftcoin.data.db.Database;
import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.CoinEntityMapper;
import com.temofey.loftcoin.data.db.model.QuoteEntity;
import com.temofey.loftcoin.data.model.Fiat;
import com.temofey.loftcoin.data.prefs.Prefs;
import com.temofey.loftcoin.screens.main.MainActivity;
import com.temofey.loftcoin.utils.CurrencyFormatter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncRateJobService extends JobService {
    private static final String TAG = "SyncRateJobService";

    public static final String EXTRA_SYMBOL = "symbol";

    private static final String NOTIFICATION_CHANNEL_RATE_CHANGED = "RATE_CHANGED";
    private static final int NOTIFICATION_ID_RATE_CHANGED = 10;

    private Api api;
    private Database database;
    private Prefs prefs;
    private CoinEntityMapper mapper;
    private CurrencyFormatter formatter;

    private Disposable disposable;

    private String symbol = "BTC";

    @Override
    public void onCreate() {
        super.onCreate();

        api = ((App) getApplication()).getApi();
        database = ((App) getApplication()).getDatabase();
        prefs = ((App) getApplication()).getPrefs();

        mapper = new CoinEntityMapper();
        formatter = new CurrencyFormatter();

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: current thread  = " + Thread.currentThread().getName());
        doJob(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: ");
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        return false;
    }

    private void doJob(JobParameters params) {
        symbol = params.getExtras().getString(EXTRA_SYMBOL, "BTC");

        disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(response -> mapper.mapCoins(response.data))
                .subscribe(
                        coinEntities -> {
                            handleCoins(coinEntities);
                            jobFinished(params, false);
                        },

                        error -> {
                            handleError(error);
                            jobFinished(params, false);
                        }
                );
    }

    private void handleError(Throwable throwable) {
        Log.e(TAG, "Failure to sync bitcoin rate", throwable);
    }

    private void handleCoins(List<CoinEntity> newCoins) {
        Log.i(TAG, "handleCoin: ");

        database.open();

        Fiat fiat = prefs.getFiatCurrency();


        CoinEntity oldCoin = database.getCoin(symbol);
        CoinEntity newCoin = findCoin(newCoins, symbol);

        if (oldCoin != null && newCoin != null) {
            QuoteEntity oldQuote = oldCoin.getQuote(fiat);
            QuoteEntity newQuote = newCoin.getQuote(fiat);

            if (newQuote.price != oldQuote.price) {
                Log.i(TAG, "Price is changed: ");

                double priceDiff = newQuote.price - oldQuote.price;

                String priceDiffString;
                String price = formatter.format(Math.abs(priceDiff), false);

                if (priceDiff > 0) {
                    priceDiffString = "+ " + price + " " + fiat.symbol;
                } else {
                    priceDiffString = "- " + price + " " + fiat.symbol;
                }


                showRateChangedNotification(newCoin, priceDiffString);
            } else {
                Log.i(TAG, "Price not changed: ");
            }
        }


        database.saveCoins(newCoins);

        database.close();
    }

    private CoinEntity findCoin(List<CoinEntity> newCoins, String symbol) {
        for (CoinEntity coin : newCoins) {
            if (coin.symbol.equals(symbol)) {
                return coin;
            }
        }
        return null;
    }

    private void showRateChangedNotification(CoinEntity newCoin, String priceDiff) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_RATE_CHANGED);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(newCoin.name);
        builder.setContentText(getString(R.string.notification_rate_changed_body, priceDiff));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setLights(Color.RED, 200, 300);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_RATE_CHANGED,
                    getString(R.string.notification_channel_rate_changed),
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(newCoin.symbol, NOTIFICATION_ID_RATE_CHANGED, notification);

    }

}
