package com.temofey.loftcoin.screens.main.rate;

import com.temofey.loftcoin.data.model.Fiat;

public interface RatePresenter {

    void attachView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);

    void onRateLongClick(String symbol);

}
