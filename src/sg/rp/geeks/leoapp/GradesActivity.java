package sg.rp.geeks.leoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import greendroid.app.GDActivity;
import greendroid.widget.GDActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import sg.rp.geeks.leoapp.adapter.SectionedAdapter;
import sg.rp.geeks.leoapp.connection.BaseServer;
import sg.rp.geeks.leoapp.connection.PyroServer;
import sg.rp.geeks.leoapp.item.GradeSlot;
import sg.rp.geeks.leoapp.widget.TitleFlowIndicator;
import sg.rp.geeks.leoapp.widget.TitleProvider;
import sg.rp.geeks.leoapp.widget.ViewFlow;

import java.util.ArrayList;
import java.util.Collections;

public class GradesActivity extends GDActivity {

    private final Handler mHandler = new Handler();

    private ViewFlow vfGrades;

    private PyroServer server;

    private ArrayList<GradeSlot> mRecentGrades;
    private ArrayList<GradeSlot> mRecentUTGrades;

    private GradeViewsAdapter mGradeViewsAdapter;

    private SectionedAdapter mSectionedRecentGradesAdapter;
    private SectionedAdapter mSectionedRecentUTGradesAdapter;


    private BaseAdapter[] mAdapters = new BaseAdapter[2];

    private SharedPreferences prefs;

    String username = "", password = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize views
        setGDActionBarContentView(R.layout.timetable_activity);
        addActionBarItem(GDActionBarItem.Type.Refresh, R.id.action_bar_refresh);


        // initialize objects
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        mRecentGrades = new ArrayList<GradeSlot>();
        mSectionedRecentGradesAdapter = new SectionedAdapter() {
            protected View getHeaderView(String caption, int index, int count, View convertView, ViewGroup parent) {
                TextView result = (TextView) convertView;
                if(convertView == null) {
                    result = (TextView)getLayoutInflater().inflate(R.layout.list_section_header, null);
                }
                result.setText(caption);
                return result;
            }
        };

        mRecentUTGrades = new ArrayList<GradeSlot>();
        mSectionedRecentUTGradesAdapter = new SectionedAdapter() {
            protected View getHeaderView(String caption, int index, int count, View convertView, ViewGroup parent) {
                TextView result = (TextView)convertView;
                if(convertView == null) {
                    result = (TextView)getLayoutInflater().inflate(R.layout.list_section_header, null);
                }
                result.setText(caption);
                return result;
            }
        };

        mAdapters[0] = mSectionedRecentGradesAdapter;
        mAdapters[1] = mSectionedRecentUTGradesAdapter;

        vfGrades = (ViewFlow) findViewById(R.id.vf_timetable);
        mGradeViewsAdapter = new GradeViewsAdapter();
        vfGrades.setAdapter(mGradeViewsAdapter);

        TitleFlowIndicator indicator = (TitleFlowIndicator)findViewById(R.id.vf_timetable_indicator);
        indicator.setTitleProvider(mGradeViewsAdapter);
        vfGrades.setFlowIndicator(indicator);

