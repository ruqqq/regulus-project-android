package sg.rp.geeks.leoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import greendroid.app.GDActivity;
import sg.rp.geeks.leoapp.adapter.DashboardAdapter;
import sg.rp.geeks.leoapp.item.DashboardItem;

import java.util.ArrayList;

public class DashboardActivity extends GDActivity {

    /* UI Elements */
    private GridView mGridView;
    private DashboardAdapter mDashboardAdapter;

    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setGDActionBarContentView(R.layout.dashboard_activity);



        ArrayList<DashboardItem> dashboardItems = new ArrayList<DashboardItem>();
        dashboardItems.add(new DashboardItem("Timetable", getResources().getDrawable(R.drawable.schedule_icon)));
        dashboardItems.add(new DashboardItem("Profile", getResources().getDrawable(R.drawable.profile_icon)));
        dashboardItems.add(new DashboardItem("Recent Grades", getResources().getDrawable(R.drawable.grades_icon)));
        dashboardItems.add(new DashboardItem("Module Summary", getResources().getDrawable(R.drawable.module_summary_icon)));



        this.mGridView = (GridView)findViewById(R.id.gv_dashboard);
        this.mDashboardAdapter = new DashboardAdapter(this, dashboardItems);

        this.mGridView.setAdapter(mDashboardAdapter);
        this.mGridView.setOnItemClickListener(dashboardItemClickListener);
    }

    private AdapterView.OnItemClickListener dashboardItemClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Initialize a new intent
            Intent intent = new Intent();
            switch(i) {
                case 0:
                    intent.setClass(adapterView.getContext(), TimetableActivity.class);
                    break;
                case 1:
                    intent.setClass(adapterView.getContext(), ProfileActivity.class);
                    break;
                case 2:
                    intent.setClass(adapterView.getContext(), GradesActivity.class);
                    break;
                case 3:
                    intent.setClass(adapterView.getContext(), ModuleSummaryActivity.class);
                    break;
            }

            if(intent.getComponent() != null) {
                startActivity(intent);
            }
        }
    };
}
