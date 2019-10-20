package com.freeuc.earn.activities;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.freeuc.earn.R;
import com.freeuc.earn.models.WithdrawRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class WithdrawActivity extends BaseActivity {

    private TextInputEditText pubgID;
    private long amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        pubgID = findViewById(R.id.pubg_id);

        findViewById(R.id.btn_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(WithdrawActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                amount=0;
                if(winning<200){
                    Snackbar.make(v,"Minimum 200 UC required for withdrawal.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else {
                    amount = winning;
                }
                if(amount!=0){
                    final ProgressDialog progress = new ProgressDialog(WithdrawActivity.this);
                    progress.setMessage("Please wait...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();
                    WithdrawRequest withdrawRequest = new WithdrawRequest();
                    withdrawRequest.setAmount(amount);
                    withdrawRequest.setPubgID(pubgID.getText().toString());

                    ParseObject entity = new ParseObject("WithdrawRequests");

                    entity.put("PubgID", withdrawRequest.getPubgID());
                    entity.put("amount", amount);
                    entity.put("email", withdrawRequest.getEmail());
                    Date date = entity.getCreatedAt();
                    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                    formatter.setTimeZone(TimeZone.getTimeZone("IST"));
                    entity.put("time",formatter.format(date));

                    // Saves the new object.
                    // Notice that the SaveCallback is totally optional!
                    entity.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                addWinning((int) -amount);
                                refreshWalletUI();
                                progress.dismiss();
                                Snackbar.make(v,"Your Withdrawal Request successfully sent.", Snackbar.LENGTH_LONG).show();
                                pubgID.setText("");
                            }
                        }
                    });
                }
                else{
                    Snackbar.make(v,"Withdraw Request failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_add_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithdrawActivity.this, AddMoneyActivity.class);
                startActivity(intent);
            }
        });
        refreshWalletUI();
    }
    public void refreshWalletUI(){
        ((TextView) findViewById(R.id.wallet_balance_deposits)).setText("\u20B9 "+getDeposit());
        ((TextView) findViewById(R.id.wallet_balance_winnings)).setText(getWinning()+" UC");
    }
}
