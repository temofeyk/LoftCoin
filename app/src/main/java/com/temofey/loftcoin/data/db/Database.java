package com.temofey.loftcoin.data.db;

import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.Wallet;
import com.temofey.loftcoin.data.db.model.WalletModel;

import com.temofey.loftcoin.data.db.model.Transaction;
import com.temofey.loftcoin.data.db.model.TransactionModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    Flowable<List<CoinEntity>> getCoins();

    CoinEntity getCoin(String symbol);

    Flowable<List<WalletModel>> getWallets();

    void saveWallet(Wallet wallet);

    void saveTransaction(List<Transaction> transactions);

    Flowable<List<TransactionModel>> getTransactions(String walletId);

}
