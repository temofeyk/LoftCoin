package com.temofey.loftcoin.screens.currencies;

import com.temofey.loftcoin.data.db.model.CoinEntity;

public interface CurrenciesAdapterListener {
    void onCurrencyClick(CoinEntity coin);
}
