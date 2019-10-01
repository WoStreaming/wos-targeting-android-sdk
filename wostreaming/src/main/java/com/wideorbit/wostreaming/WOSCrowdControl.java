package com.wideorbit.wostreaming;

import android.content.Context;
import android.util.Log;

import com.lotame.android.CrowdControl;

import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

public class WOSCrowdControl extends CrowdControl {
	private final WOSCrowdControlObserver observer;

	WOSCrowdControl(Context ctx, int clientId, Protocol protocol, WOSCrowdControlObserver observer) {
		super(ctx, clientId, protocol);
		this.observer = observer;
	}

	@Override
	public void startSession() {
		super.startSession();
		this.observer.onCrowdControlReady();
	}

	LotameAudienceResponse getAudience(long timeout, TimeUnit timeUnit) {
		try {
			String lotameJSONResponse = super.getAudienceJSON(timeout, timeUnit);

			Gson gson = new Gson();
			return gson.fromJson(lotameJSONResponse, LotameAudienceResponse.class);
		} catch (Exception e) {
			Log.e(WOSTargeting.LOG_TAG,
					"Exception obtaining Lotame audience data: e = " + e.toString());
			return null;
		}
	}
}
