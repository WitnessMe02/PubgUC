package com.freeuc.earn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freeuc.earn.JSONParser;
import com.freeuc.earn.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMoneyActivity extends BaseActivity  implements PaytmPaymentTransactionCallback {

    private long deposits;
    //    private DatabaseReference depositsRef;
//    private FirebaseUser user;
    private long winnings;
    private TextInputEditText editAmount;
    private TextInputLayout mobile_til;
    private TextInputEditText mobile_et;
    private String custid = "";
    private String orderId = "";
    private String mid = "";
    //    private DatabaseReference orderReference;
    private String payment_gateway;
    //    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private ParseObject entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
//        user = FirebaseAuth.getInstance().getCurrentUser();
        custid = user.getObjectId();
        mid = ParseConfig.getCurrentConfig().getString("MerchantID");
//        depositsRef = FirebaseDatabase.getInstance().getReference("/users/"+user.getEmail()+"/Wallet/deposits");
        editAmount = findViewById(R.id.edit_amount);
        mobile_et = findViewById(R.id.edit_phone);
        mobile_til = findViewById(R.id.phoneInputLayout);

        findViewById(R.id.amount_50).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAmount.setText("50");
            }
        });
        findViewById(R.id.amount_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAmount.setText("100");
            }
        });
        findViewById(R.id.amount_200).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAmount.setText("200");
            }
        });
        findViewById(R.id.btn_deposit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAmount.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please enter valid value", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                int amount = Integer.valueOf(editAmount.getText().toString());
                if (amount < 20) {
                    Snackbar.make(v, "Minimum deposit limit is \u0B2920 ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                entity = new ParseObject("Payments");
                orderId = UUID.randomUUID().toString().split("-")[0];
                System.out.println("Chutiya "+orderId);

                PaytmPaymentGateway dl = new PaytmPaymentGateway(amount);
                dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//                orderReference= FirebaseDatabase.getInstance().getReference("/Orders").push();
//
//                if(payment_gateway.compareTo("paytm")==0){
//
//                }
//                else if(payment_gateway.compareTo("payumoney")==0){
//                    findViewById(R.id.btn_deposit).setEnabled(false);
//                    findViewById(R.id.phone_layout).setVisibility(View.VISIBLE);
//                }

            }
        });

        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.valueOf(editAmount.getText().toString());

//                    launchPayUMoneyFlow(amount);
            }
        });
        refreshWalletUI();
//        loadData();

    }

    public class PaytmPaymentGateway extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(AddMoneyActivity.this);
//        private String orderId , mid, custid, amt;
        String url ="https://pubguc-paytm.000webhostapp.com/generateChecksum.php";
        String verifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH ="";
        private int amount;

        public PaytmPaymentGateway(int amount) {
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(AddMoneyActivity.this);
            String param=
                    "&MID="+mid+
                            "&CUST_ID="+custid+
                            "&ORDER_ID=" + orderId+
                            "&INDUSTRY_TYPE_ID=Retail"+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+amount+"&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ verifyurl;
            System.out.println("Link: "+param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
           // PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
           PaytmPGService Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", String.valueOf(amount));
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,verifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(AddMoneyActivity.this, true, true,
                    AddMoneyActivity.this  );
        }
    }
    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
        if(bundle.getString("STATUS").compareTo("TXN_SUCCESS")==0){
            Toast.makeText(this,"Transaction Successful",Toast.LENGTH_SHORT).show();
            String checksum = bundle.getString("CHECKSUMHASH");
            int amount = Float.valueOf(bundle.getString("TXNAMOUNT")).intValue();
            addDeposit(amount);
            refreshWalletUI();
            entity.put("CustID", custid);
            entity.put("amount", amount);
            entity.put("OrderID", orderId);
            entity.saveInBackground();


//            orderReference.child("order_id").setValue(orderId);
//            orderReference.child("checksum").setValue(checksum);
//            depositsRef.setValue(deposits+);
        }
        else{
            Toast.makeText(this, "TXN_FAILED", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void networkNotAvailable() {
    }
    @Override
    public void clientAuthenticationFailed(String s) {
    }
    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }
    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }

    public void refreshWalletUI(){
        ((TextView) findViewById(R.id.wallet_balance_deposits)).setText("\u20B9 "+getDeposit());
        ((TextView) findViewById(R.id.wallet_balance_winnings)).setText(getWinning()+" UC");
    }

    public void loadData() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Updating Balance");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();


