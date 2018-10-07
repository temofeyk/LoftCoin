package com.temofey.loftcoin.screens.welcome;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class WelcomePage implements Parcelable {

    @DrawableRes
    private int icon;

    @StringRes
    private int title;

    @StringRes
    private int subtitle;

    WelcomePage(int icon, int title, int subtitle) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    private WelcomePage(Parcel in) {
        icon = in.readInt();
        title = in.readInt();
        subtitle = in.readInt();
    }

    public static final Creator<WelcomePage> CREATOR = new Creator<WelcomePage>() {
        @Override
        public WelcomePage createFromParcel(Parcel in) {
            return new WelcomePage(in);
        }

        @Override
        public WelcomePage[] newArray(int size) {
            return new WelcomePage[size];
        }
    };

    public int getIcon() {
        return icon;
    }

    public int getTitle() {
        return title;
    }

    public int getSubtitle() {
        return subtitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeInt(title);
        parcel.writeInt(subtitle);
    }
}
