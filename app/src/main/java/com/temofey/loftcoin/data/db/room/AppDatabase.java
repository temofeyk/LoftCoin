package com.temofey.loftcoin.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.Wallet;

@Database(entities = {CoinEntity.class, Wallet.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();

    public abstract WalletDao walletDao();
}
