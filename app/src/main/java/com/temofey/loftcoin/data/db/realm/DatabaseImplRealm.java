package com.temofey.loftcoin.data.db.realm;

import com.temofey.loftcoin.data.db.Database;
import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.Transaction;
import com.temofey.loftcoin.data.db.model.Wallet;

import java.util.List;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DatabaseImplRealm implements Database {

    private Realm realm;

    @Override
    public void open() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void close() {
        realm.close();
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(coins);
        realm.commitTransaction();
    }

    @Override
    public void saveWallet(Wallet wallet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(wallet);
        realm.commitTransaction();
    }

    @Override
    public void saveTransaction(List<Transaction> transactions) {
        realm.beginTransaction();
        realm.copyToRealm(transactions);
        realm.commitTransaction();
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {

        Flowable<RealmResults<CoinEntity>> flowable = realm.where(CoinEntity.class)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);
        //noinspection unchecked
        return (Flowable) flowable;
    }

    @Override
    public Flowable<List<Wallet>> getWallets() {
        Flowable<RealmResults<Wallet>> flowable = realm.where(Wallet.class)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);
        //noinspection unchecked
        return (Flowable) flowable;
    }

    @Override
    public Flowable<List<Transaction>> getTransactions(String walletId) {
        Flowable<RealmResults<Transaction>> flowable = realm.where(Transaction.class)
                .equalTo("walletId", walletId)
                .findAll()
                .sort("date", Sort.DESCENDING)
                .asFlowable()
                .filter(RealmResults::isLoaded);
        //noinspection unchecked
        return (Flowable) flowable;
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return realm.where(CoinEntity.class).equalTo("symbol", symbol).findFirst();
    }
}
