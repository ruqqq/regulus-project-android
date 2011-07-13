package sg.rp.geeks.leoapp.connection;

import android.content.Context;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

public class BaseServer {
    protected static final String TAG = "RegulusProject";
    protected String SERVER_BASE_URL;

    protected final Context mContext;

    public interface Delegate {
        // Called if connection error occurred
        public void connectionError(String error);

        // Return empty error if success
        public void connectionEnded(String error, Object object);
    };

    public interface BaseRequest {
        public void getClasses(final Delegate delegate);

        public void getUTs(final Delegate delegate);
    }

    public BaseServer(Context context) {
        this.mContext = context;
    }

    public HttpResponse makeRequest(String url) throws Exception {
        HttpResponse res = makeRequestNoRetry(url);
        if (res.getStatusLine().getStatusCode() == 500) {
            res = makeRequestNoRetry(url);
        }
        return res;
    }

    protected HttpResponse makeRequestNoRetry(String url)
            throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        URI uri = new URI(url);
        HttpGet http = new HttpGet(uri);
        //post.setHeader("X-Same-Domain", "1");  // XSRF
	    //Log.d("RegulusProject", "Making GET request to "+uri.toString());
        HttpResponse res = client.execute(http);
	    //Log.d("RegulusProject", "Result: "+res.getStatusLine().getStatusCode());
        return res;
    }

    public HttpResponse makePostRequest(String url, List<NameValuePair> params) throws Exception {
        HttpResponse res = makePostRequestNoRetry(url, params);
        if (res.getStatusLine().getStatusCode() == 500) {
            res = makePostRequestNoRetry(url, params);
        }
        return res;
    }

    protected HttpResponse makePostRequestNoRetry(String url, List<NameValuePair> params)
            throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        URI uri = new URI(url);
        HttpPost http = new HttpPost(uri);
        //post.setHeader("X-Same-Domain", "1");  // XSRF
        UrlEncodedFormEntity entity =
            new UrlEncodedFormEntity(params, "UTF-8");
        http.setEntity(entity);
	    //Log.d("RegulusProject", "Making POST request to "+uri.toString());
        HttpResponse res = client.execute(http);
	    //Log.d("RegulusProject", "Result: "+res.getStatusLine().getStatusCode());
        return res;
    }
    
    public static HttpResponse makeGetRequest(Context context, String url) throws Exception {
        BaseServer client = new BaseServer(context);
        return client.makeRequest(url);
    }

    protected class CompletedResponse {
        String error;
        String body;

        public CompletedResponse(String error, String body) {
            this.error = error;
            this.body = body;
        }
    }

    protected String getServerURL() {
        return SERVER_BASE_URL;
    }

    protected CompletedResponse doPost(List<NameValuePair> params, final Delegate delegate) {
        try {
            HttpResponse res = makePostRequest(getServerURL(), params);
            if (res.getStatusLine().getStatusCode() == 200) {
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                res.getEntity().writeTo(outstream);
                String responseBody = new String(outstream.toByteArray());
                Log.d(TAG, "Response: " + responseBody);

                return new CompletedResponse("", responseBody);
            } else {
                Log.e(TAG, "Error "+res.getStatusLine().getStatusCode()+"!");
                delegate.connectionError("Error "+res.getStatusLine().getStatusCode()+"!");
                return null;
            }
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            e.printStackTrace();
            delegate.connectionError(e.getMessage());
            return null;
        }
    }
}
