package com.temofey.loftcoin.screens.main.wallets.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.temofey.loftcoin.R;
import com.temofey.loftcoin.data.db.model.QuoteEntity;
import com.temofey.loftcoin.data.db.model.Wallet;
import com.temofey.loftcoin.data.model.Currency;
import com.temofey.loftcoin.data.model.Fiat;
import com.temofey.loftcoin.data.prefs.Prefs;
import com.temofey.loftcoin.utils.CurrencyFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletsPagerAdapter extends PagerAdapter {

    private static final String TAG = "WalletsPagerAdapter";

    private List<Wallet> wallets = Collections.emptyList();

    private Prefs prefs;

    public WalletsPagerAdapter(Prefs prefs) {
        this.prefs = prefs;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return wallets.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.item_wallet, container, false);


        WalletViewHolder viewHolder = new WalletViewHolder(v, prefs);
        viewHolder.bind(wallets.get(position));

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    static class WalletViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.symbol_text)
        TextView symbolText;

        @BindView(R.id.symbol_icon)
        ImageView symbolIcon;

        @BindView(R.id.currency)
        TextView currency;

        @BindView(R.id.primary_amount)
        TextView primaryAmount;

        @BindView(R.id.secondary_amount)
        TextView secondaryAmount;


        private Random random = new Random();

        private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

        private Context context;

        private Prefs prefs;

        private int[] colors;

        WalletViewHolder(View itemView, Prefs prefs) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            context = itemView.getContext();
            this.prefs = prefs;
            this.colors = context.getResources().getIntArray(R.array.CryptoCurrenciesColors);
        }

        void bind(Wallet wallet) {
            bindCurrency(wallet);
            bindSymbol(wallet);
            bindPrimaryAmount(wallet);
            bindSecondaryAmount(wallet);
        }

        private void bindCurrency(Wallet wallet) {
            currency.setText(wallet.coin.symbol);
        }


        private void bindSymbol(Wallet wallet) {
            Currency currency = Currency.getCurrency(wallet.coin.symbol);

            if (currency != null) {
                symbolIcon.setVisibility(View.VISIBLE);
                symbolText.setVisibility(View.GONE);

                symbolIcon.setImageResource(currency.iconRes);
            } else {
                symbolIcon.setVisibility(View.GONE);
                symbolText.setVisibility(View.VISIBLE);

                Drawable background = symbolText.getBackground();
                Drawable wrapped = DrawableCompat.wrap(background);
                DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);

                symbolText.setText(String.valueOf(wallet.coin.symbol.charAt(0)));
            }
        }

        private void bindPrimaryAmount(Wallet wallet) {
            String value = currencyFormatter.format(wallet.amount, true);
            primaryAmount.setText(itemView.getContext().getString(R.string.currency_amount, value, wallet.coin.symbol));
        }

        private void bindSecondaryAmount(Wallet wallet) {

            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quote = wallet.coin.getQuote(fiat);

            double amount = wallet.amount * quote.price;
            String value = currencyFormatter.format(amount, false);

            secondaryAmount.setText(itemView.getContext().getString(R.string.currency_amount, value, fiat.symbol));
        }


    }

}
