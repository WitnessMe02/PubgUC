package com.freeuc.earn.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.freeuc.earn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle t;
    private DrawerLayout drawer;
    private TextView deposit_tv;
    private TextView winning_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        deposit_tv = findViewById(R.id.wallet_balance_deposits);
        winning_tv = findViewById(R.id.wallet_balance_winnings);
        deposit_tv.setText("\u20B9 "+getDeposit());
        winning_tv.setText(getWinning()+" UC");
        initView();

        drawer = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.bringToFront();
            }
        };
        drawer.addDrawerListener(t);
        t.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.privacy_policy, R.id.terms_and_condition,
//                R.id.about_us, R.id.contact_us, R.id.exit)
//                .setDrawerLayout(drawer)
//                .build();

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


    public void initView(){
        findViewById(R.id.btn_add_money).setOnClickListener(this);
        findViewById(R.id.btn_withdraw).setOnClickListener(this);
        findViewById(R.id.i1).setOnClickListener(this);
        findViewById(R.id.i2).setOnClickListener(this);
        findViewById(R.id.i3).setOnClickListener(this);
        findViewById(R.id.i4).setOnClickListener(this);
        findViewById(R.id.i5).setOnClickListener(this);
        findViewById(R.id.i6).setOnClickListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            System.out.println("Hello");
            if (id == R.id.privacy_policy) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                intent.putExtra("name","Privacy Policy");
                intent.putExtra("file_name","privacy_policy.txt");
                startActivity(intent);
            } else if (id == R.id.terms_and_condition ){
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("name","Terms and Conditions");
                intent.putExtra("file_name","terms_and_conditions.txt");
                startActivity(intent);
            } else if (id == R.id.contact_us){
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("name","Contact us");
                intent.putExtra("file_name","contact_us.txt");
                startActivity(intent);
            } else if (id == R.id.about_us){
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("name","About us");
                intent.putExtra("file_name","about_us.txt");
                startActivity(intent);
            }
            else if (id == R.id.exit) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Exit Confirmation");
                alertDialogBuilder
                        .setMessage("Click yes to Exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        t.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        t.onConfigurationChanged(newConfig);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.i1:
                startActivity(new Intent(MainActivity.this,EarnMoreActivity.class));
                break;
            case R.id.i2:
                AlertDialog.Builder alert = new AlertDialog.Builder(this)
                        .setTitle("Buy Royal Pass")
                        .setMessage("After payment, you will receive a Pubg ID. You have to send a request of royal pass on it. Click Confirm to Pay.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(deposit<399){
                                    Snackbar.make(view,"Deposit Balance Insufficient.",Snackbar.LENGTH_LONG).show();
                                    dialogInterface.dismiss();
                                    return;
                                }
                                addDeposit(-399);
                                Snackbar.make(view,"Pubg ID: 979349998",Snackbar.LENGTH_INDEFINITE).setAction("Copy", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("Pubg ID", "979349998");
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
                break;
            case R.id.i3:
                startActivity(new Intent(MainActivity.this,BuyUCActivity.class));
                break;
            case R.id.i4:
                startActivity(new Intent(MainActivity.this,SpinActivity.class));
                break;
            case R.id.i5:
                startActivity(new Intent(MainActivity.this,ScriptActivity.class));
                break;
            case R.id.btn_add_money:
                startActivity(new Intent(MainActivity.this,AddMoneyActivity.class));
                break;
            case R.id.btn_withdraw:
                startActivity(new Intent(MainActivity.this,WithdrawActivity.class));
                break;
        }
    }
    public void refreshWalletUI(){
        ((TextView) findViewById(R.id.wallet_balance_deposits)).setText("\u20B9 "+getDeposit());
        ((TextView) findViewById(R.id.wallet_balance_winnings)).setText(getWinning()+" UC");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshWalletUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWalletUI();
    }
}
