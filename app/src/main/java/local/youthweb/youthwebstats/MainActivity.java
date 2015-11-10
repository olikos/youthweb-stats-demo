package local.youthweb.youthwebstats;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {
    private Spinner spinner;
    private YWService ywService = null;
    private TextView textView;
    private ImageView imgYwLogo;
    public final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "start activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{ParamsYouthwebStats.ACCOUNT, ParamsYouthwebStats.FORUM, ParamsYouthwebStats.GROUPS};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        textView = (TextView) findViewById(R.id.textView_main);
        imgYwLogo = (ImageView) findViewById(R.id.imageView);
        imgYwLogo.setImageDrawable(getResources().getDrawable(R.drawable.youthweb_logo));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String stats = String.valueOf(parent.getItemAtPosition(position));
                ywService = new YWService(stats, getApplicationContext());
                Log.i(TAG, "Selected = " + stats);
                String json = stats;
                String jsonString;
                Log.d(TAG, "URL = " + stats);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage() + "\n" + e.toString());
                }
                try {
                    jsonString = ywService.getJson();
                    Log.d(TAG, "getJson= " + jsonString);

                    JSONObject obj = new JSONObject(jsonString);
                    json = obj.toString(4);
                    Log.d(TAG, "JSon pretty= " + json);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + "\n" + e.toString());
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                textView.setText(dateFormat.format(date) + "\n" + json);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*stats_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = ywService.getJson();
                ywService = new YWService("https://youthweb.net/stats/account");
                // textView.setText(json);
                try {
                    JSONObject obj = new JSONObject(json);
                    json = obj.toString(4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textView.setText(json);
                // JSON.parse(json);

                Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
