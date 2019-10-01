package com.wideorbit.wostreaming;

import android.util.Log;

import com.google.common.base.Joiner;

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
		Map<String, String> m = new HashMap<>();
		m.put("lmt", this.lmt);
		m.put("ifa", this.ifa);
		m.put("bundle", this.bundle);
		m.put("privacypolicy", this.privacypolicy);

		if (this.lmt.equals("0")) {
			m.put("lptid", this.lptid);
			m.put("ltids", this.ltids);
		}

		String url = "";
		try {
			url = Joiner.on("&").withKeyValueSeparator("=").join(m);
		} catch (Exception e) {
			Log.e(WOSTargeting.LOG_TAG,
					"Exception creating url param string: e = " + e.toString());
		}
		return url;
	}
}