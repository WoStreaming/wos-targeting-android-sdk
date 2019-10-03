# WideOrbit Streaming Targeting SDK for Android

This library can be used by WideOrbit Streaming customers to obtain audio stream url parameters to increase ad targeting potential.

## Prerequisites

    minSdkVersion 28

    compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
    }

## Installation

This library is available via jitpack.io.

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the nd of the repositories

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

### Step 2. Add the dependency


    dependencies {
        implementation 'com.github.WoStreaming:wos-targeting-android-sdk:v1.0.4'
    }

##Usage

    import com.wideorbit.wostreaming.WOSObserver;
    import com.wideorbit.wostreaming.WOSTargeting;


    public class MainActivity extends AppCompatActivity implements WOSObserver {

    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.activity_main);

    		WOSTargeting WOSDK = new WOSTargeting(getApplicationContext(), XXXX, true, this);
    		WOSTargeting.enableDebug(false);
    		WOSDK.execute();
    	}

    	@Override
    	public void onWOSTargetingReady(String queryParams) {
    		Log.w("WOST: Add the following parameters to your stream url:", queryParams);
    	}
    }


#### Example response from WOSTargeting.getStreamUrlParams()
    https://live.wostreaming.net/manifest/xxxxxxxx-wkrpfmaac-hlsc3.m3u8?lmt=0&ifa=AA762328-E74C-49D0-B386-E646FC3B7301&bundle=com.wideorbit.WOSTargetingExample&privacypolicy=1&lptid=MAA762328-E74C-49D0-B386-E646FC3B7301&ltids=593088,511080,513421,99286
