package com.example.anhqu.foody.view.fragment;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.model.Menu;
import com.example.anhqu.foody.model.api.ApiClient;
import com.example.anhqu.foody.model.api.ApiInterface;
import com.example.anhqu.foody.view.FoodActivity;
import com.example.anhqu.foody.view.MainActivity;
import com.example.anhqu.foody.view.adapter.MenuAdapter;
import com.example.anhqu.foody.utils.GridSpacingItemDecoration;
import com.example.anhqu.foody.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private final String TAG = this.getTag();
    Unbinder unbinder;
    List<Menu> menuList;
    MenuAdapter adapter;
    Bundle b;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rootView)
    RelativeLayout rootView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (MainActivity.isConnected()) {
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            unbinder = ButterKnife.bind(this, view);
            setRecyclerView();
            getApi();
            return view;
        } else {
            view = inflater.inflate(R.layout.layout_no_connect, container, false);
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        getApi().dispose();
    }

    private void setRecyclerView() {
        progressBar.setVisibility(View.VISIBLE);
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
                foodActivity(menuId);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private Disposable getApi() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        return api.get().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(menus -> {
                    menuList.addAll(menus);
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }, throwable -> Log.d(TAG, "getApi: " + throwable));
    }

    private void foodActivity(String id) {
        Intent i = new Intent(getContext(), FoodActivity.class);
        i.putExtra(KEY_ID,id);
        getContext().startActivity(i);
    }
}
