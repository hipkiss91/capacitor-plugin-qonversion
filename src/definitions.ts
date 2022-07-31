/**
 * Here we define the methods in the QonversionPluginPlugin.java
 * that can be accessed by the application.
 */
export interface QonversionPluginPlugin {
  /** Launch the Qonversion instance with the provided `projectKey`, optionally enable `observerMode`. */
  launchWithKey(options: LaunchOptions): Promise<Result<LaunchResult>>;
  /** Set the flag to distinguish sandbox and production users. */
  setDebugMode(): Promise<void>;
  identify(options: IdentifyOptions): Promise<void>;
  logout(): Promise<void>;
  setUserProperty(options: PropertyOptions): Promise<void>;
  storeSDKInfo(options: SDKInfo): Promise<void>;
  /** Check the user receipt and return all the associated permissions. */
  checkPermissions(): Promise<Result<PermissionResult[]>>;
  /** Check if the user is eligible for an introductory offer of the products. */
  checkTrialIntroEligibilityForProductIds(
    options: ProductIds,
  ): Promise<Result<EligibilityResult[]>>;
  /** Perform a purchase for the product. */
  purchase(options: ProductId): Promise<Result<PermissionResult[]>>;
  restore(): Promise<Result<PermissionResult[]>>;
  syncPurchases(): Promise<void>;
  experiments(): Promise<Result<any[]>>;
  /** Retrieve a list of available products. */
  products(): Promise<Result<ProductResult[]>>;
  /** Retrieve a list of available offerings. */
  offerings(): Promise<Result<OfferingResult[]>>;
  // Notifications
  // setNotificationsToken(options: { token: string }): Promise<null>;
  // handleNotification(): Promise<any>;
  // 3rd Parties
  addAttributionData(options: AttributionData): Promise<any>;
}

// export interface AutomationsModule {
//     subscribe(): Promise<any>;
// }

export interface LaunchOptions {
  key: string;
  observerMode?: boolean;
}

export interface IdentifyOptions {
  userID: string;
}

export interface PropertyOptions {
  key: string;
  value: string;
}

export interface SDKInfo {
  sourceKey: string;
  source: string;
  sdkVersionKey: string;
  sdkVersion: string;
}

export interface ProductIds {
  products: string[];
}

export interface ProductId {
  productId: string;
}

export interface AttributionData {
  data: any;
  provider: AttributionSource;
}

export enum AttributionSource {
  AppsFlyer = 0,
  Branch = 1,
  Adjust = 2,
}

export interface Result<T> {
  data: T;
}

export interface LaunchResult {
  uid: string;
  timestamp: number;
  products: ProductResult[];
  permissions: PermissionResult[];
  userProducts: ProductResult[];
}

export interface EligibilityResult {
  productId: string;
  status: string;
  key: string;
}

export interface ProductResult {
  id: string;
  storeId: string;
  type: number;
  offeringId: string;
  duration: number;
  trialDuration: number;
  storeProduct: SkuResult;
  prettyPrice: string;
}

export interface SkuResult {
  description: string;
  freeTrialPeriod: string;
  iconUrl: string;
  introductoryPrice: string;
  introductoryPriceAmountMicros: number;
  introductoryPriceCycles: number;
  introductoryPricePeriod: string;
  originalJson: string;
  originalPrice: string;
  originalPriceAmountMicros: number;
  price: string;
  priceAmountMicros: number;
  priceCurrencyCode: string;
  sku: string;
  subscriptionPeriod: string;
  title: string;
  type: string;
  hashCode: number;
  toString: string;
}

export interface OfferingResult {
  id: string;
  main?: boolean;
  tag?: number;
  offeringProducts: ProductResult[];
}

export interface PermissionResult {
  id: string;
  associatedProduct: string;
  active: boolean;
  renewState:
    | 'NonRenewable'
    | 'Unknown'
    | 'WillRenew'
    | 'Canceled'
    | 'BillingIssue';
  startedTimestamp: number;
  expirationTimestamp: number;
  key: string;
}

export interface Error {
  message: string;
  code?: ErrorCode | string;
}

export enum ErrorCode {
  UnknownError,
  PlayStoreError,
  BillingUnavailable,
  PurchasePending,
  PurchaseUnspecified,
  PurchaseInvalid,
  CanceledPurchase,
  ProductNotOwned,
  ProductAlreadyOwned,
  FeatureNotSupported,
  ProductUnavailable,
  NetworkConnectionFailed,
  ParseResponseFailed,
  BackendError,
  ProductNotFound,
  OfferingsNotFound,
  LaunchError,
  SkuDetailsError,
  InvalidCredentials,
  InvalidClientUid,
  UnknownClientPlatform,
  FraudPurchase,
  ProjectConfigError,
  InvalidStoreCredentials,
}
