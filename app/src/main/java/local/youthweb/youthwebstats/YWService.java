package local.youthweb.youthwebstats;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by oliver on 13.10.15.
 */
public class YWService {
    private Context context;
    private static ConnectivityManager connectivityManager;
    private String stats;
    private String TAG = this.getClass().getName();
    private String url;
    private String string = "fail";


    private HttpURLConnection conn = null;

    public YWService(String stats, Context context) {
        this.stats = stats;
        this.context = context;
        if (checkConnection(context)) {
            getUrl();
            createConnection();
            getJson();
        } else {
            Toast.makeText(context, "no Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUrl() {
        if (stats.equals(ParamsYouthwebStats.ACCOUNT)) {
            url = ParamsYouthwebStats.ACCOUNT_URL;
        } else if (stats.equals(ParamsYouthwebStats.FORUM)) {
            url = ParamsYouthwebStats.FORUM_URL;
        } else if (stats.equals(ParamsYouthwebStats.GROUPS)) {
            url = ParamsYouthwebStats.GROUPS_URL;
        }
    }

    public boolean checkConnection(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public String getJson() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    try {
                        // Starts the query
                        conn.connect();
                        int response = conn.getResponseCode();
                        is = conn.getInputStream();

                        // Convert the InputStream into a string
                        string = readIt(is, 1000);
                        Log.i(TAG, "received: " + string);
                        // Makes sure that the InputStream is closed after the app is
                        // finished using it.
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "\n" + e.toString());

                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                Log.e(TAG, e.getLocalizedMessage() + "\n" + e.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getLocalizedMessage() + "\n" + e.toString());
                }
            }
        });

        thread.start();
        return string;
    }

    private void createConnection() {
            try {
                URL urlu = new URL(url);
                conn = (HttpURLConnection) urlu.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/vnd.api+json");
                conn.setRequestProperty("Accept", "application/vnd.api+json, application/vnd.api+json; net.youthweb.api.version=0.6");
                conn.setDoInput(true);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage() + "\n" + e.toString());

            }
    }

    public String readIt(InputStream stream, int len) {
        try {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage() + "\n" + e.toString());
        }
        return "";

    }

}
