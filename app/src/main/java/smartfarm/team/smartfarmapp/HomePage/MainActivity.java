package smartfarm.team.smartfarmapp.HomePage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import smartfarm.team.smartfarmapp.Crop.CropActivity;
import smartfarm.team.smartfarmapp.R;
import smartfarm.team.smartfarmapp.Suggestion.SuggestionActivity;
import smartfarm.team.smartfarmapp.farm.CurrentCrop;
import smartfarm.team.smartfarmapp.farm.MyFarmActivity;
import smartfarm.team.smartfarmapp.gcm.NotificationActivity;
import smartfarm.team.smartfarmapp.gcm.RegistrationIntentService;
import smartfarm.team.smartfarmapp.signup.SoilActivity;
import static smartfarm.team.smartfarmapp.R.id.notification;

public class MainActivity extends AppCompatActivity {

    private LinearLayout cropButton,notificationButton,suggestionButton,myFarmButton,currentCropButton;
    static public Activity thisAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisAct = MainActivity.this;

        cropButton = (LinearLayout) findViewById(R.id.crops);
        suggestionButton = (LinearLayout) findViewById(R.id.suggestions);
        notificationButton = (LinearLayout) findViewById(notification);
        myFarmButton = (LinearLayout) findViewById(R.id.farm);
        currentCropButton = (LinearLayout) findViewById(R.id.current);

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropClick();
            }
        });
        suggestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestionClick();
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationClick();
            }
        });
        myFarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmClick();
            }
        });
        currentCropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClick();
            }
        });



        //TP
        SharedPreferences details = getSharedPreferences(getString(R.string.shared_main_name), MODE_PRIVATE);
        Intent intent = new Intent(MainActivity.this, RegistrationIntentService.class);
        Bundle arg = new Bundle();
        arg.putString("id",details.getString(getString(R.string.shared_farm_id),"001"));
        intent.putExtras(arg);
        startService(intent);

    }

    private void farmClick() {
        Intent intent = new Intent(MainActivity.this, MyFarmActivity.class);
        startActivity(intent);
    }
    private void currentClick() {
        Intent intent = new Intent(MainActivity.this, CurrentCrop.class);
        startActivity(intent);
    }

    private void suggestionClick() {
        Intent intent = new Intent(MainActivity.this, SuggestionActivity.class);
        startActivity(intent);
    }

    private void cropClick() {
        Intent intent=new Intent(MainActivity.this,CropActivity.class);
        startActivity(intent);
    }

    private void notificationClick() {
        Intent intent=new Intent(MainActivity.this,NotificationActivity.class);
        startActivity(intent);
    }
}