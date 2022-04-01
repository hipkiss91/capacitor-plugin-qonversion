/**
 * Here we define the methods in the QonversionPluginPlugin.java
 * that can be accessed by the application.
 */
export interface QonversionPluginPlugin {
    // Initialisation
    launchWithKey(options: {key: String, observerMode: Boolean}): Promise<objectData>;
    // User Session Methods
    setDebugMode(): Promise<void>;
    resetUser(): Promise<void>;
    identify(options: {userID: String}): Promise<void>;
    logout(): Promise<void>;
    setUserProperty(options: {key: String, value: String}): Promise<void>;
    storeSDKInfo(options: {sourceKey: String, source: String, sdkVersionKey: String, sdkVersion: String}): Promise<any>;
    // Permissions
    checkPermissions(): Promise<any>;
    // Eligibility
    checkTrialIntroEligibilityForProductIds(options: { products: Array<String>}): Promise<any>;
    // Purchases
    purchase(options: {productId: String}): Promise<any>;
    restore(): Promise<void>;
    syncPurchases(): Promise<void>;
    // Experiments
    experiments(): Promise<any>;
    // Products
    products(): Promise<any>;
    // Offerings
    offerings(): Promise<any | null>;
}

export interface objectData {
    data: Object;
}