//        DatabaseReference settingsRef = FirebaseDatabase.getInstance().getReference("/Settings");
//        settingsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mid = (String) dataSnapshot.child("merchant_id").getValue();
//                payment_gateway = (String) dataSnapshot.child("payment_gateway").getValue();
//                System.out.println(payment_gateway);
//                progress.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        DatabaseReference walletRef = FirebaseDatabase.getInstance().getReference("/users/"+user.getEmail()+"/Wallet");
//        walletRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                deposits = (long) dataSnapshot.child("deposits").getValue();
//                winnings = (long) dataSnapshot.child("winnings").getValue();
//                ((TextView) findViewById(R.id.wallet_balance_deposits)).setText("\u20B9 "+deposits);
//                ((TextView) findViewById(R.id.wallet_balance_winnings)).setText("\u20B9 "+winnings);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}

    /*
    *
    *
    *
    * ////////////////////////////////////    PayUMoney  //////////////////////////////////////////////////
    *
    *
    *
    * */


//    private void launchPayUMoneyFlow(int amount) {
//
//        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
//
//        //Use this to set your custom text on result screen button
//        payUmoneyConfig.setDoneButtonText("Go Back");
//
//        //Use this to set your custom title for the activity
//        payUmoneyConfig.setPayUmoneyActivityTitle("Deposit Money");
//
//        payUmoneyConfig.disableExitConfirmation(false);
//
//        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
//
//        String txnId = "TXNID"+System.currentTimeMillis();
//        //String txnId = "TXNID720431525261327973";
//        String phone = mobile_et.getText().toString();
//        String productName = "Money Deposit";
//        String firstName = user.getDisplayName().split(" ",2)[0];
//        String email = user.getEmail();
//        String udf1 = "";
//        String udf2 = "";
//        String udf3 = "";
//        String udf4 = "";
//        String udf5 = "";
//        String udf6 = "";
//        String udf7 = "";
//        String udf8 = "";
//        String udf9 = "";
//        String udf10 = "";
//
//        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
//        builder.setAmount(String.valueOf(amount))
//                .setTxnId(txnId)
//                .setPhone(phone)
//                .setProductName(productName)
//                .setFirstName(firstName)
//                .setEmail(email)
//                .setsUrl(appEnvironment.surl())
//                .setfUrl(appEnvironment.furl())
//                .setUdf1(udf1)
//                .setUdf2(udf2)
//                .setUdf3(udf3)
//                .setUdf4(udf4)
//                .setUdf5(udf5)
//                .setUdf6(udf6)
//                .setUdf7(udf7)
//                .setUdf8(udf8)
//                .setUdf9(udf9)
//                .setUdf10(udf10)
//                .setIsDebug(appEnvironment.debug())
//                .setKey(appEnvironment.merchant_Key())
//                .setMerchantId(appEnvironment.merchant_ID());
//
//        try {
//            mPaymentParams = builder.build();
//
//            /*
//             * Hash should always be generated from your server side.
//             * */
////                generateHashFromServer(mPaymentParams);
//
//            /*            *//**
//             * Do not use below code when going live
//             * Below code is provided to generate hash from sdk.
//             * It is recommended to generate hash from server side only.
//             * */
//            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);
//            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,AddMoneyActivity.this, R.style.AppTheme_default,false);
//        } catch (Exception e) {
//            // some exception occurred
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//            findViewById(R.id.btn_deposit).setEnabled(true);
//
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result Code is -1 send from Payumoney activity
//        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
//        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
//                null) {
//            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
//                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
//
//            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);
//
//            // Check which object is non-null
//            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
//                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
//                    // Success Transaction
//                    depositsRef.setValue(deposits+Float.valueOf(editAmount.getText().toString()));
//                    editAmount.setText("");
//                    findViewById(R.id.phone_layout).setVisibility(View.GONE);
//                    findViewById(R.id.btn_deposit).setEnabled(true);
//                    Toast.makeText(this,"Transaction Successful",Toast.LENGTH_SHORT).show();
//                } else {
//                    // Failed Transaction
//                    Toast.makeText(this,"Transaction Failed",Toast.LENGTH_SHORT).show();
//                }
//
//                // Response from Payumoney
////                String payuResponse = transactionResponse.getPayuResponse();
////
////                // Response from SURl and FURL
////                String merchantResponse = transactionResponse.getTransactionDetails();
////
////                new AlertDialog.Builder(this)
////                        .setCancelable(false)
////                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
////                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int whichButton) {
////                                dialog.dismiss();
////                            }
////                        }).show();
//
//            } else if (resultModel != null && resultModel.getError() != null) {
//                Log.d("ERROR","Error response : " + resultModel.getError().getTransactionResponse());
//            } else {
//                Log.d("ERROR", "Both objects are null!");
//            }
//        }
//    }
//
//    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        HashMap<String, String> params = paymentParam.getParams();
//        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
//        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
//
//        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
//        stringBuilder.append(appEnvironment.salt());
//
//        String hash = hashCal(stringBuilder.toString());
//        paymentParam.setMerchantHash(hash);
//
//        return paymentParam;
//    }
//
//    public static String hashCal(String str) {
//        byte[] hashseq = str.getBytes();
//        StringBuilder hexString = new StringBuilder();
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
//            algorithm.reset();
//            algorithm.update(hashseq);
//            byte messageDigest[] = algorithm.digest();
//            for (byte aMessageDigest : messageDigest) {
//                String hex = Integer.toHexString(0xFF & aMessageDigest);
//                if (hex.length() == 1) {
//                    hexString.append("0");
//                }
//                hexString.append(hex);
//            }
//        } catch (NoSuchAlgorithmException ignored) {
//        }
//        return hexString.toString();
//    }
//
////    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
////        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.
////
////        HashMap<String, String> params = paymentParam.getParams();
////
////        // lets create the post params
////        StringBuffer postParamsBuffer = new StringBuffer();
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
////        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
////        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
////        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));
////
////        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
////
////        // lets make an api call
////        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
////        getHashesFromServerTask.execute(postParams);
////    }
////
////    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
////        private ProgressDialog progressDialog;
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            progressDialog = new ProgressDialog(AddMoneyActivity.this);
////            progressDialog.setMessage("Please wait...");
////            progressDialog.show();
////        }
////
////        @Override
////        protected String doInBackground(String... postParams) {
////
////            String merchantHash = "";
////            try {
////                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
////                URL url = new URL("https://payu.herokuapp.com/get_hash");
////
////                String postParam = postParams[0];
////
////                byte[] postParamsByte = postParam.getBytes("UTF-8");
////
////                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                conn.setRequestMethod("POST");
////                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
////                conn.setDoOutput(true);
////                conn.getOutputStream().write(postParamsByte);
////
////                InputStream responseInputStream = conn.getInputStream();
////                StringBuffer responseStringBuffer = new StringBuffer();
////                byte[] byteContainer = new byte[1024];
////                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
////                    responseStringBuffer.append(new String(byteContainer, 0, i));
////                }
////
////                JSONObject response = new JSONObject(responseStringBuffer.toString());
////
////                Iterator<String> payuHashIterator = response.keys();
////                while (payuHashIterator.hasNext()) {
////                    String key = payuHashIterator.next();
////                    switch (key) {
////                        /**
////                         * This hash is mandatory and needs to be generated from merchant's server side
////                         *
////                         */
////                        case "payment_hash":
////                            merchantHash = response.getString(key);
////                            break;
////                        default:
////                            break;
////                    }
////                }
////
////            } catch (MalformedURLException e) {
////                e.printStackTrace();
////            } catch (ProtocolException e) {
////                e.printStackTrace();
////            } catch (IOException e) {
////                e.printStackTrace();
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////            return merchantHash;
////        }
////
////        @Override
////        protected void onPostExecute(String merchantHash) {
////            super.onPostExecute(merchantHash);
////
////            progressDialog.dismiss();
////            findViewById(R.id.btn_deposit).setEnabled(true);
////
////            if (merchantHash.isEmpty() || merchantHash.equals("")) {
////                Toast.makeText(AddMoneyActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
////            } else {
////                mPaymentParams.setMerchantHash(merchantHash);
////                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, AddMoneyActivity.this, R.style.AppTheme_default, false);
////            }
////        }
////    }
//
//
//    public boolean validateDetails(String mobile) {
//        mobile = mobile.trim();
//
//        if (TextUtils.isEmpty(mobile)) {
//            setErrorInputLayout(mobile_et, getString(R.string.err_phone_empty), mobile_til);
//            return false;
//        } else if (!isValidPhone(mobile)) {
//            setErrorInputLayout(mobile_et, getString(R.string.err_phone_not_valid), mobile_til);
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public static void setErrorInputLayout(EditText editText, String msg, TextInputLayout textInputLayout) {
//        textInputLayout.setError(msg);
//        editText.requestFocus();
//    }
//
//    public static boolean isValidPhone(String phone) {
//        String PHONE_PATTERN = "^[9876]\\d{9}$";
//        Pattern pattern = Pattern.compile(PHONE_PATTERN);
//        Matcher matcher = pattern.matcher(phone);
//        return matcher.matches();
//    }
//
//    protected String concatParams(String key, String value) {
//        return key + "=" + value + "&";
//    }
//}
