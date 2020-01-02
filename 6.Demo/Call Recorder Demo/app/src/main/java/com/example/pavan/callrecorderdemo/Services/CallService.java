package com.example.pavan.callrecorderdemo.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pavan.callrecorderdemo.R;

public class CallService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    View view;
    WindowManager.LayoutParams layoutParams;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();
        int flag = WindowManager.LayoutParams.TYPE_PHONE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }


        layoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                flag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER;


        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {


            view = inflater.inflate(R.layout.popup_layout, null);
            Button record = view.findViewById(R.id.record);
            Button cancel = view.findViewById(R.id.cancel);
            String text = intent.getStringExtra("phoneNumber") + "  is calling";


            windowManager.addView(view, layoutParams);

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
                    return true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(view);
//        throw new RuntimeException("DEstroy");
    }


    public void Record(View v) {
        Log.i("CLICKED ON", "Record");
    }

    public void Cancel(View view) {
        Log.i("CLICKED ON", "Cancel");
        onDestroy();
    }


}

//    public void outgoingRecord() {
//
//        Cursor c = getContentResolver().query(
//                android.provider.CallLog.Calls.CONTENT_URI,
//                null,
//                null,
//                null,
//                android.provider.CallLog.Calls.DATE + " DESC");
////        startManagingCursor(c);
//        int numberColumn = c.getColumnIndex(
//                android.provider.CallLog.Calls.NUMBER);
//        int dateColumn = c.getColumnIndex(
//                android.provider.CallLog.Calls.DATE);
//// type can be: Incoming, Outgoing or Missed
//        int typeColumn = c.getColumnIndex(
//                android.provider.CallLog.Calls.TYPE);
//        int durationColumn=c.getColumnIndex(
//                android.provider.CallLog.Calls.DURATION);
//// Will hold the calls, available to the cursor
//        ArrayList<String> callList = new ArrayList<String>();
//        try{
//            boolean moveToFirst=c.moveToFirst();
//            Log.e("MOVETOFIRST", "moveToFirst="+moveToFirst);
//        }
//
//        catch(Exception e)
//        {
//            Log.e("MOVETOFIRSTERROR","MOVETOFIRST Error="+e.toString());
//        }
//
//        String callerPhoneNumber = c.getString(numberColumn);
//        int callDate = c.getInt(dateColumn);
//        int callType = c.getInt(typeColumn);
//        int duration=c.getInt(durationColumn);
//        Log.d("CALLS", "callDate="+callDate);
//        switch(callType){
//            case android.provider.CallLog.Calls.INCOMING_TYPE:
//                Log.d("INCOMINGCALLLOG", "CallerPhoneNum="+
//                        callerPhoneNumber+" "+"Duration="+duration);
//                break;
//            case android.provider.CallLog.Calls.MISSED_TYPE:
//                break;
//            case android.provider.CallLog.Calls.OUTGOING_TYPE:
//                Log.d("OUTGOINGCALLLOG",
//                        "CallerPhoneNum="+ callerPhoneNumber+" "+"Duration="+duration);
//                break;
//        }
//
//    }
//}
