package smartfarm.team.smartfarmapp.log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import smartfarm.team.smartfarmapp.gcm.RegistrationIntentService;
import smartfarm.team.smartfarmapp.R;

public class LogActivity extends AppCompatActivity {

    ListView logListView;
    static public Activity thisAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        thisAct = LogActivity.this;
        Intent gcmToken = new Intent(LogActivity.thisAct, RegistrationIntentService.class);
        startService(gcmToken);

        logListView = (ListView) findViewById(R.id.recentLogListView);

        Intent dataIntent = getIntent();
        String array[] = new String[]{
                dataIntent.getStringExtra(getString(R.string.gcm_end_light)),
                dataIntent.getStringExtra(getString(R.string.gcm_water)),
                dataIntent.getStringExtra(getString(R.string.gcm_start_light)),
                //dataIntent.getStringExtra(getString(R.string.gcm_moisture))
        };

        LogListViewAdapter adapter = new LogListViewAdapter(LogActivity.this,array);
        logListView.setAdapter(adapter);
    }
}
