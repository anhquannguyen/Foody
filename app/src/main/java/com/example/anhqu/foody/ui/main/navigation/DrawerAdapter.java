package com.example.anhqu.foody.ui.main.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anhqu.foody.R;
import com.example.anhqu.foody.data.database.model.DrawerItem;
import com.example.anhqu.foody.ui.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhqu on 9/5/2017.
 */

public class DrawerAdapter extends RecyclerView.Adapter {
    private static final int ITEM_TYPE = 1;
    private static final int HEADER_TYPE = 0;
    private Context context;
    private List<DrawerItem> drawerItems;
    private int clickPos = 0;

    public DrawerAdapter(Context context, List<DrawerItem> drawerItems) {
        this.context = context;
        this.drawerItems = drawerItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.list_drawer_item, parent, false);
            return new ItemHolder(view);
        } else if (viewType == HEADER_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.nav_header_main, parent, false);
            return new HeaderHolder(view);
        }
        throw new RuntimeException("There is no type that match " + viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {

        } else if (holder instanceof ItemHolder) {
            //((ItemHolder) holder).navIcon.setImageResource(getItem(position).getIcon());
            ((ItemHolder) holder).navTitle.setText(getItem(position).getTitle());

            // hightlight selected item
            if (context instanceof MainActivity) {
                ((MainActivity) context).setHandler(position1 -> {
                    if (position1 >= 0) {
                        // IMPORTANT!
                        clickPos = position1;
                        notifyDataSetChanged();
                    }
                });
            }
            // IMPORTANT!
            if (clickPos == position - 1) {
                //((ItemHolder) holder).navRow.setBackgroundColor(context.getResources().getColor(R.color.colorSelectedItem));
            } else {
                //setBackgroundSelected(((ItemHolder) holder).navRow);
            }
        }
    }

    private DrawerItem getItem(int position) {
        return drawerItems.get(position - 1);
    }

/*    private void setBackgroundSelected(View view) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }*/

    @Override
    public int getItemCount() {
        return drawerItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        return ITEM_TYPE;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nav_icon)
        ImageView navIcon;
        @BindView(R.id.nav_title)
        TextView navTitle;
        @BindView(R.id.nav_row)
        RelativeLayout navRow;

        private ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        private HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
