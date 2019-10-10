package com.wideorbit.wostreaming;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

class WOSQueryParams {
	// Device parameters
	String ifa = null;
	String lmt = "0";

	// Application parameters
	String bundle = null;
	String privacypolicy = null;

	// Lotame parameters
	String lptid = null;
	String ltids = null;

	String toURLParams() {
		String url = "";
		try {
			Uri.Builder builder = new Uri.Builder();
			builder.appendQueryParameter("lmt", this.lmt);
			builder.appendQueryParameter("ifa", this.ifa);
			builder.appendQueryParameter("bundle", this.bundle);
			builder.appendQueryParameter("privacypolicy", this.privacypolicy);

			if (this.lmt.equals("0")) {
				builder.appendQueryParameter("lptid", this.lptid);
				builder.appendQueryParameter("ltids", this.ltids);
			}

			url = builder.build().toString();
			url = url.replace("?", "&");

		} catch (Exception e) {
			Log.e(WOSTargeting.LOG_TAG,
					"Exception creating url param string: e = " + e.toString());
		}
		return url;
	}
}