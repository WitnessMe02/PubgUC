package com.freeuc.earn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adefruandta.spinningwheel.SpinningWheelView;
import com.freeuc.earn.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.Arrays;
import java.util.Random;

public class SpinActivity extends BaseActivity {

    MediaPlayer spinSound;
    MediaPlayer coinSound;
    Button spinButton;
    private TextView titleTextView;
    private SpinningWheelView wheelView;
//    private Long deposits;
//    private Long winnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
//        user = ParseUser.getCurrentUser();
//        wallet = FirebaseDatabase.getInstance().getReference("/users/"+user.getEmail()+"/Wallet");
        titleTextView = findViewById(R.id.title);
        wheelView = findViewById(R.id.wheel);
        wheelView.setEnabled(false);
        String[] items = {"06","40","45","8","50","60","02","10","20","04","25","30"};
        items = shuffleArray(items);
        wheelView.setItems(Arrays.asList(items));
        spinSound = MediaPlayer.create(this, R.raw.spinsoundeffect);
        coinSound = MediaPlayer.create(this, R.raw.coin);
        spinButton = findViewById(R.id.spin_button);
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deposit<10){
                    Snackbar.make(v,"Minimum Rs10 requried in deposits to spin",Snackbar.LENGTH_SHORT).show();
                }
                else{
//                    wallet.child("deposits").setValue(deposits-10);
                    addDeposit(-10);
                    refreshWalletUI();
                    wheelView.rotate(30, 6000, 50);
                }

            }
        });
        wheelView.setOnRotationListener(new SpinningWheelView.OnRotationListener<String>() {
            // Call once when start rotation
            @Override
            public void onRotation() {
                spinButton.setEnabled(false);
                spinSound = MediaPlayer.create(SpinActivity.this, R.raw.spinsoundeffect);
                spinSound.start();
            }

            @Override
            public void onStopRotation(String item) {
                spinButton.setEnabled(true);
                spinSound.stop();
                addWinning(Integer.valueOf(wheelView.getSelectedItem()));
                refreshWalletUI();
//                wallet.child("winnings").setValue(winnings+Integer.valueOf(wheelView.getSelectedItem()));
                showWinningDialog(wheelView.getSelectedItem());
            }
        });
        String text = titleTextView.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        final int start = 0;
        final int end = text.length();
        spannableString.setSpan(new RainbowSpan(this), start, end, 0);
        titleTextView.setText(spannableString);
        findViewById(R.id.btn_add_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpinActivity.this, AddMoneyActivity.class);
                startActivity(intent);
            }
        });
        refreshWalletUI();
//        loadData();
    }

    public void loadData(){
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Updating Balance");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();


//        DatabaseReference settingsRef = FirebaseDatabase.getInstance().getReference();
//        settingsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataSnapshot wallet = dataSnapshot.child("users").child(user.getEmail()).child("Wallet");
//                deposits = (long) wallet.child("deposits").getValue();
//                winnings = (long) wallet.child("winnings").getValue();
//                ((TextView) findViewById(R.id.wallet_balance_deposits)).setText("\u20B9 "+deposits);
//                ((TextView) findViewById(R.id.wallet_balance_winnings)).setText("\u20B9 "+winnings);
//                progress.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
    }

    private static class RainbowSpan extends CharacterStyle implements UpdateAppearance {
        private final int[] colors;
        public RainbowSpan(Context context) {
            colors = context.getResources().getIntArray(R.array.rainbow);
        }

        public void updateDrawState(TextPaint paint) {
            paint.setStyle(Paint.Style.FILL);
            Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors,
                    null, Shader.TileMode.MIRROR);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            shader.setLocalMatrix(matrix);
            paint.setShader(shader);
        }
    }

    private void showWinningDialog(Object selectedItem) {
        FancyGifDialog build = new FancyGifDialog.Builder(this)
                .setTitle("Congrats!!, You have won " + selectedItem + " UC.")
                .setMessage("Amount will be added to your winnings.")
                .setGifResource(R.drawable.trophy)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .setPositiveBtnText("Spin More")
                .isCancellable(false)
                .setNegativeBtnText("Cancel").OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                }).build();
        coinSound.start();
    }
    private String[] shuffleArray(String[] items){
        String[] res = new String[items.length];
        Random rand = new Random();
        int offset = rand.nextInt(11);
        for(int i=0;i<items.length;i++){
            res[i]=items[(i+offset)%items.length];
        }
        return res;
    }

    public void refreshWalletUI(){
        ((TextView) findViewById(R.id.wallet_balance_deposits)).setText("\u20B9 "+getDeposit());
        ((TextView) findViewById(R.id.wallet_balance_winnings)).setText(getWinning()+" UC");
    }
}
