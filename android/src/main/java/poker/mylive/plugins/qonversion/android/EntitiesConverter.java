package poker.mylive.plugins.qonversion.android;

import com.android.billingclient.api.SkuDetails;

/*
   Capacitor Android Library
 */
import com.getcapacitor.JSObject;

/*
   Qonversion Android library
 */
import com.qonversion.android.sdk.dto.QLaunchResult;
import com.qonversion.android.sdk.dto.experiments.QExperimentInfo;
import com.qonversion.android.sdk.dto.offerings.QOffering;
import com.qonversion.android.sdk.dto.offerings.QOfferings;
import com.qonversion.android.sdk.dto.QPermission;
import com.qonversion.android.sdk.dto.products.QProduct;
import com.qonversion.android.sdk.dto.products.QProductDuration;
import com.qonversion.android.sdk.dto.eligibility.QEligibility;
import com.qonversion.android.sdk.dto.products.QTrialDuration;
import com.qonversion.android.sdk.automations.QActionResult;
import com.qonversion.android.sdk.QonversionError;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class EntitiesConverter {
    static JSObject mapLaunchWithKeySuccess(QLaunchResult launchResult) {
        JSONArray products = mapProducts(launchResult.getProducts());
        JSONArray permissions = mapPermissions(launchResult.getPermissions());
        JSONArray userProducts = mapProducts(launchResult.getUserProducts());

        JSObject result = new JSObject();
        result.put("uid", launchResult.getUid());
        result.put("timestamp", (double)launchResult.getDate().getTime());
        result.put("products", products);
        result.put("permissions", permissions);
        result.put("user_products", userProducts);

        return result;
    }

    /**
     * Products
     */
    static JSONArray mapProducts(Map<String, QProduct> products) {
        JSONArray data = new JSONArray();

        for (Map.Entry<String, QProduct> entry : products.entrySet()) {
            JSObject map = mapProduct(entry.getValue());
            data.put(map);
        }

        return data;
    }

    static JSObject mapProduct(QProduct product) {
        JSObject data = new JSObject();

        data.put("id", product.getQonversionID());
        data.put("store_id", product.getStoreID());
        data.put("type", product.getType().getType());

        String offeringId = product.getOfferingID();
        if (offeringId != null) {
            data.put("offeringId", offeringId);
        }

        QProductDuration duration = product.getDuration();
        if (duration != null) {
            data.put("duration", duration.getType());
        }

        QTrialDuration trialDuration = product.getTrialDuration();
        if (trialDuration != null) {
            data.put("trialDuration", trialDuration.getType());
        }

        SkuDetails skuDetails = product.getSkuDetail();
        if (skuDetails != null) {
            data.put("storeProduct", mapSkuDetails(product.getSkuDetail()));
            data.put("prettyPrice", product.getPrettyPrice());
        }

        return data;
    }

    /**
     * Permissions
     */
    static JSONArray mapPermissions(Map<String, QPermission> permissions){
        JSONArray data = new JSONArray();

        for (Map.Entry<String, QPermission> entry : permissions.entrySet()) {
            JSObject map = new JSObject();

            map.put("id", entry.getValue().getPermissionID());
            map.put("associated_product", entry.getValue().getProductID());
            map.put("active", entry.getValue().isActive());
            map.put("renew_state", entry.getValue().getRenewState());
            map.put("started_timestamp", (double)entry.getValue().getStartedDate().getTime());

            Date expirationDate = entry.getValue().getExpirationDate();
            if (expirationDate != null) {
                map.put("expiration_timestamp", (double)expirationDate.getTime());
            }

            map.put("key", entry.getKey());
            data.put(map);
        }

        return data;
    }

    /**
     * Offerings
     */
    static JSONArray mapOfferings(QOfferings offerings) {
        JSONArray result = new JSONArray();

        if (offerings.getMain() != null) {
            JSObject mainOffering = mapOffering(offerings.getMain());
            mainOffering.put("main", true);
            result.put(mainOffering);
        }

        for (QOffering offering : offerings.getAvailableOfferings()) {
            JSObject offeringFormatted = mapOffering(offering);
            result.put(offeringFormatted);
        }

        return result;
    }

    static JSObject mapOffering(QOffering offering) {
        JSObject result = new JSObject();
        result.put("id", offering.getOfferingID());

        Integer tagValue = offering.getTag().getTag();
        if (tagValue != null) {
            result.put("tag", tagValue);
        }

        JSONArray convertedProducts = new JSONArray();

        for (QProduct product : offering.getProducts()) {
            JSObject productFormatted = mapProduct(product);
            convertedProducts.put(productFormatted);
        }

        result.put("offeringProducts", convertedProducts);

        return result;
    }

    /**
     * Experiments
     */
    static JSONArray mapExperiments(Map<String, QExperimentInfo> experiments) {
        JSONArray result = new JSONArray();

        for (Map.Entry<String, QExperimentInfo> entry : experiments.entrySet()) {
            QExperimentInfo experimentInfo = entry.getValue();

            JSObject convertedExperimentInfo = new JSObject();
            JSObject convertedExperimentGroup = new JSObject();
            convertedExperimentGroup.put("type", 0);

            convertedExperimentInfo.put("id", experimentInfo.getExperimentID());
            convertedExperimentInfo.put("group", convertedExperimentGroup);
            convertedExperimentInfo.put("key", entry.getKey());

            result.put(convertedExperimentInfo);
        }

        return result;
    }

    /**
     * Sku Details
     */
    static JSObject mapSkuDetails(SkuDetails skuDetails) {
        JSObject data = new JSObject();

        data.put("description", skuDetails.getDescription());
        data.put("freeTrialPeriod", skuDetails.getFreeTrialPeriod());
        data.put("iconUrl", skuDetails.getIconUrl());
        data.put("introductoryPrice", skuDetails.getIntroductoryPrice());
        data.put("introductoryPriceAmountMicros", (double)skuDetails.getIntroductoryPriceAmountMicros());
        data.put("introductoryPriceCycles", skuDetails.getIntroductoryPriceCycles());
        data.put("introductoryPricePeriod", skuDetails.getIntroductoryPricePeriod());
        data.put("originalJson", skuDetails.getOriginalJson());
        data.put("originalPrice", skuDetails.getOriginalPrice());
        data.put("originalPriceAmountMicros", (double)skuDetails.getOriginalPriceAmountMicros());
        data.put("price", skuDetails.getPrice());
        data.put("priceAmountMicros", (double)skuDetails.getPriceAmountMicros());
        data.put("priceCurrencyCode", skuDetails.getPriceCurrencyCode());
        data.put("sku", skuDetails.getSku());
        data.put("subscriptionPeriod", skuDetails.getSubscriptionPeriod());
        data.put("title", skuDetails.getTitle());
        data.put("type", skuDetails.getType());
        data.put("hashCode", skuDetails.hashCode());
        data.put("toString", skuDetails.toString());

        return data;
    }

    /**
     * Eligibility
     */
    static JSONArray mapEligibility(Map<String, QEligibility> eligibilities) {
        JSONArray result = new JSONArray();

        for (Map.Entry<String, QEligibility> entry : eligibilities.entrySet()) {
            JSObject convertedEligibility = new JSObject();
            QEligibility eligibility = entry.getValue();

            convertedEligibility.put("productId", entry.getKey());
            convertedEligibility.put("status", eligibility.getStatus().getType());
            convertedEligibility.put("key", entry.getKey());

            result.put(convertedEligibility);
        }

        return result;
    }

    /**
     * Automations
     */
    static JSObject mapActionResult(QActionResult actionResult) {
        JSObject result = new JSObject();
        
        result.put("type", actionResult.getType().getType());
        result.put("error", mapQonversionError(actionResult.getError()));
        result.put("value", mapStringsMap(actionResult.getValue()));
        return result;
    }

    /**
     * Other Methods
     */
    static JSObject mapQonversionError(@Nullable QonversionError error) {
        if (error == null) {
            return null;
        }

        JSObject result = new JSObject();
        result.put("code", error.getCode().toString());
        result.put("description", error.getDescription());
        result.put("additionalMessage", error.getAdditionalMessage());
        return result;
    }

    static JSObject mapStringsMap(@Nullable Map<String, String> map) {
        if (map == null) {
            return null;
        }

        JSObject result = new JSObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    static HashMap<String, Object> convertJSObjectToHashMap(JSObject jsonObject) throws JSONException {
        return (HashMap<String, Object>)toMap(jsonObject);
    }

    static Map<String, Object> toMap(JSObject jsonobj)  throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = jsonobj.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSObject) {
                value = toMap((JSObject) value);
            }
            map.put(key, value);
        }   return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if (value instanceof JSObject) {
                value = toMap((JSObject) value);
            }
            list.add(value);
        }   return list;
    }
}
