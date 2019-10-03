package com.wideorbit.wostargetingandroidsdk;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;

import com.wideorbit.wostreaming.WOSObserver;
import com.wideorbit.wostreaming.WOSTargeting;

public class MainActivity extends Activity implements WOSObserver {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WOSTargeting WOSDK = new WOSTargeting(getApplicationContext(), 0, true, this);
		WOSTargeting.enableDebug(false);
		WOSDK.execute();
	}

	@Override
	public void onWOSTargetingReady(String queryParams) {
		Log.w("WOST: Add the following parameters to your stream url:", queryParams);
	}
}
