package com.temofey.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.db.model.WalletModel;
import com.temofey.loftcoin.data.db.model.TransactionModel;

import java.util.List;

public abstract class WalletsViewModel extends AndroidViewModel {

    WalletsViewModel(@NonNull Application application) {
        super(application);
    }


    public abstract void getWallets();

    public abstract void onNewWalletClick();

    public abstract void onCurrencySelected(CoinEntity coin);

    public abstract LiveData<List<WalletModel>> wallets();

    public abstract LiveData<Boolean> walletsVisible();

    public abstract LiveData<Boolean> newWalletVisible();

    public abstract LiveData<Object> selectCurrency();

    public abstract void onWalletChanged(int position);

    public abstract LiveData<List<TransactionModel>> transactions();

}
