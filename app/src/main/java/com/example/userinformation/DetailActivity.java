package com.example.userinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        editText = findViewById(R.id.textViewRes);

//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        nm.cancelAsPackage("com.example.userinformation","mytag", 1);

        String message = getIntent().getStringExtra("message");

        //ExampleAdapter mAdapter = (ExampleAdapter) getIntent().getSerializableExtra("adapter");
        //ExampleItem myItem = (ExampleItem) getIntent().getSerializableExtra("myitem");
        editText.setText(message);
        //editText.seton
        boolean flag = getIntent().getBooleanExtra("flag", false);

        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        saveButton = findViewById(R.id.save_button);

        if(flag == false) {
            saveButton.setEnabled(false);
            editText.setEnabled(false);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updated = editText.getText().toString();
                editText.setText(updated);
                //myItem.setText1(updated);
                //mAdapter.notifyItemChanged(0);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("myname", updated);
                editor.apply();
                Toast.makeText(DetailActivity.this,"Saved Successfully",Toast.LENGTH_LONG).show();

            }
        });
    }
}