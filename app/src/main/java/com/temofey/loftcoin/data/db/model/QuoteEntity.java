package com.temofey.loftcoin.data.db.model;

import io.realm.RealmObject;

public class QuoteEntity extends RealmObject {

    public double price;

    float percentChange1h;

    public float percentChange24h;

    float percentChange7d;

}
