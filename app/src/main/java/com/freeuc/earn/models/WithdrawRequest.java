package com.freeuc.earn.models;

import com.parse.ParseUser;

public class WithdrawRequest {
    private String pubgID;

    public String getPubgID() {
        return pubgID;
    }

    public void setPubgID(String pubgID) {
        this.pubgID = pubgID;
    }

    private long amount;
    private String email = ParseUser.getCurrentUser().getEmail();



//    public String getDate(){
////        Date date = new Date(timestamp);
//        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//        return dateFormat.format(date);
//    }



    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
