package com.temofey.loftcoin.data.db;

import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.Wallet;
import com.temofey.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    Flowable<List<CoinEntity>> getCoins();

    CoinEntity getCoin(String symbol);

    Flowable<List<WalletModel>> getWallets();

    void saveWallet(Wallet wallet);
}
