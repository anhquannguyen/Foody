package com.example.anhqu.orderApp.data.database.model;

import java.io.Serializable;

/**
 * Created by anhqu on 9/5/2017.
 */

public class DrawerItem implements Serializable {
    private int icon;
    private String title;

    public DrawerItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
