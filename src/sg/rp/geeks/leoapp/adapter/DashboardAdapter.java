package sg.rp.geeks.leoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import sg.rp.geeks.leoapp.R;
import sg.rp.geeks.leoapp.item.DashboardItem;

import java.util.ArrayList;

public class DashboardAdapter extends BaseAdapter {

    protected ArrayList<DashboardItem> mItems;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public DashboardAdapter(Context context, ArrayList<DashboardItem> items) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mItems = items;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int i) {
        return mItems.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null) {
            view = this.mLayoutInflater.inflate(R.layout.dashboard_item, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.iv_dashboard_icon);
            viewHolder.textView = (TextView)view.findViewById(R.id.tv_dashboard_label);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();

        }
        DashboardItem item = this.mItems.get(i);
        viewHolder.textView.setText(item.getName());
        if(item.getIcon()!= null) {
            viewHolder.imageView.setImageDrawable(item.getIcon());
        }

        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
