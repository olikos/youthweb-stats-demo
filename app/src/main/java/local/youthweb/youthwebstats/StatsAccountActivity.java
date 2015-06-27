package local.youthweb.youthwebstats;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StatsAccountActivity extends Activity {
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_account);
        output = (TextView) findViewById(R.id.output);
        // http request
        String outputString = "";
        try {
            StringBuilder builder = getResponseBody();
            JSONObject jsonObject = parseJsonString(builder.toString());
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONObject attributes = (JSONObject) data.get("attributes");
            outputString = (String) attributes.get("user_total");
        } catch (Exception e) {
            outputString = e.getMessage();
        }
        this.output.setText(outputString);
        // parse JSONObject(json-String)
        // JSONObject jsonObject = parseJsonString(builder.toString());
        // show get data.aAttributes.user_total
    }

    private JSONObject parseJsonString(String jsonString) throws Exception {
        JSONObject jsonObject = null;
            jsonObject = new JSONObject(jsonString);
        if (jsonObject == null) {
            throw new Exception("JSONObject is null");
        }
        return jsonObject;
    }

    private StringBuilder getResponseBody() throws IOException {
        String url = "https://youthweb.net/stats/account";
        String data = "data";
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        StringBuilder builder = null;

            builder = new StringBuilder();
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
    		int statusCode = statusLine.getStatusCode();
    		if(statusCode == 200){
    			HttpEntity entity = response.getEntity();
    			InputStream content = entity.getContent();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    			String line;
    			while((line = reader.readLine()) != null){
    				builder.append(line);
    			}

    		} else {
    			Log.e(StatsAccountActivity.class.toString(), "Failed to get JSON object");
                builder.append("");
    		}

        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it i
        // s present.
        getMenuInflater().inflate(R.menu.menu_stats_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
