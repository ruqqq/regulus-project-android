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

import java.util.ArrayList;
import java.util.List;

public class DanteServer extends BaseServer implements BaseServer.BaseRequest {
    public String SERVER_BASE_URL = "http://definerp.com/leo/leo.php";

    private String username;
    private String password;

    private final Handler mHandler = new Handler();

    public DanteServer(Context context, String username, String password) {
        super(context);

        this.username = username;
        this.password = password;
    }

    private List<NameValuePair> makeParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", username));
        params.add(new BasicNameValuePair("pass", password));
        return params;
    }

    public String getServerURL() {
        return SERVER_BASE_URL;
    }

    public void getClasses(final Delegate delegate) {
        new Thread(new Runnable() {
            public void run() {
                JSONArray jsonArray = doPost("timetable", delegate);
                if (jsonArray != null) {
                    final ArrayList<ModuleSlot> timetable = new ArrayList<ModuleSlot>();

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            timetable.add(new ModuleSlot(jsonObject.getString("id"), jsonObject.getString("problem"), jsonObject.getString("title"), jsonObject.getString("date"), jsonObject.getString("venue"), jsonObject.getString("time")));
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
                JSONArray jsonArray = doPost("ut", delegate);
                if (jsonArray != null) {
                    final ArrayList<UTSlot> timetable = new ArrayList<UTSlot>();

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String[] ut_split = jsonObject.getString("title").split(" ");
                            timetable.add(new UTSlot(ut_split[3], ut_split[0]+" UT"+ut_split[3], jsonObject.getString("date"), jsonObject.getString("venue"), jsonObject.getString("time")));
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

    private JSONArray doPost(String mode, final Delegate delegate) {
        List<NameValuePair> params = makeParams();
        params.add(new BasicNameValuePair("mode", mode));

        CompletedResponse res = doPost(params, delegate);

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
