package com.temofey.loftcoin.screens.currencies;

import com.temofey.loftcoin.data.db.model.CoinEntity;

public interface CurrenciesBottomSheetListener {
    void onCurrencySelected(CoinEntity coin);
}
