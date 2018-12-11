# Liferay Analytics Android [![Codacy Badge](https://api.codacy.com/project/badge/Grade/aed9538a5fe047dfb7843a8466139d04)](https://www.codacy.com/app/liferay-mobile/liferay-analytics-android?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=liferay-mobile/liferay-analytics-android&amp;utm_campaign=Badge_Grade) [![Codacy Badge](https://api.codacy.com/project/badge/Coverage/aed9538a5fe047dfb7843a8466139d04)](https://www.codacy.com/app/liferay-mobile/liferay-analytics-android?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=liferay-mobile/liferay-analytics-android&amp;utm_campaign=Badge_Coverage) [![Build Status](https://travis-ci.org/liferay-mobile/liferay-analytics-android.svg?branch=master)](https://travis-ci.org/liferay-mobile/liferay-analytics-android)

## Analytics Core 


### Setup
In your app build.gradle add the dependency:

```kotlin
implementation 'com.liferay:liferay-analytics-android:x.x.x'
```
Change x.x.x for the latest version: [![Download](https://api.bintray.com/packages/liferay/liferay-mobile/liferay-analytics-android/images/download.svg) ](https://bintray.com/liferay/liferay-mobile/liferay-analytics-android/_latestVersion)


### How to use ?

#### Initialize the library
You should initialize the library providing your analytics key. If you don't initialize your library properly you will receive a runtime exception when you try to send an event, also you can't initialize your library twice. This will also happen if you try to initialize the library twice.

Parameters:
* context: Context (required)
* analyticsKey: String (required)
* flushInterval: int (optional, default is 60)

```kotlin
Analytics.configure(this, "key", 90)
```

It is recomended to do that inside your Application singleton: 
```kotlin
class MainApplication : Application() {

	override fun onCreate() {
		super.onCreate()

		Analytics.configure(this, "key", 90)
	}

}
```

#### Set identity
It is recommended identify your user after he logged in. This way, next events will be connected to his identity.

Parameters:
* email: String (required)
* name: String (optional)

```kotlin
 Analytics.setIdentity("ned.ludd@email.com", "Ned Ludd") 
 ```

 #### Clear the identity
It is recommended to clean the analytics session when the user logout. This is necessary to unbind the next events from the previous user.

```kotlin
Analytics.clearSession()
```

#### Send custom events
Method to send any custom event.

Parameters:
* eventId: String (required)
* applicationId: String (required)
* properties: Map<String: String> (optional). For additional properties

```kotlin
Analytics.send("eventId", "applicationId", hashMapOf("property" to "value", 
                                                    "property2" to "value2"))
```

## Analytics Forms Plugin


### Setup
In your app build.gradle add the dependency:

```kotlin
implementation 'com.liferay:liferay-analytics-forms-android:x.x.x'
annotationProcessor "com.liferay:liferay-analytics-forms-android:x.x.x"
```

Change x.x.x for the latest version: [ ![Download](https://api.bintray.com/packages/liferay/liferay-mobile/liferay-analytics-forms-android/images/download.svg) ](https://bintray.com/liferay/liferay-mobile/liferay-analytics-forms-android/_latestVersion)

### How to use?

#### Forms Attributes
It is a struct to contextualize forms events.

Parameters:
* formId: String (required)
* formTitle: String (optional)

```kotlin
 val formAttributes = FormAttributes("10", "People") 
 ```

#### Form Viewed
Method to send a form viewed event.

Parameters:
* attributes: FormAttributes (required)

```
Forms.formViewed(attributes: formAttributes)
```

#### Form Submit
Method to send a form submit event.

Parameters:
* attributes: FormAttributes (required)

```
Forms.formSubmitted(formAttributes)
```

#### Field Attributes
It is a struct to contextualize field events.

Parameters:
* name: String (required)
* title: String (optional)
* formAttributes: FormAttributes (required)

```kotlin
val fieldNameAttributes = FieldAttributes("nameField", "Name", formAttributes)
````

#### Tracking Fields
Method to track all events from the Field, like (focus and blur).

Parameters:
* field: EditText (required)
* fieldAttributes: FieldAttributes (required)

```kotlin
Forms.trackField(field, fieldNameAttributes)
```
