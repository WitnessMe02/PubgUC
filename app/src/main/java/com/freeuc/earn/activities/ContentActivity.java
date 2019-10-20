package com.freeuc.earn.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.freeuc.earn.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.storage.FileDownloadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ContentActivity extends AppCompatActivity {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getExtras().getString("name");
        String fileName = getIntent().getExtras().getString("file_name");
        getSupportActionBar().setTitle(name);

        final TextView txtView = (TextView)findViewById(R.id.text);
        txtView.setMovementMethod(new ScrollingMovementMethod());

//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        BufferedReader reader = null;
        try {
            assert fileName != null;
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName), StandardCharsets.UTF_8));

            // do reading, usually loop until end of file reading
            StringBuilder text= new StringBuilder();
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                text.append(mLine);
            }
            txtView.setText(text.toString());
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

    }

}
