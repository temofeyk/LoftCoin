package com.temofey.loftcoin.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.temofey.loftcoin.App;
import com.temofey.loftcoin.R;
import com.temofey.loftcoin.data.db.model.CoinEntity;
import com.temofey.loftcoin.data.prefs.Prefs;
import com.temofey.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.temofey.loftcoin.screens.currencies.CurrenciesBottomSheetListener;
import com.temofey.loftcoin.screens.main.wallets.adapters.WalletsPagerAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {


    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.new_wallet)
    ViewGroup newWallet;

    private WalletsPagerAdapter walletsPagerAdapter;

    private WalletsViewModel viewModel;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);

        Prefs prefs = ((App) Objects.requireNonNull(getActivity()).getApplication()).getPrefs();

        walletsPagerAdapter = new WalletsPagerAdapter(prefs);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.accounts_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);


        int screenWidth = getScreenWidth();
        int walletItemWidth = getResources().getDimensionPixelOffset(R.dimen.item_wallet_width);
        int walletItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_wallet_margin);
        int pageMargin = (screenWidth - walletItemWidth) - walletItemMargin;

        walletsPager.setPageMargin(-pageMargin);
        walletsPager.setOffscreenPageLimit(5);
        walletsPager.setAdapter(walletsPagerAdapter);

        Fragment bottomSheet = Objects.requireNonNull(getFragmentManager()).findFragmentByTag(CurrenciesBottomSheet.TAG);
        if (bottomSheet != null) {
            ((CurrenciesBottomSheet) bottomSheet).setListener(this);
        }

        viewModel.getWallets();

        initOutputs();
        initInputs();

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private void initOutputs() {
        newWallet.setOnClickListener(view -> viewModel.onNewWalletClick());

        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(menuItem -> {
            viewModel.onNewWalletClick();
            return true;
        });
    }

    private void initInputs() {

        viewModel.wallets().observe(this, wallets -> walletsPagerAdapter.setWallets(wallets));

        viewModel.walletsVisible().observe(this, visible -> {
            if (visible != null) walletsPager.setVisibility(visible ? View.VISIBLE : View.GONE);
        });
        viewModel.newWalletVisible().observe(this, visible -> {
            if (visible != null) newWallet.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        viewModel.selectCurrency().observe(this, o -> showCurrenciesBottomSheet());

    }

    private void showCurrenciesBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(Objects.requireNonNull(getFragmentManager()), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModel.onCurrencySelected(coin);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) Objects.requireNonNull(getContext()).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }
}
