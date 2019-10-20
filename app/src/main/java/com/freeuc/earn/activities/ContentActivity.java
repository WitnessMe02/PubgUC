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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

//        StorageReference islandRef = storageRef.child(fileName);
        final long ONE_MEGABYTE = 1024 * 1024;
        File localFile = null;
        try {
            localFile = File.createTempFile(name,"txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
//        islandRef.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                InputStream inputStream = null;
//                try {
//                    inputStream = new FileInputStream(finalLocalFile);
//                    int i;
//                    i = inputStream.read();
//                    while (i != -1)
//                    {
//                        byteArrayOutputStream.write(i);
//                        i = inputStream.read();
//                    }
//                    inputStream.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                txtView.setText(byteArrayOutputStream.toString());
//                progress.dismiss();
//            }
//        });

    }

}
