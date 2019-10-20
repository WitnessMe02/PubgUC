package com.freeuc.earn.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;

public class BaseActivity extends AppCompatActivity {
    public static int deposit;
    public static int winning;
    private ParseUser user;
    BaseActivity() {
        user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        deposit = (int) user.get("deposit");
        winning = (int) user.get("winning");
    }

    public int getDeposit() {
        return deposit;
    }

    public void addDeposit(int amount) {
        deposit += amount;
        user.put("deposit",deposit);
        user.saveInBackground();
    }

    public int getWinning() {
        return winning;
    }

    public void addWinning(int amount) {
        winning += amount;
        user.put("winning",winning);
        user.saveInBackground();
    }
}
