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
import com.qonversion.android.sdk.AttributionSource;
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
import org.json.JSONException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.fragment.app.FragmentActivity;

@SuppressWarnings("unused")
@CapacitorPlugin(name = "QonversionPlugin")
public class QonversionPluginPlugin extends Plugin {
    private FragmentActivity activity;
    private QonversionError qonversionError;
    private QonversionSDKInfo sdkInfoToSave;

    private static final HashMap<String, QUserProperties> userPropertiesMap = new HashMap<String, QUserProperties>() {{
        put("EMAIL", QUserProperties.Email);
        put("NAME", QUserProperties.Name);
        put("APPS_FLYER_USER_ID", QUserProperties.AppsFlyerUserId);
        put("ADJUST_USER_ID", QUserProperties.AdjustAdId);
        put("KOCHAVA_DEVICE_ID", QUserProperties.KochavaDeviceId);
        put("CUSTOM_USER_ID", QUserProperties.CustomUserId);
        put("FACEBOOK_ATTRIBUTION", QUserProperties.FacebookAttribution);
    }};

    /*
       Initialisation
     */
    @PluginMethod
    public void launchWithKey(PluginCall call) {
        String key = call.getString("key");
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
        if (activity == null) {
            qonversionError = generateActivityError();
            String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
            call.reject(errorMessage, qonversionError.getCode().toString());
            return;
        }

        if (this.sdkInfoToSave != null) {
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
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
            }
        });
    }

    /*
       User Session Methods
     */
    @PluginMethod
    public void setDebugMode() {
        Qonversion.setDebugMode();
    }

    @PluginMethod
    public void identify(PluginCall call) {
        String userId = call.getString("userId");

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
    public void setProperty(PluginCall call) {
        String propertyKey = call.getString("propertyKey");
        String propertyValue = call.getString("propertyValue");

        if (propertyKey == null || propertyValue == null) {
            call.reject("Please provide the following values to use this method:", "{propertyKey}, {propertyValue}");
            return;
        }

        QUserProperties property = userPropertiesMap.get(propertyKey);

        if (property != null) {
            Qonversion.setProperty(property, propertyValue);
        }
    }

    @PluginMethod
    public void setUserProperty(PluginCall call) {
        String propertyKey = call.getString("propertyKey");
        String propertyValue = call.getString("propertyValue");

        if (propertyKey == null || propertyValue == null) {
            call.reject("Please provide the following values to use this method:", "{propertyKey}, {propertyValue}");
            return;
        }

        Qonversion.setUserProperty(propertyKey, propertyValue);
    }

    @PluginMethod
    public void storeSDKInfo(PluginCall call) {
        String sourceKey = call.getString("sourceKey");
        String source = call.getString("source");
        String sdkVersionKey = call.getString("sdkVersionKey");
        String sdkVersion = call.getString("sdkVersion");

        if (sourceKey == null || source == null || sdkVersionKey == null || sdkVersion == null) {
            call.reject("Please provide the following values to use this method:", "{sourceKey}, {source}, {sdkVersionKey}, {sdkVersion}");
            return;
        }

        Activity currentActivity = getActivity();
        QonversionSDKInfo sdkInfo = new QonversionSDKInfo(sourceKey, source, sdkVersionKey, sdkVersion);

        if (currentActivity == null) {
            this.sdkInfoToSave = sdkInfo;
            return;
        }

        storeSDKInfoToPreferences(sdkInfo, currentActivity);
    }

    /*
       Permissions
       Note please that the permission object will be available only in case the user made any purchase. In case there were no purchases you will receive an empty result.
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
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
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

        List<String> productsList = new ArrayList<>(products.length());
        for (int i = 0; i < products.length(); i++) {
            productsList.add(products.optString(i));
        }

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
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
            }
        });
    }

    /*
       Purchases
     */
    @PluginMethod
    public void purchase(PluginCall call) {
        String productId = call.getString("productId");

        if (productId == null) {
            call.reject("Please provide the following values to use this method:", "{productId}");
            return;
        }

        Activity currentActivity = getActivity();
        if (currentActivity == null) {
            qonversionError = generateActivityError();
            String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
            call.reject(errorMessage, qonversionError.getCode().toString());
            return;
        }

        Qonversion.purchase(currentActivity, productId, new QonversionPermissionsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QPermission> map) {
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapPermissions(map);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
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
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
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
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
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
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
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
                JSObject result = new JSObject();
                JSONArray data = EntitiesConverter.mapOfferings(offerings);
                result.put("data", data);
                call.resolve(result);
            }

            @Override
            public void onError(@NotNull QonversionError qonversionError) {
                String errorMessage = qonversionError.getDescription() + "\n" + qonversionError.getAdditionalMessage();
                call.reject(errorMessage, qonversionError.getCode().toString());
            }
        });
    }

    /**
     * Notifications
     */
    @PluginMethod
    public void setNotificationsToken(PluginCall call) {
        String token = call.getString("token");

        if (token == null) {
            call.reject("Please provide the following values to use this method:", "{token}");
            return;
        }

        Qonversion.setNotificationsToken(token);
    }

    @PluginMethod
    public void handleNotification(PluginCall call) {
        String notificationData = call.getString("notificationData");

        if (notificationData == null) {
            call.reject("Please provide the following values to use this method:", "{notificationData}");
        }
    }

    /**
     * 3rd Party Integrations
     */
    @PluginMethod
    public void addAttributionData(PluginCall call) {
        JSObject data = call.getObject("data");
        Integer provider = call.getInt("provider");

        if (data == null || provider == null) {
            call.reject("Please provide the following values to use this method:", "{notificationData}");
            return;
        }

        AttributionSource source = null;
        switch (provider) {
            case 0:
                source = AttributionSource.AppsFlyer;
                break;
            case 1:
                source = AttributionSource.Branch;
                break;
            case 2:
                source = AttributionSource.Adjust;
                break;
        }

        if (source == null) {
            call.reject("Please provide the following values to use this method:", "{notificationData}");
            return;
        }

        try {
            HashMap attributesHashMap = EntitiesConverter.convertJSObjectToHashMap(data);
            Qonversion.attribution(attributesHashMap, source);
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject("Failed to add attribution data to 3rd party.");
        }
    }

    /*
     * Supporting Methods
     */
    private QonversionError generateActivityError() {
        return new QonversionError(QonversionErrorCode.UnknownError, "Android current activity is null, cannot perform the process.");
    }

    private void storeSDKInfoToPreferences(QonversionSDKInfo sdkInfo, Activity currentActivity) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(currentActivity.getApplication()).edit();
        editor.putString(sdkInfo.sdkVersionKey, sdkInfo.sdkVersion);
        editor.putString(sdkInfo.sourceKey, sdkInfo.source);
        editor.apply();
    }
}
