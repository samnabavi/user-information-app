package com.example.userinformation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class UsersActivity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList = new ArrayList<>();
    private List<User> mUsersList;
    private Button logoutButton;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;

    private RecyclerView mRecyclerView;

    //private RecyclerView.Adapter mAdapter;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences sharedPreferences;

    public static Boolean closing = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        closing = true;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        String myResponse = getIntent().getStringExtra("res");
        Gson gson = new Gson();
        User[] users = gson.fromJson(myResponse, User[].class);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();

            mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, personName, personEmail));
        }
        for(User u:users) {
            mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, u.username, u.email));
        }
        //System.out.println(res);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //changeItem(position, "Clicked");
                Intent intent = new Intent(UsersActivity.this, DetailActivity.class);
                String message = mExampleList.get(position).getText1();
                intent.putExtra("message", message);
                if(position == 0) {
                    intent.putExtra("flag", true);
                }else {
                    intent.putExtra("flag", false);
                }

                //intent.putExtra("adapter", mAdapter);
//                ExampleItem item = mExampleList.get(0);
//                intent.putExtra("myitem", item);
                closing = false;
                startActivity(intent);
                //closing = true;
            }
        });

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.logout_button:
                        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                        signOut();
                        break;
                    // ...
                }
            }
        });



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        //acct = null;
                        finish();
                        Intent intent = new Intent(UsersActivity.this, MainActivity.class);
//                        String message = mExampleList.get(position).getText1();
//                        intent.putExtra("message", message);
                        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        startActivity(intent);

                    }
                });
    }

    @Override
    protected void onResume() {
        //super.onRestart();
        super.onResume();
        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("myname")) {
            System.out.println("INSIDE ONRESUME CONDITION ---------------------------------------------");
            String updated = sharedPreferences.getString("myname", "ERROR");
            mExampleList.get(0).setText1(updated);
            mAdapter.notifyItemChanged(0);
        } else {
            mAdapter.notifyItemChanged(0);
        }

        //closing = false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        closing = true;
        sendNotification();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        Intent intent = new Intent();
////        intent.setAction(Intent.ACTION_MAIN);
////        intent.addCategory(Intent.CATEGORY_HOME);
////        startActivity(intent);
//        if (!this.isFinishing()){
//            //Insert your finishing code here
//
//            sendNotification();
//        }
//
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        closing = true;
    }

    public void sendNotification() {
        String msg = "Come Back";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UsersActivity.this, "My Notification");
        builder.setContentTitle("Missed You");
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);

        Intent intent = new Intent(UsersActivity.this,
                QuickActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("message", msg);

        PendingIntent pendingIntent = PendingIntent.getActivity(UsersActivity.this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(UsersActivity.this);
        managerCompat.notify("mytag",1,  builder.build());

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (!this.isFinishing() && closing){
//            //Insert your finishing code here
//            sendNotification();
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        //if (!this.isFinishing()){
        if(closing) {
            //Insert your finishing code here

            sendNotification();
        }
    }

//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if(keyCode == KeyEvent.KEYCODE_HOME)
//        {
//            Toast.makeText(UsersActivity.this,"Thanks for using application!!",Toast.LENGTH_LONG).show();
//            return true;
//        }
//        return false;
//    }

}