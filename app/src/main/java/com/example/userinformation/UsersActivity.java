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
import android.util.Log;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsersActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> mExampleList;
    private Button logoutButton;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;

    private RecyclerView mRecyclerView;

    //private RecyclerView.Adapter mAdapter;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences sharedPreferences;

    public static boolean closing = true;
    public static boolean inOnCreate = true;
    public static boolean firstTime = true;
    public static boolean comeFromBackGround=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Log.d("myTag3", "////////////////////////CREATE///////////////////////////");

        closing = true;
        mExampleList = new ArrayList<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new ExampleAdapter(mExampleList));

        getJSONs();


        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.logout_button:
                        signOut();
                        break;
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

    @Override
    protected void onResume() {
        //super.onRestart();
        super.onResume();
        Log.d("myTag", "ONRESUME CALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLED");
        if(firstTime == false) {
            Log.d("myTag1", "NOT --------- A ----------- FIRST ----------TIME");
            if(mExampleList != null) {
                Log.d("myTag2", String.valueOf(mExampleList.size()));
            }

            sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("myname") ) {

                String updated = sharedPreferences.getString("myname", "ERROR");
                mExampleList.get(0).setText1(updated);
                mAdapter.notifyItemChanged(0);
            } else {
                mAdapter.notifyItemChanged(0);
            }
        } else {
            getJSONs();
        }

    }


    public void getJSONs() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://jsonplaceholder.typicode.com/users";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).

                enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("************************************************************");

                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws
                            IOException {
                        if (response.isSuccessful()) {

                            final String myResponse = response.body().string();
                            connectRecycler(myResponse);


                        }
                    }
                });
    }



    public void connectRecycler(String res) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                User[] users = gson.fromJson(res, User[].class);
                acct = GoogleSignIn.getLastSignedInAccount(UsersActivity.this);
                if (acct != null) {
                    String personName = acct.getDisplayName();
                    String personEmail = acct.getEmail();
                    sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
                    if (sharedPreferences.contains("myname") ) {

                        personName = sharedPreferences.getString("myname", "ERROR");

                    }


                    mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, personName, personEmail));
                }
                for(User u:users) {
                    mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, u.username, u.email));
                }

                mAdapter = new ExampleAdapter(mExampleList);
                mRecyclerView.setAdapter(mAdapter);



                mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Intent intent = new Intent(UsersActivity.this, DetailActivity.class);
                        String message = mExampleList.get(position).getText1();
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        intent.putExtra("myname1", message);
                        editor.putString("myname1", message);

                        editor.apply();
                        if(position == 0) {
                            //intent.putExtra("flag", true);
                            editor.putBoolean("flag", true);
                        }else {
                            //intent.putExtra("flag", false);
                            editor.putBoolean("flag", false);
                        }

                        editor.apply();


                        closing = false;
                        startActivity(intent);
                        //closing = true;
                    }
                });

            }
        });
        inOnCreate =false;
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        closing = false;
                        finish();
                        Intent intent = new Intent(UsersActivity.this, MainActivity.class);

                        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        startActivity(intent);

                    }
                });
    }





    //invoke by coming back from details activity not by coming from background
    @Override
    protected void onRestart() {
        super.onRestart();
        closing = true;
        //firstTime = true;
        System.out.println("in on restart -----------------------------------------------------------restart");
        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("myname")) {

            String updated = sharedPreferences.getString("myname", "ERROR");
            mExampleList.get(0).setText1(updated);
            mAdapter.notifyItemChanged(0);

        } else {
            mAdapter.notifyItemChanged(0);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("in on stop ----------------------------------------------------------------");

        firstTime = true;
        if(closing) {
            sendNotification();
            comeFromBackGround = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel("mytag", 1);
    }


    public void sendNotification() {
        String msg = "Come Back";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UsersActivity.this, "My Notification");
        builder.setContentTitle("Missed You");
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);

        Intent intent = new Intent(UsersActivity.this,
                UsersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("message", msg);

        PendingIntent pendingIntent = PendingIntent.getActivity(UsersActivity.this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(UsersActivity.this);
        managerCompat.notify("mytag",1,  builder.build());

    }


}