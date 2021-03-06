package com.example.anhqu.foody.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.data.database.DaoRepository;
import com.example.anhqu.foody.data.database.model.DrawerItem;
import com.example.anhqu.foody.data.prefs.SessionManager;
import com.example.anhqu.foody.services.ConnectivityReceiver;
import com.example.anhqu.foody.ui.BaseActivity;
import com.example.anhqu.foody.ui.main.menu.MenuFragment;
import com.example.anhqu.foody.ui.main.ordered.OrderedFragment;
import com.example.anhqu.foody.ui.order.OrderActivity;
import com.example.anhqu.foody.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    RecyclerView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    List<DrawerItem> drawerItems;
    Fragment fragment = null;
    SmoothActionBarDrawerToggle toggle;
    RecyclerHandler recyclerHandle;
    @BindView(R.id.content)
    CoordinatorLayout content;
    DaoRepository repository;
    @BindView(R.id.img_action)
    ImageView imgOrder;
    Disposable disposable;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    public static boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }

    protected static List<DrawerItem> itemList() {
        List<DrawerItem> itemList = new ArrayList<>();
        itemList.add(new DrawerItem(0, "Restaurant"));
        itemList.add(new DrawerItem(1, "Offers"));
        itemList.add(new DrawerItem(2, "Ordered"));
        itemList.add(new DrawerItem(3, "Account"));
        itemList.add(new DrawerItem(4, "Feedback me!"));
        return itemList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        toggle = new SmoothActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            drawerLayout.setElevation(16);
        }
        toggle.syncState();

        /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawerLayout.isDrawerOpen(navView)) {
                                                         drawerLayout.closeDrawer(navView);
                                                     } else {
                                                         drawerLayout.openDrawer(navView);
                                                     }
                                                 }
                                             }
        );

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawer, float slideOffset) {
                                               content.setX(navView.getWidth() * slideOffset);
                                               RelativeLayout.LayoutParams lp =
                                                       (RelativeLayout.LayoutParams) content.getLayoutParams();
                                               lp.height = drawer.getHeight() -
                                                       (int) (drawer.getHeight() * slideOffset * 0f);
                                               lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                               content.setLayoutParams(lp);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
                                           }
                                       }
        );
        */
        repository = new DaoRepository(this);
        setNavigationView();
        firstRun();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCountInOrder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setHandler(RecyclerHandler handler) {
        this.recyclerHandle = handler;
    }

    public RecyclerHandler getRecyclerHandle() {
        return recyclerHandle;
    }

    private void getCountInOrder() {
        disposable = repository.countInOrder().observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != 0) {
                        imgOrder.setImageResource(R.drawable.ic_cart);
                        imgOrder.setOnClickListener(view -> {
                            Intent i = new Intent(MainActivity.this, OrderActivity.class);
                            startActivity(i);
                        });
                        imgOrder.setVisibility(View.VISIBLE);
                    } else {
                        if (imgOrder.getVisibility() == View.VISIBLE) {
                            imgOrder.setVisibility(View.INVISIBLE);
                        }
                    }
                }, throwable -> Log.d(TAG, "getCountInOrder: " + throwable));
    }

    private void firstRun() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_layout, new MenuFragment()).commit();
        setFragmentTitle(0);
    }

    protected void setNavigationView() {
        drawerItems = itemList();
        navView.setLayoutManager(new LinearLayoutManager(this));
        navView.setAdapter(new DrawerAdapter(this, drawerItems));
        navView.addOnItemTouchListener(new RecyclerTouchListener(this, navView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                navigationHandler(position - 1);
                if (getRecyclerHandle() != null) {
                    getRecyclerHandle().onAdapterHandler(position - 1);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    protected void navigationHandler(final int position) {
        switch (position) {
            case 0:
                fragment = new MenuFragment();
                break;
            case 1:

                break;
            case 2:
                fragment = new OrderedFragment();
                break;
            case 3:
                new SessionManager(this).signOut();
                break;
        }
        toggle.runWhenIdle(() -> {
            if (fragment != null) {
                setFragmentTitle(position);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_layout, fragment).commit();
            }
        });
        drawerLayout.closeDrawers();
    }

    private void setFragmentTitle(int position) {
        getSupportActionBar().setTitle(drawerItems.get(position).getTitle());
    }

    /*============================================== INTERFACE ==================================================*/

    public interface RecyclerHandler {
        void onAdapterHandler(int position);
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }
}