        vfGrades.setSelection(0);   // select recent grades

    }

    @Override
    public void onResume() {
        super.onResume();

        String prefUsername = prefs.getString("pref_key_rp_username", "");
        String prefPassword = prefs.getString("pref_key_rp_password", "");

        if(prefUsername.equals("") || prefPassword.equals("")) {

            showPreferences();
            Toast.makeText(GradesActivity.this, "Please enter a username and password", Toast.LENGTH_LONG).show();
        } else {

            if(!this.username.equals(prefUsername) || !this.password.equals(prefPassword)) {
                this.username = prefUsername;
                this.password = prefPassword;
                server = new PyroServer(this, this.username, this.password);
                reloadData();
            }
        }
    }

    @Override
    public boolean onHandleActionBarItemClick(GDActionBarItem item, int position) {
        switch(item.getItemId()) {
            case R.id.action_bar_refresh:
                final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                reloadData();
                break;
             case R.id.action_bar_home_item:
                 Intent i = new Intent(GradesActivity.this, DashboardActivity.class);
                 startActivity(i);
            default:
                return super.onHandleActionBarItemClick(item, position);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem itemPreferences = menu.add(0, R.id.menu_preferences, Menu.NONE, "Preferences");
        itemPreferences.setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }

    public boolean onOptionsMenuItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case(R.id.menu_preferences):
                showPreferences();
                break;
        }
        return true;
    }

    private void showPreferences() {
        Intent i = new Intent(GradesActivity.this, PreferencesActivity.class);
        startActivity(i);
    }

    public void reloadData() {
        final LoaderActionBarItem loaderItem = ((LoaderActionBarItem) getGDActionBar().getItem(0));
        loaderItem.setLoading(true);



         // get Recent Grades
        server.getRecentGrades(new BaseServer.Delegate() {
            public void connectionError(String error) {

            }

            public void connectionEnded(String error, Object object) {
                  if(object instanceof ArrayList) {
                    mRecentGrades = (ArrayList<GradeSlot>) object;
                    ArrayList<String> problem_title = new ArrayList<String>();
                    ArrayList<Grade> grades = new ArrayList<Grade>();
                    Collections.reverse(mRecentGrades);
                    for(GradeSlot m : mRecentGrades) {
                        Grade grade;
                        if(!problem_title.contains("Problem " + m.getProblem())) {
                            grade = new Grade();
                            grade.name = "Problem " + m.getProblem();
                            grades.add(grade);
                            problem_title.add(grade.name);
                            Log.d("Regulus", "Added Problem For Grades " + m.getProblem());
                        } else {
                            grade = grades.get(problem_title.indexOf("Problem " + m.getProblem()));
                        }

                        Log.d("Regulus", "Added To Problem For Grades " + grade.name + ": " + m.getModuleCode());
                        grade.grades.add(m);
                    }

                    mSectionedRecentGradesAdapter.removeAllSections();
                      for(Grade grade: grades) {
                          Log.d("Regulus", "Added to Adapter :" + grade.name );
                          GradesAdapter  gradesAdapter = new GradesAdapter(grade.grades);
                          mSectionedRecentGradesAdapter.addSection(grade.name, gradesAdapter);
                      }
                      mSectionedRecentGradesAdapter.notifyDataSetChanged();
                      mGradeViewsAdapter.notifyDataSetChanged();;
                }
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        // Using a temporary soluton of setting the loader item only when grades are loaded
                        // Grades are usually the slowest to load since it has to get the module name
                        loaderItem.setLoading(false);
                    }
                }, 2000);
            }
        });


        // get UT Grades
        server.getRecentUTGrades(new BaseServer.Delegate() {
            public void connectionError(String error) {

            }

            public void connectionEnded(String error, Object object) {
                if(object instanceof ArrayList) {
                    mRecentUTGrades = (ArrayList<GradeSlot>) object;
                    ArrayList<String> problem_title = new ArrayList<String>();
                    ArrayList<Grade> grades = new ArrayList<Grade>();
                    Collections.reverse(mRecentUTGrades);

                    for(GradeSlot m : mRecentUTGrades) {

                        Grade grade;
                        if(!problem_title.contains("Understanding Test " + m.getProblem())) {

                            grade = new Grade();
                            grade.name = "Understanding Test " + m.getProblem();
                            grades.add(grade);

                            problem_title.add(grade.name);
                            Log.d("Regulus", "Added UT For Grades " + m.getProblem());
                        }
                        else {
                            grade= grades.get(problem_title.indexOf("Understanding Test " + m.getProblem()));
                        }

                         Log.d("Regulus", "Added To UT For Grades " + grade.name + ": " + m.getModuleCode());
                        grade.grades.add(m);
                    }

                    mSectionedRecentUTGradesAdapter.removeAllSections();

                    for(Grade grade : grades ) {
                        Log.d("Regulus", "Aded to Adapter: " + grade.name);
                        GradesAdapter gradesAdapter = new GradesAdapter(grade.grades);
                        mSectionedRecentUTGradesAdapter.addSection(grade.name, gradesAdapter);
                    }

                    mSectionedRecentUTGradesAdapter.notifyDataSetChanged();
                    mGradeViewsAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private class Grade {
        String name;
        ArrayList<GradeSlot> grades = new ArrayList<GradeSlot>();
    }

    public class GradesAdapter extends ArrayAdapter<GradeSlot> {
        ArrayList<GradeSlot> items;

        public GradesAdapter(Context context, int textViewResourceId, ArrayList<GradeSlot> items) {
            super(context, textViewResourceId, items);
        }

        public GradesAdapter(ArrayList<GradeSlot> items) {
            this(GradesActivity.this, R.layout.grade_list_item, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.grade_list_item, null);
            }
            GradeSlot gradeSlot = items.get(position);

            ((TextView)convertView.findViewById(R.id.title)).setText(gradeSlot.getModuleCode());
            ((TextView)convertView.findViewById(R.id.grade_holder)).setTextColor(getResources().getColor(mapGradeToColor(gradeSlot.getGrade())));
            ((TextView)convertView.findViewById(R.id.grade_holder)).setText(gradeSlot.getGrade());

            return convertView;
        }
    }

    private int mapGradeToColor(String grade) {
            if (grade.equalsIgnoreCase("A")) {
                return R.color.A;
            }
            if (grade.equalsIgnoreCase("B")) {
                return R.color.B;
            }
            if (grade.equalsIgnoreCase("B+")) {
                return R.color.Bplus;
            }
            if (grade.equalsIgnoreCase("C")) {
                return R.color.C;
            }
            if (grade.equalsIgnoreCase("C+")) {
                return R.color.Cplus;
            }
            if (grade.equalsIgnoreCase("D")) {
                return R.color.D;
            }
            if (grade.equalsIgnoreCase("D+")) {
                return R.color.Dplus;
            }
            if (grade.equalsIgnoreCase("E")) {
                return R.color.E;
            }
            if (grade.equalsIgnoreCase("F")) {
                return R.color.F;
            }
            if (grade.equalsIgnoreCase("X")) {
                return R.color.X;
            }
            return 0;
        }


    public class GradeViewsAdapter extends BaseAdapter implements TitleProvider {
        private final String[] titles = {"Daily Grades", "UT Grades"};

        public int getCount() {
            return titles.length;
        }

        public Object getItem(int i) {
            return i;
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = new ListView(GradesActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                view.setLayoutParams(lp);
            }
            Log.d("Regulus", "Assigning " + mAdapters[i]);
            ((ListView)view).setAdapter(mAdapters[i]);

            return view;
        }

        public String getTitle(int position) {
            return titles[position];
        }
    }
}
