package com.wideorbit.wostreaming;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

public interface AdvertisingInfoTaskObserver {
    void setAdvertisingInfo(AdvertisingIdClient.Info adInfo);
}
