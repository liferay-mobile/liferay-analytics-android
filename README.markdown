# Liferay Analytics Android

## Analytics Core

### Setup
In your app build.gradle add the dependency:

```kotlin
imlementation 'com.liferay:liferay-analytics-android:1.0.0'
```

### How to use ?

#### Initialize the library
You should initialize the lib passing your analytics key. If you don't initialize your library you will receive a runtime exception when you try to send a event, also you can't initialize your library twice.

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
It is recomended to call when the user is logged in, necessary to bind the next events for this user. The name parameter is optional.

Parameters:
* email: String (required)
* name: String (optional)

```kotlin
 Analytics.setIdentity("ned.ludd@email.com", "Ned Ludd") 
 ```

 #### Clear the identity
 It is recomended to call when the user is logged out, necessary to unbind the next events of the previous user.

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
imlementation 'com.liferay:liferay-analytics-forms-android:1.0.0'
```

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
Forms.trackField(nameField, fieldNameAttributes)
```
