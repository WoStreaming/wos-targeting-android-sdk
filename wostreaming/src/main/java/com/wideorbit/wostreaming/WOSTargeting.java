package com.wideorbit.wostreaming;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * WOSTargeting collects the required parameters to append to the WO Streaming URL for advanced ad targeting.
 * An example for the usage is
 *
 *  public class YourActivity extends AppCompatActivity implements WOSObserver {
 *     @Override
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *
 *         // WOSTargeting.enableDebug(true);
 *
 *         WOSTargeting WOSDK = new WOSTargeting(getApplicationContext(), <ClientID>, true, this);
 *         WOSDK.execute();
 *     }
 *
 *     @Override
 *     public void onWOSTargetingReady(String queryParams) {
 *       ... Add queryParams to the end of your existing stream url
 *     }
 *  }
 *
 *  WOSTargeting WOSDK = new WOSTargeting(getApplicationContext(), XXXX, true, this);
 *  WOSDK.execute();
 */
public class WOSTargeting implements WOSCrowdControlObserver, AdvertisingInfoTaskObserver {

    // Constructor parameters
    private final WeakReference<Context> weakContext;
    private final int clientId;
    private final Boolean hasPrivacyPolicy;
    private final WOSObserver observer;

    private WOSQueryParams params;
    private WOSCrowdControl cc;

    private static boolean debug = false;
    static final String LOG_TAG = WOSTargeting.class.getSimpleName();

    /**
     * Initialize the WOSTargeting.
     *
     * @param weakContext Application context (ex this.getApplicationContext() from an activity)
     * @param clientId Your provided client ID.  Contact WO Streaming support to find your ID.
     * @param appHasPrivacyPolicy True if your application has a privacy policy
     * @param observer The WOSObserver object's onWOSTargetingReady() method will be called after execute() is called and finished processing.
     */
    public WOSTargeting(Context weakContext, int clientId, Boolean appHasPrivacyPolicy, WOSObserver observer) {
        this.weakContext = new WeakReference<>(weakContext);
        this.clientId = clientId;
        this.observer = observer;
        this.hasPrivacyPolicy = appHasPrivacyPolicy;
        this.params = new WOSQueryParams();
    }

    /**
     * Start the async calls to Lotame to collect advanced targeting information.
     * The WOSObserver provided to the constructor will have the method onWOSTargetingReady() when finished processing.
     */
    public void execute() {
        if (WOSTargeting.debug) Log.d(WOSTargeting.LOG_TAG, "Starting WOSTargeting data " +
                "collection");
        this.cc = new WOSCrowdControl(this.getContext(), this.clientId, WOSCrowdControl.Protocol.HTTPS, this);
        this.getApplicationInfo();
    }

    /**
     * Enable debug messages for development only.
     * @param debug Boolean indicating if debug is enabled
     */
    public static void enableDebug(boolean debug) {
        WOSTargeting.debug = debug;
        WOSCrowdControl.enableDebug(debug);
    }

    /**
     * Called when Lotame is successfully initialized and performs
     * the remaining Lotame data actions in order to obtain audience information.
     * CrowdControl will be initialized by this time.
     *
     *  When we don't get a Lotame response, it gets the advertiserID and isLimitedAdTracking
     *  settings using google identifier
     */
    @Override
    public void onCrowdControlReady() {
        try {
            this.cc.bcp();
        } catch (Exception e) {
            Log.e(WOSTargeting.LOG_TAG,
                    "Exception calling CrowdControl.bcp: e = " + e.toString());
        }

        // The Lotame response includes the advertiserID and isLimitedAdTracking value
        LotameAudienceResponse lotameAudienceResponse = cc.getAudience(5, TimeUnit.SECONDS);

        // If Lotame fails, collect the advertiser info and return.  AdvertisingInfoTask will do the callback.
        if (lotameAudienceResponse == null) {
            AdvertisingInfoTask t = new AdvertisingInfoTask(this.weakContext, this);
            t.execute();
            return;
        }

        this.setAdvertiserId(this.cc.getId());
        this.setIsLimitedAdTracking(this.cc.isLimitedAdTrackingEnabled());
        this.params.lptid = lotameAudienceResponse.Profile.tpid;
        this.params.ltids = lotameAudienceResponse.Profile.Audiences.Audience.
                stream()
                .map(a -> a.id)
                .collect(Collectors.joining(","));

        // Notify the observer and provide the URL Params string
        this.executeCallback();
    }

    /**
     * Callback for AdvertisingInfoTask to set device advertising settings.
     *
     * @param adInfo Populated object or null.
     */
    @Override
    public void setAdvertisingInfo(AdvertisingIdClient.Info adInfo) {
        this.setAdvertiserId(adInfo.getId());
        this.setIsLimitedAdTracking(adInfo.isLimitAdTrackingEnabled());
        this.executeCallback();
    }

    private void getApplicationInfo() {
        this.params.bundle = this.getContext().getPackageName();
        this.params.privacypolicy = this.hasPrivacyPolicy ? "1" : "0";
    }

    private Context getContext() {
        return this.weakContext.get();
    }

    private void setAdvertiserId(String advertiserId) {
        this.params.ifa = advertiserId;
    }

    private void setIsLimitedAdTracking(Boolean isLimitedAdTracking) {
        this.params.lmt = isLimitedAdTracking ? "1" : "0";
    }

    private void executeCallback() {
        this.observer.onWOSTargetingReady(this.params.toURLParams());
    }
}
