package sg.rp.geeks.leoapp.connection;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import sg.rp.geeks.leoapp.item.ModuleSlot;
import sg.rp.geeks.leoapp.item.UTSlot;
import sg.rp.geeks.leoapp.item.GradeSlot;

import java.util.ArrayList;
import java.util.List;

public class PyroServer extends BaseServer implements BaseServer.BaseRequest {
    public String SERVER_BASE_URL = "http://emoosx.me/regulus/api/";

    private String username;
    private String password;

    private final Handler mHandler = new Handler();

    public PyroServer(Context context, String username, String password) {
        super(context);

        this.username = username;
        this.password = password;
    }

    private List<NameValuePair> makeParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", username));
        params.add(new BasicNameValuePair("password", password));
        return params;
    }

    public String getServerURL() {
        return SERVER_BASE_URL;
    }

    //mode will be either ut or class
    public String getScheduleUrl(String mode) {
        if(mode.equals("ut")) {
            return getServerURL()+"classroom/utSchedule"; //UT grades
        }
        return getServerURL()+"classroom/classSchedule"; //Daily grades
    }

    //mode will be either ut or daily
    public String getGradesUrl(String mode) {
        if(mode.equals("ut")) {
            return getServerURL() + "grades/recentUTGrades";
        }
        return getServerURL() + "grades/recentGrades";
    }

    public void getClasses(final Delegate delegate) {
        new Thread(new Runnable() {
            public void run() {
                JSONArray jsonArray = doPost(getScheduleUrl("class"), delegate);
                if (jsonArray != null) {
                    final ArrayList<ModuleSlot> timetable = new ArrayList<ModuleSlot>();
                    try {
                        Log.d(TAG, jsonArray.length()+"");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            timetable.add(new ModuleSlot(jsonObject.getString("module_code"), jsonObject.getString("problem_no"), jsonObject.getString("module_name"), jsonObject.getString("date"), jsonObject.getString("venue"), jsonObject.getString("time"), jsonObject.getString("day")));
                        }

                        mHandler.post(new Runnable() {
                            public void run() {
                                delegate.connectionEnded(null, timetable);
                            }
                        });
                    } catch (Exception e) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                delegate.connectionEnded("Error parsing JSON!", null);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void getUTs(final Delegate delegate) {
        new Thread(new Runnable() {
            public void run() {
                JSONArray jsonArray = doPost(getScheduleUrl("ut"), delegate);
                if (jsonArray != null) {
                    final ArrayList<UTSlot> timetable = new ArrayList<UTSlot>();

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            timetable.add(new UTSlot(jsonObject.getString("ut_no"), jsonObject.getString("ut_name"), jsonObject.getString("date"), jsonObject.getString("venue"), jsonObject.getString("time")));
                        }

                        mHandler.post(new Runnable() {
                            public void run() {
                                delegate.connectionEnded(null, timetable);
                            }
                        });
                    } catch (Exception e) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                delegate.connectionEnded("Error parsing JSON!", null);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void getRecentGrades(final Delegate delegate) {
        new Thread(new Runnable() {
            public void run() {
                JSONArray jsonArray = doPost(getGradesUrl("daily"), delegate);
                if (jsonArray != null) {
                    final ArrayList<GradeSlot> grades = new ArrayList<GradeSlot>();

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            grades.add(new GradeSlot(jsonObject.getString("module_code"), jsonObject.getString("problem"), jsonObject.getString("grade")));
                        }
                        mHandler.post(new Runnable() {
                            public void run() {
                                delegate.connectionEnded(null, grades);
                            }
                        });
                    } catch (Exception e) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                delegate.connectionEnded("Error parsing JSON!", null);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private JSONArray doPost(String postUrl, final Delegate delegate) {
        List<NameValuePair> params = makeParams();

        CompletedResponse res = doPost(params, postUrl, delegate);

        if (res != null && res.error.equals("")) {
            try {
                if (!res.body.contains("invalid")) {
                    JSONArray jsonArray = (JSONArray) new JSONTokener(res.body).nextValue();
                    return jsonArray;
                } else {
                    Log.e(TAG, "Server returned: "+res.body);
                    delegate.connectionEnded("Server returned: "+res.body, null);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                delegate.connectionEnded(e.getMessage(), null);
            }
        }

        return null;
    }
}
