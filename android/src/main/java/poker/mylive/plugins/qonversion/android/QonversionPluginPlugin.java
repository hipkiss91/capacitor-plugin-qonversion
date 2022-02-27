package poker.mylive.plugins.qonversion.android;

/*
  Capacitor Android Library
 */
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/*
  Qonversion Android library
 */
import com.qonversion.android.sdk.QUserProperties;
import com.qonversion.android.sdk.Qonversion;
import com.qonversion.android.sdk.QonversionEligibilityCallback;
import com.qonversion.android.sdk.QonversionError;
import com.qonversion.android.sdk.QonversionErrorCode;
import com.qonversion.android.sdk.QonversionExperimentsCallback;
import com.qonversion.android.sdk.QonversionLaunchCallback;
import com.qonversion.android.sdk.QonversionOfferingsCallback;
import com.qonversion.android.sdk.QonversionPermissionsCallback;
import com.qonversion.android.sdk.QonversionProductsCallback;
import com.qonversion.android.sdk.dto.QLaunchResult;
import com.qonversion.android.sdk.dto.experiments.QExperimentInfo;
import com.qonversion.android.sdk.dto.offerings.QOfferings;
import com.qonversion.android.sdk.dto.QPermission;
import com.qonversion.android.sdk.dto.products.QProduct;
import com.qonversion.android.sdk.dto.eligibility.QEligibility;

import org.json.JSONArray;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.fragment.app.FragmentActivity;

@CapacitorPlugin(name = "QonversionPlugin")
public class QonversionPluginPlugin extends Plugin {
    private FragmentActivity activity;
    private QonversionError qonversionError;
    private QonversionSDKInfo sdkInfoToSave;

    // private static final HashMap<Integer, QUserProperties> userPropertiesMap = new HashMap<Integer, QUserProperties>() {{
    //     put(0, QUserProperties.Email);
    //     put(1, QUserProperties.Name);
    //     put(2, QUserProperties.AppsFlyerUserId);
    //     put(3, QUserProperties.AdjustAdId);
    //     put(4, QUserProperties.KochavaDeviceId);
    //     put(5, QUserProperties.CustomUserId);
    //     put(6, QUserProperties.FacebookAttribution);
    // }};

