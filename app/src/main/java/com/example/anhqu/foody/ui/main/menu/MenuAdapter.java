package com.example.anhqu.foody.ui.main.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhqu.foody.R;
import com.example.anhqu.foody.data.database.model.Menu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhqu on 6/22/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<Menu> menuList;
    private Context context;

    public MenuAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        final Menu menuItemObject = menuList.get(position);
        Glide.with(context)
                .load(menuItemObject.getmImage())
                .into(holder.imageView);
        holder.textView.setText(menuItemObject.getmName());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;

        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
