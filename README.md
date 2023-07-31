# capacitor-plugin-qonversion

### NB: Code for iOS has not been done for this project yet.

### NB: Been away for a year but updates and fixes will resume shortly

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

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

Here we define the methods in the QonversionPluginPlugin.java
that can be accessed by the application.

### launchWithKey(...)

```typescript
launchWithKey(options: { key: String; observerMode: Boolean; }) => any
```

| Param         | Type                                          |
| ------------- | --------------------------------------------- |
| **`options`** | <code>{ key: any; observerMode: any; }</code> |

**Returns:** <code>any</code>

--------------------


### setDebugMode()

```typescript
setDebugMode() => any
```

**Returns:** <code>any</code>

--------------------


### identify(...)

```typescript
identify(options: { userID: String; }) => any
```

| Param         | Type                          |
| ------------- | ----------------------------- |
| **`options`** | <code>{ userID: any; }</code> |

**Returns:** <code>any</code>

--------------------


### logout()

```typescript
logout() => any
```

**Returns:** <code>any</code>

--------------------


### setUserProperty(...)

```typescript
setUserProperty(options: { key: String; value: String; }) => any
```

| Param         | Type                                   |
| ------------- | -------------------------------------- |
| **`options`** | <code>{ key: any; value: any; }</code> |

**Returns:** <code>any</code>

--------------------


### storeSDKInfo(...)

```typescript
storeSDKInfo(options: { sourceKey: String; source: String; sdkVersionKey: String; sdkVersion: String; }) => any
```

| Param         | Type                                                                               |
| ------------- | ---------------------------------------------------------------------------------- |
| **`options`** | <code>{ sourceKey: any; source: any; sdkVersionKey: any; sdkVersion: any; }</code> |

**Returns:** <code>any</code>

--------------------


### checkPermissions()

```typescript
checkPermissions() => any
```

**Returns:** <code>any</code>

--------------------


### checkTrialIntroEligibilityForProductIds(...)

```typescript
checkTrialIntroEligibilityForProductIds(options: { products: Array<String>; }) => any
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ products: any; }</code> |

**Returns:** <code>any</code>

--------------------


### purchase(...)

```typescript
purchase(options: { productId: String; }) => any
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ productId: any; }</code> |

**Returns:** <code>any</code>

--------------------


### restore()

```typescript
restore() => any
```

**Returns:** <code>any</code>

--------------------


### syncPurchases()

```typescript
syncPurchases() => any
```

**Returns:** <code>any</code>

--------------------


### experiments()

```typescript
experiments() => any
```

**Returns:** <code>any</code>

--------------------


### products()

```typescript
products() => any
```

**Returns:** <code>any</code>

--------------------


### offerings()

```typescript
offerings() => any
```

**Returns:** <code>any</code>

--------------------


### addAttributionData(...)

```typescript
addAttributionData(options: { data: Object; provider: Number; }) => any
```

| Param         | Type                                       |
| ------------- | ------------------------------------------ |
| **`options`** | <code>{ data: any; provider: any; }</code> |

**Returns:** <code>any</code>

--------------------


### Interfaces


#### objectData

| Prop       | Type                |
| ---------- | ------------------- |
| **`data`** | <code>Object</code> |

</docgen-api>
