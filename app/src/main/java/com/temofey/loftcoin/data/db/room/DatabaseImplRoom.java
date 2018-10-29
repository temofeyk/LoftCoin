package com.temofey.loftcoin.data.db.room;

import com.temofey.loftcoin.data.db.Database;
import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.Wallet;
import com.temofey.loftcoin.data.db.model.WalletModel;
import com.temofey.loftcoin.data.db.model.Transaction;
import com.temofey.loftcoin.data.db.model.TransactionModel;

import java.util.List;

import io.reactivex.Flowable;

public class DatabaseImplRoom implements Database {

    private AppDatabase database;

    public DatabaseImplRoom(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().saveCoins(coins);
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return database.coinDao().getCoin(symbol);
    }

    @Override
    public Flowable<List<WalletModel>> getWallets() {
        return database.walletDao().getWallets();
    }

    @Override
    public void saveWallet(Wallet wallet) {
        database.walletDao().saveWallet(wallet);
    }

    @Override
    public void saveTransaction(List<Transaction> transactions) {
        database.walletDao().saveTransactions(transactions);
    }
    @Override
    public Flowable<List<TransactionModel>> getTransactions(String walletId) {
        return database.walletDao().getTransactions(walletId);
    }
}