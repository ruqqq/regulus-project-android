package sg.rp.geeks.leoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
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
        dashboardItems.add(new DashboardItem(getResources().getString(R.string.timetable), getResources().getDrawable(R.drawable.schedule_icon)));
        dashboardItems.add(new DashboardItem(getResources().getString(R.string.recent_grades), getResources().getDrawable(R.drawable.grades_icon)));

        this.mGridView = (GridView) findViewById(R.id.gv_dashboard);
        this.mDashboardAdapter = new DashboardAdapter(this, dashboardItems);

        this.mGridView.setAdapter(mDashboardAdapter);
        this.mGridView.setOnItemClickListener(dashboardItemClickListener);
    }

    private AdapterView.OnItemClickListener dashboardItemClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Initialize a new intent
            Intent intent = new Intent();
            switch (i) {
                case 0:
                    intent.setClass(adapterView.getContext(), TimetableActivity.class);
                    break;
                //case 1:
                //intent.setClass(adapterView.getContext(), ProfileActivity.class);
                //break;
                case 1:
                    intent.setClass(adapterView.getContext(), GradesActivity.class);
                    break;
                //case 3:
                //intent.setClass(adapterView.getContext(), ModuleSummaryActivity.class);
                //break;
            }

            if (intent.getComponent() != null) {
                startActivity(intent);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem itemPreferences = menu.add(0, R.id.menu_preferences, Menu.NONE, "Preferences");
        itemPreferences.setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case (R.id.menu_preferences):
                showPreferences();
                break;
        }

        return true;
    }

    private void showPreferences() {
        Intent i = new Intent(DashboardActivity.this, PreferencesActivity.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String prefUsername = prefs.getString("pref_key_rp_username", "");
        String prefPassword = prefs.getString("pref_key_rp_password", "");

        if (prefUsername.equals("") || prefPassword.equals("")) {

            showPreferences();
            Toast.makeText(DashboardActivity.this, "Please enter a username and password", Toast.LENGTH_LONG).show();
        }
    }
}