    /*
       Initialisation
     */
    @PluginMethod
    public void launchWithKey(PluginCall call) {
        String key = call.getString("key", "");
        Boolean observerMode = call.getBoolean("observerMode", false);

        if (key == null) {
            call.reject("Please provide the following values as a String to use this method:", "{key}");
            return;
        }

        if (observerMode == null) {
            call.reject("The following field is a Boolean and only {true} or {false} values are accepted", "{observerMode}");
            return;
        }

        activity = getActivity();
        if (activity == null){
            qonversionError = generateActivityError();
            call.reject(qonversionError.getDescription(), qonversionError.getCode().toString());
            return;
        }

        if(this.sdkInfoToSave != null){
            storeSDKInfoToPreferences(this.sdkInfoToSave, activity);
        }

        Qonversion.launch(activity.getApplication(), key, observerMode, new QonversionLaunchCallback() {
            @Override
            public void onSuccess(@NotNull QLaunchResult qLaunchResult) {
                JSObject result = new JSObject();
                JSObject data = EntitiesConverter.mapLaunchWithKeySuccess(qLaunchResult);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    /*
       User Session Methods
     */
    @PluginMethod
    public void setUserId(PluginCall call) {
        String userId = call.getString("userId", "");

        if (userId == null) {
            call.reject("Please provide the following values to use this method:", "{userId}");
            return;
        }

        Qonversion.setUserID(userId);
    }

    @PluginMethod
    public void setDebugMode() {
        Qonversion.setDebugMode();
    }

    @PluginMethod
    public void resetUser() {
        Qonversion.resetUser();
    }

    @PluginMethod
    public void identify(PluginCall call) {
        String userId = call.getString("userId", "");

        if (userId == null) {
            call.reject("Please provide the following values to use this method:", "{userId}");
            return;
        }

        Qonversion.identify(userId);
    }

    @PluginMethod
    public void logout() {
        Qonversion.logout();
    }

    @PluginMethod
    public void setUserProperty(PluginCall call) {
        String propertyKey = call.getString("propertyKey", "");
        String propertyValue = call.getString("propertyValue", "");

        if (propertyKey == null || propertyValue == null) {
            call.reject("Please provide the following values to use this method:", "{propertyKey}, {propertyValue}");
            return;
        }

        Qonversion.setUserProperty(propertyKey, propertyValue);
    }

    @PluginMethod
    public void storeSDKInfo(PluginCall call) {
        String sourceKey = call.getString("sourceKey", "");
        String source = call.getString("source", "");
        String sdkVersionKey = call.getString("sdkVersionKey", "");
        String sdkVersion = call.getString("sdkVersion", "");

        if (sourceKey == null || source == null || sdkVersionKey == null || sdkVersion == null) {
            call.reject("Please provide the following values to use this method:", "{sourceKey}, {source}, {sdkVersionKey}, {sdkVersion}");
            return;
        }

        Activity currentActivity = getActivity();
        QonversionSDKInfo sdkInfo = new QonversionSDKInfo(sourceKey, source, sdkVersionKey, sdkVersion);

        if(currentActivity == null){
            this.sdkInfoToSave = sdkInfo;
            return;
        }

        storeSDKInfoToPreferences(sdkInfo, currentActivity);
    }

    /*
       Permissions
     */
    @PluginMethod
    public void checkPermissions(PluginCall call) {
        Qonversion.checkPermissions(new QonversionPermissionsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QPermission> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapPermissions(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    /*
       Check Trial Eligibility for a list of products.
     */
    @PluginMethod
    public void checkTrialIntroEligibilityForProductIds(PluginCall call) {
        JSONArray products = call.getArray("products");

        if (products == null) {
            call.reject("Please provide the following values to use this method:", "{products}");
            return;
        }

        List<String> productsList = new ArrayList(Arrays.asList(products));

        Qonversion.checkTrialIntroEligibilityForProductIds(productsList, new QonversionEligibilityCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QEligibility> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapEligibility(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    /*
       Purchases
     */
    @PluginMethod
    public void purchase(PluginCall call) {
        String productId = call.getString("productId", "");

        if (productId == null) {
            call.reject("Please provide the following values to use this method:", "{productId}");
            return;
        }

        Activity currentActivity = getActivity();
        if(currentActivity == null){
            qonversionError = generateActivityError();
            call.reject(qonversionError.getDescription(), qonversionError.getCode().toString());
            return;
        }

        Qonversion.purchase(currentActivity, productId, new QonversionPermissionsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String,QPermission> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapPermissions(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    @PluginMethod
    public void restore(PluginCall call) {
        Qonversion.restore(new QonversionPermissionsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QPermission> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapPermissions(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    @PluginMethod
    public void syncPurchases() {
        Qonversion.syncPurchases();
    }

    /*
       Experiments
     */
    @PluginMethod
    public void experiments(PluginCall call) {
        Qonversion.experiments(new QonversionExperimentsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QExperimentInfo> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapExperiments(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    /*
       Displaying Products
     */
    @PluginMethod
    public void products(PluginCall call) {
        Qonversion.products(new QonversionProductsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QProduct> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapProducts(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    /*
       Displaying Offerings
     */
    @PluginMethod
    public void offerings(PluginCall call) {
        Qonversion.offerings(new QonversionOfferingsCallback() {
            @Override
            public void onSuccess(@NotNull QOfferings offerings) {
                if (offerings.getMain() != null && !offerings.getMain().getProducts().isEmpty()) {
                    // Display products for sale
                    JSObject result = new JSObject();
                    JSONArray data = EntitiesConverter.mapOfferings(offerings);
                    result.put("data", data);
                    call.resolve(result);
                } else {
                    call.reject("No Offerings or Products have been configured in the Qonversion Product Centre.");
                }
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                call.reject(qonversionError.getCode().toString(), qonversionError.getDescription());
            }
        });
    }

    /*
       Supporting Methods
     */
    private QonversionError generateActivityError () {
        return new QonversionError(QonversionErrorCode.UnknownError, "Something went wrong.");
    }

    private void storeSDKInfoToPreferences(QonversionSDKInfo sdkInfo, Activity currentActivity){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(currentActivity.getApplication()).edit();
        editor.putString(sdkInfo.sdkVersionKey, sdkInfo.sdkVersion);
        editor.putString(sdkInfo.sourceKey, sdkInfo.source);
        editor.apply();
    }
}
