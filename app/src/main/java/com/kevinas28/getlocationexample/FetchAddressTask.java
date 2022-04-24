package com.kevinas28.getlocationexample;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private Context mContext;
    private OnTaskCompleted mListener;

    interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }

    public FetchAddressTask(Context ctx, OnTaskCompleted listener) {
        mContext = ctx;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Location loc = locations[0];
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses = null;
        String resMsg = "";

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses == null || addresses.size() == 0) {
                resMsg = "Address not found";
            } else {
                Address address = addresses.get(0);
                ArrayList<String> parts = new ArrayList<>();
                for (int i=0; i <= address.getMaxAddressLineIndex(); i++) {
                    parts.add(address.getAddressLine(i));
                }

                resMsg = TextUtils.join("\n", parts);
            }

        } catch (IOException ex) {
            Log.d("Exception", "FetchAddressTask.doInBackground: Unable to get location");
        }
        return resMsg;
    }

    @Override
    protected void onPostExecute(String s) {
        mListener.onTaskCompleted(s);
        super.onPostExecute(s);
    }
}
