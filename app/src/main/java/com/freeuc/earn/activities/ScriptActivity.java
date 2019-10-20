package com.freeuc.earn.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.freeuc.earn.R;
import com.freeuc.earn.adapters.ScriptAdapter;
import com.freeuc.earn.listeners.OnScriptItemClickListener;
import com.freeuc.earn.models.Script;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ScriptActivity extends BaseActivity implements OnScriptItemClickListener {
    ScriptAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Script> scripts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);
        scripts = new ArrayList<>();
        scripts.add(new Script("Wall Hacks",5,"https://wallhack.com"));
        scripts.add(new Script("God Mod",200,"https://godmod.com"));
        adapter = new ScriptAdapter(scripts,this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(Script script) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Buy Hacking Script")
                .setMessage("After payment, you will receive a Download link with Instructions. Click Confirm to Pay.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(deposit<script.getAmount()){
                            Snackbar.make(recyclerView,"Deposit Balance Insufficient.",Snackbar.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        addDeposit(-script.getAmount());
                        Snackbar.make(recyclerView,"Download Link: "+script.getLink(),Snackbar.LENGTH_INDEFINITE).setAction("Copy", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Download Link", script.getLink());
                                clipboard.setPrimaryClip(clip);
                            }
                        }).show();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alert.show();
    }
}
