package com.wideorbit.wostreaming;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.lang.ref.WeakReference;

public class AdvertisingInfoTask extends AsyncTask<Void, Void, AdvertisingIdClient.Info> {
    private final WeakReference<Context> weakContext;
    private final AdvertisingInfoTaskObserver taskObserver;

    AdvertisingInfoTask(WeakReference<Context> context, AdvertisingInfoTaskObserver taskObserver) {
        this.taskObserver = taskObserver;
        this.weakContext = context;
    }

    @Override
    protected AdvertisingIdClient.Info doInBackground(Void... params) {
        AdvertisingIdClient.Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.weakContext.get());
        } catch (Exception e) {
            Log.e(WOSTargeting.LOG_TAG,
                    "Exception obtaining AdvertisingClientInfo: e = " + e.toString());
        }
        return adInfo;
    }

    @Override
    protected void onPostExecute(AdvertisingIdClient.Info adInfo) {
        taskObserver.setAdvertisingInfo(adInfo);
    }
}