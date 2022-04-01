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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

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
     * 
     * @return Array {products}
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
            map.putString("offeringId", offeringId);
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
            JSONObject offeringFormatted = mapOffering(offering);
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
            JSONObject productFormatted = mapProduct(product);
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
     * Other Methods
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
}
