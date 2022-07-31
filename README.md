# capacitor-plugin-qonversion

Plugin to allow users in-app purchases and subscriptions via the use of [Qonversion](https://qonversion.io/)

## Install

```bash
npm install capacitor-plugin-qonversion
npx cap sync
```

## API

<docgen-index>

* [`launchWithKey(...)`](#launchwithkey)
* [`setDebugMode()`](#setdebugmode)
* [`identify(...)`](#identify)
* [`logout()`](#logout)
* [`setUserProperty(...)`](#setuserproperty)
* [`storeSDKInfo(...)`](#storesdkinfo)
* [`checkPermissions()`](#checkpermissions)
* [`checkTrialIntroEligibilityForProductIds(...)`](#checktrialintroeligibilityforproductids)
* [`purchase(...)`](#purchase)
* [`restore()`](#restore)
* [`syncPurchases()`](#syncpurchases)
* [`experiments()`](#experiments)
* [`products()`](#products)
* [`offerings()`](#offerings)
* [`addAttributionData(...)`](#addattributiondata)
* [Interfaces](#interfaces)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

Here we define the methods in the QonversionPluginPlugin.java
that can be accessed by the application.

### launchWithKey(...)

```typescript
launchWithKey(options: LaunchOptions) => Promise<Result<LaunchResult>>
```

Launch the Qonversion instance with the provided `projectKey`, optionally enable `observerMode`.

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#launchoptions">LaunchOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;<a href="#launchresult">LaunchResult</a>&gt;&gt;</code>

--------------------


### setDebugMode()

```typescript
setDebugMode() => Promise<void>
```

Set the flag to distinguish sandbox and production users.

--------------------


### identify(...)

```typescript
identify(options: IdentifyOptions) => Promise<void>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#identifyoptions">IdentifyOptions</a></code> |

--------------------


### logout()

```typescript
logout() => Promise<void>
```

--------------------


### setUserProperty(...)

```typescript
setUserProperty(options: PropertyOptions) => Promise<void>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#propertyoptions">PropertyOptions</a></code> |

--------------------


### storeSDKInfo(...)

```typescript
storeSDKInfo(options: SDKInfo) => Promise<void>
```

| Param         | Type                                        |
| ------------- | ------------------------------------------- |
| **`options`** | <code><a href="#sdkinfo">SDKInfo</a></code> |

--------------------


### checkPermissions()

```typescript
checkPermissions() => Promise<Result<PermissionResult[]>>
```

Check the user receipt and return all the associated permissions.

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;PermissionResult[]&gt;&gt;</code>

--------------------


### checkTrialIntroEligibilityForProductIds(...)

```typescript
checkTrialIntroEligibilityForProductIds(options: ProductIds) => Promise<Result<EligibilityResult[]>>
```

Check if the user is eligible for an introductory offer of the products.

| Param         | Type                                              |
| ------------- | ------------------------------------------------- |
| **`options`** | <code><a href="#productids">ProductIds</a></code> |

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;EligibilityResult[]&gt;&gt;</code>

--------------------


### purchase(...)

```typescript
purchase(options: ProductId) => Promise<Result<PermissionResult[]>>
```

Perform a purchase for the product.

| Param         | Type                                            |
| ------------- | ----------------------------------------------- |
| **`options`** | <code><a href="#productid">ProductId</a></code> |

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;PermissionResult[]&gt;&gt;</code>

--------------------


### restore()

```typescript
restore() => Promise<Result<PermissionResult[]>>
```

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;PermissionResult[]&gt;&gt;</code>

--------------------


### syncPurchases()

```typescript
syncPurchases() => Promise<void>
```

--------------------


### experiments()

```typescript
experiments() => Promise<Result<any[]>>
```

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;any[]&gt;&gt;</code>

--------------------


### products()

```typescript
products() => Promise<Result<ProductResult[]>>
```

Retrieve a list of available products.

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;ProductResult[]&gt;&gt;</code>

--------------------


### offerings()

```typescript
offerings() => Promise<Result<OfferingResult[]>>
```

Retrieve a list of available offerings.

**Returns:** <code>Promise&lt;<a href="#result">Result</a>&lt;OfferingResult[]&gt;&gt;</code>

--------------------


### addAttributionData(...)

```typescript
addAttributionData(options: AttributionData) => Promise<any>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#attributiondata">AttributionData</a></code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### Interfaces


#### Result

| Prop       | Type           |
| ---------- | -------------- |
| **`data`** | <code>T</code> |


#### LaunchResult

| Prop               | Type                |
| ------------------ | ------------------- |
| **`uid`**          | <code>string</code> |
| **`timestamp`**    | <code>number</code> |
| **`products`**     | <code>any[]</code>  |
| **`permissions`**  | <code>any[]</code>  |
| **`userProducts`** | <code>any[]</code>  |


#### LaunchOptions

| Prop               | Type                 |
| ------------------ | -------------------- |
| **`key`**          | <code>string</code>  |
| **`observerMode`** | <code>boolean</code> |


#### IdentifyOptions

| Prop         | Type                |
| ------------ | ------------------- |
| **`userID`** | <code>string</code> |


#### PropertyOptions

| Prop        | Type                |
| ----------- | ------------------- |
| **`key`**   | <code>string</code> |
| **`value`** | <code>string</code> |


#### SDKInfo

| Prop                | Type                |
| ------------------- | ------------------- |
| **`sourceKey`**     | <code>string</code> |
| **`source`**        | <code>string</code> |
| **`sdkVersionKey`** | <code>string</code> |
| **`sdkVersion`**    | <code>string</code> |


#### PermissionResult

| Prop                      | Type                                                                                    |
| ------------------------- | --------------------------------------------------------------------------------------- |
| **`id`**                  | <code>string</code>                                                                     |
| **`associatedProduct`**   | <code>string</code>                                                                     |
| **`active`**              | <code>boolean</code>                                                                    |
| **`renewState`**          | <code>'NonRenewable' \| 'Unknown' \| 'WillRenew' \| 'Canceled' \| 'BillingIssue'</code> |
| **`startedTimestamp`**    | <code>number</code>                                                                     |
| **`expirationTimestamp`** | <code>number</code>                                                                     |
| **`key`**                 | <code>string</code>                                                                     |


#### EligibilityResult

| Prop            | Type                |
| --------------- | ------------------- |
| **`productId`** | <code>string</code> |
| **`status`**    | <code>string</code> |
| **`key`**       | <code>string</code> |


#### ProductIds

| Prop           | Type                  |
| -------------- | --------------------- |
| **`products`** | <code>string[]</code> |


#### ProductId

| Prop            | Type                |
| --------------- | ------------------- |
| **`productId`** | <code>string</code> |


#### ProductResult

| Prop                | Type                                            |
| ------------------- | ----------------------------------------------- |
| **`id`**            | <code>string</code>                             |
| **`storeId`**       | <code>string</code>                             |
| **`type`**          | <code>number</code>                             |
| **`offeringId`**    | <code>string</code>                             |
| **`duration`**      | <code>number</code>                             |
| **`trialDuration`** | <code>number</code>                             |
| **`storeProduct`**  | <code><a href="#skuresult">SkuResult</a></code> |
| **`prettyPrice`**   | <code>string</code>                             |


#### SkuResult

| Prop                                | Type                |
| ----------------------------------- | ------------------- |
| **`description`**                   | <code>string</code> |
| **`freeTrialPeriod`**               | <code>string</code> |
| **`iconUrl`**                       | <code>string</code> |
| **`introductoryPrice`**             | <code>string</code> |
| **`introductoryPriceAmountMicros`** | <code>number</code> |
| **`introductoryPriceCycles`**       | <code>number</code> |
| **`introductoryPricePeriod`**       | <code>string</code> |
| **`originalJson`**                  | <code>string</code> |
| **`originalPrice`**                 | <code>string</code> |
| **`originalPriceAmountMicros`**     | <code>number</code> |
| **`price`**                         | <code>string</code> |
| **`priceAmountMicros`**             | <code>number</code> |
| **`priceCurrencyCode`**             | <code>string</code> |
| **`sku`**                           | <code>string</code> |
| **`subscriptionPeriod`**            | <code>string</code> |
| **`title`**                         | <code>string</code> |
| **`type`**                          | <code>string</code> |
| **`hashCode`**                      | <code>number</code> |
| **`toString`**                      | <code>string</code> |


#### OfferingResult

| Prop                   | Type                         |
| ---------------------- | ---------------------------- |
| **`id`**               | <code>string</code>          |
| **`main`**             | <code>boolean</code>         |
| **`tag`**              | <code>number</code>          |
| **`offeringProducts`** | <code>ProductResult[]</code> |


#### AttributionData

| Prop           | Type                                                            |
| -------------- | --------------------------------------------------------------- |
| **`data`**     | <code>any</code>                                                |
| **`provider`** | <code><a href="#attributionsource">AttributionSource</a></code> |


### Enums


#### AttributionSource

| Members         | Value          |
| --------------- | -------------- |
| **`AppsFlyer`** | <code>0</code> |
| **`Branch`**    | <code>1</code> |
| **`Adjust`**    | <code>2</code> |

</docgen-api>
