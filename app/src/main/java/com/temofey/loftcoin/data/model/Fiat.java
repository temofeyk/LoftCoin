package com.temofey.loftcoin.data.model;

public enum Fiat {
    USD("$"),
    EUR("€"),
    RUB("₽");

    public String symbol;

    Fiat(String symbol) {
        this.symbol = symbol;
    }
}
