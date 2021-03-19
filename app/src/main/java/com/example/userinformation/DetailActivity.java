package com.example.userinformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    EditText editText;
    Button saveButton;
    //boolean flag;
    SharedPreferences sharedpreferences;
    public static boolean closing = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification01", "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        editText = findViewById(R.id.textViewRes);
        saveButton = findViewById(R.id.save_button);

        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        closing = true;

        String myName = sharedpreferences.getString("myname1", "ERROR");
        editText.setText(myName);

        boolean flag = sharedpreferences.getBoolean("flag", false);


        if(flag == false) {
            saveButton.setEnabled(false);
            editText.setEnabled(false);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updated = editText.getText().toString();
                editText.setText(updated);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("myname", updated);
                editor.putString("myname1", updated);///////////////
                editor.apply();
                Toast.makeText(DetailActivity.this,"Saved Successfully",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closing = false;

        Intent intent = new Intent(DetailActivity.this, UsersActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(closing ) {
            sendNotification();
            String updated = editText.getText().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("myname1", updated);
            //editor.putString("message", updated);
            editor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel("mytag2", 2);
    }

    public void sendNotification() {
        String msg = "Come Back";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this, "My Notification01");
        builder.setContentTitle("Missed You");
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);

        Intent intent = new Intent(DetailActivity.this,
                DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(DetailActivity.this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(DetailActivity.this);
        managerCompat.notify("mytag2",2,  builder.build());

    }
}