package sg.rp.geeks.leoapp.item;

import android.graphics.drawable.Drawable;

public class DashboardItem {

    protected String mName;
    protected Drawable mIcon;

    public DashboardItem(String name, Drawable icon) {
        this.mName = name;
        this.mIcon = icon;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }
}
