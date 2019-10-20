package com.freeuc.earn.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.freeuc.earn.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;

public class BuyUCActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_uc);
        initview();

    }
    public void initview(){
        findViewById(R.id.i2).setOnClickListener(this);
        findViewById(R.id.i3).setOnClickListener(this);
        findViewById(R.id.i4).setOnClickListener(this);
        findViewById(R.id.i5).setOnClickListener(this);
        findViewById(R.id.i6).setOnClickListener(this);
    }

    public void showAlert(int rs,int uc){
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setMessage("Click confirm to buy.")
                .setTitle("Buy UC")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addDeposit(-rs);
                        addWinning(uc);
                        Toast.makeText(BuyUCActivity.this,"Payment Done",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.i2:
                if(deposit<200){
                    Snackbar.make(view,"Minimum Rs200 required in deposits to buy this.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    showAlert(200,350);
                }
                break;
            case R.id.i3:
                if(deposit<400){
                    Snackbar.make(view,"Minimum Rs400 required in deposits to buy this.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    showAlert(400,700);
                }
                break;
            case R.id.i4:
                if(deposit<600){
                    Snackbar.make(view,"Minimum Rs600 required in deposits to buy this.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                   showAlert(600,1150);
                }
                break;
            case R.id.i5:
                if(deposit<800){
                    Snackbar.make(view,"Minimum Rs800 required in deposits to buy this.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    showAlert(800,1550);
                }
                break;
            case R.id.i6:
                if(deposit<1000){
                    Snackbar.make(view,"Minimum Rs1000 required in deposits to buy this.",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    showAlert(1000,2000);
                }
                break;

        }
    }
}
