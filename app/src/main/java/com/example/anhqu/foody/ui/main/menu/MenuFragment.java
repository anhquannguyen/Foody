package com.example.anhqu.foody.ui.main.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.data.database.model.Menu;
import com.example.anhqu.foody.data.network.ApiClient;
import com.example.anhqu.foody.data.network.ApiInterface;
import com.example.anhqu.foody.ui.food.FoodActivity;
import com.example.anhqu.foody.ui.main.MainActivity;
import com.example.anhqu.foody.utils.GridSpacingItemDecoration;
import com.example.anhqu.foody.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhqu on 6/22/2017.
 */

public class MenuFragment extends Fragment {
    private static final String KEY_RC = "recycler_data";
    private static final String KEY_ID = "menu_id";
    private static final String KEY_NAME = "menu_name";
    private final String TAG = this.getTag();
    Unbinder unbinder;
    List<Menu> menuList;
    MenuAdapter adapter;
    Bundle b;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
        getMenu();
        fetchMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (menuList != null && menuList.size() != 0) {
            b = new Bundle();
            Parcelable parcelable = recyclerView.getLayoutManager()
                    .onSaveInstanceState();
            b.putParcelable(KEY_RC, parcelable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (b != null) {
            Parcelable parcelable = b.getParcelable(KEY_RC);
            recyclerView.getLayoutManager()
                    .onRestoreInstanceState(parcelable);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMenu().dispose();
    }

    private void setRecyclerView() {
        menuList = new ArrayList<>(10);
        adapter = new MenuAdapter(menuList, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 10, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String menuId = menuList.get(position).getmId();
                String menuName = menuList.get(position).getmName();
                foodActivity(menuId, menuName);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void fetchMenu(){
        MainActivity.getNetworkStatus().subscribe(aBoolean ->  Log.d(TAG, "fetchMenu: "+Thread.currentThread().getName()));
    }

    private Disposable getMenu() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        return api.getMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> progressBar.setVisibility(View.VISIBLE))
                .doOnComplete(() -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .subscribe(menus -> {
                    menuList.addAll(menus);
                    adapter.notifyDataSetChanged();
                }, throwable -> Log.d(TAG, "getApi: " + throwable));
    }

    private void foodActivity(String id, String name) {
        Intent i = new Intent(getContext(), FoodActivity.class);
        i.putExtra(KEY_ID, id);
        i.putExtra(KEY_NAME, name);
        getContext().startActivity(i);
    }

    @OnClick(R.id.txt_retry)
    public void onViewClicked() {

    }
}
