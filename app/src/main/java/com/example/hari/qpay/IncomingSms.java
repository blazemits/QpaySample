package com.example.hari.qpay;
 import android.content.BroadcastReceiver;
 import android.content.Context;
 import android.content.Intent;
 import android.os.Bundle;
 import android.telephony.SmsMessage;
//Receving the OTP for Verification
 public class IncomingSms extends BroadcastReceiver
 {
 @Override
 public void onReceive(Context context, Intent intent)
 {

 final Bundle bundle = intent.getExtras();
   try {
 if (bundle != null)
 {
 final Object[] pdusObj = (Object[]) bundle.get("pdus");
 for (int i = 0; i < pdusObj .length; i++)
 {
 SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
 String senderNum = currentMessage.getDisplayOriginatingAddress();
 String message = currentMessage .getDisplayMessageBody();
  

 try
 {
 if (senderNum.equals("BP-080001")||senderNum.equals("VK-040060"))
 {
 Otp Sms = new Otp();
 Sms.recivedSms(message );
 }
 }
 catch(Exception e){}

 }
 }

 } catch (Exception e)
 {
 }
 }
 }

