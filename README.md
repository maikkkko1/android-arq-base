
# Android Base Architecture

## What is it?
A library that contains the base MVVM architecture (and more) for Android projects.

## Installation

Last releases: https://github.com/maikkkko1/android-arq-base/releases

```groovy
 implementation 'com.github.maikkkko1:android-arq-base:$version'
```

## Available Extensions

### Activity Extensions
```kotlin
// Opens a URL in your browser.
AppCompatActivity.openBrowserUrl(url: String)

// Called to observe any live data from your view model.
AppCompatActivity.bindData(observable: LiveData<T>, block: (result: T) -> Unit)

// Called to observe any component state from your view model.
AppCompatActivity.bindState(observable: LiveData<ComponentState<T>>, block: (result: ComponentState<T>) -> Unit)

// Called to observe all commands sent from your view model.
AppCompatActivity.bindCommand(viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit)
```

### AlertDialog Extensions
```kotlin
// Sets the full height in an AlertDialog.
AlertDialog.setFullHeight()
```

### Button Extensions
```kotlin
// Disables a button changing the title (optional). Sets alpha to 0.8f.
Button.disableButton(newTitle: String? = null)

// Enables a button changing the title (optional). Sets alpha to 1f.
Button.enableButton(newTitle: String? = null)
```

### Context Extensions
```kotlin
// Returns the current activity or throw an exception.
Context.getActivity(): AppCompatActivity

// Converts DP to PX.
Context.dpToPx(dp: Int): Int

// Checks if the app is in the foreground.
Context.isAppInForeground(): Boolean
```

### Fragment Extensions
```kotlin
// Called to observe any live data from your view model.
Fragment.bindData(observable: LiveData<T>, block: (result: T) -> Unit)

// Called to observe any component state from your view model.
Fragment.bindState(  observable: LiveData<ComponentState<T>>, block: (result: ComponentState<T>) -> Unit)

// Called to observe all commands sent from your view model.
Fragment.bindCommand(viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit)

// Navigates back on the current navigation stack. (popBackStack)
Fragment.navigateBack(finishIfNoBackStack: Boolean = false)

// Navigates back to a given destination. (popBackStack)
Fragment.navigateBackTo(destination: Int, finishIfNoBackStack: Boolean = false)

// Navigates to a given destination. (safe navigation).
Fragment.navigateTo(destination: Int, bundle: Bundle? = null)

// Hides the keyboard.
Fragment.closeKeyboard()

// Returns a drawable resource.
Fragment.getResourcesDrawable(id: Int): Drawable?

// Displays a simple toast message.
Fragment.showToast(text: String)

// Displays a simple snack message.
Fragment.showSnack(text: String)

// Returns the current container activity.
Fragment.getContainerActivity(): T

// Returns a GridLayoutManager.
Fragment.getRecyclerLayoutManager(tabletSpanCount: Int, isTablet: Boolean): GridLayoutManager?

// Returnsa color resource.
Fragment.getResourcesColor(id: Int): Int

// Displays a "Please check again" simple dialog.
Fragment.showIncorrectInfoDialog(text: String? = null)

// Displays a "Oops" error simple dialog.
Fragment.showErrorDialog(text: String? = null)

// Returns if the app is with the notifications enabled (on Android configurations).
Fragment.isAppNotificationsEnabled(): Boolean

// Returns a new instance of the CustomAlertDialog class.
Fragment.getCustomAlertDialog()

// Opens the android gallery picker.
Fragment.openGallery(actionCode: Int, resourceType: String = "image")

// Opens the android camera to take a photo.
Fragment.openTakePhoto(actionCode: Int)

// Returns the ID from a view object.
Fragment.getViewId(id: String): Int
```

### ImageView Extensions
```kotlin
// Loads an image from a URL into a ShapeableImageView.
ShapeableImageView.loadFromUrl(url: String)

// Loads an image from a URL into a ImageView.
ImageView.loadFromUrl(url: String)

// Loads an video thumbnail from a URL into a ImageView.
ImageView.loadVideoUrlThumbnail(url: String, useCache: Boolean, onThumbLoaded: (() -> Unit)? = null)
```

### List Extensions
```kotlin
// Returns if all the elements from the list are null.
List<Any?>.isAllNull(): Boolean

// Returns if all the elements (String) from the list are null or empty.
List<String?>.isAllNullOrEmpty(): Boolean
```

### RecyclerView Extensions
Check the extension file: [recyclerView.kt](https://github.com/maikkkko1/android-arq-base/blob/master/android-base-arq-lib/src/main/java/com/maikkkko1/android_base_arq/arq/extensions/recyclerView.kt)

### String Extensions
```kotlin
// Pluralizes the string. Ex: "day".pluralize(2, "s") -- output: "days"
String.pluralize(count: Int, plural: String? = null): String
```

### TextInputEditText Extensions
```kotlin
// Returns the string value of a TextInputEditText.
TextInputEditText.getValue(): String

// Forces a TextInputEditText to caps.
TextInputEditText.forceToCaps()

// Adds a suffix to a TextInputEditText.
TextInputEditText.addSuffix(suffix: String)

// Returns the string value of a TextInputEditText after textChanged event.
TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit)
```

### View Extensions
```kotlin
// Detects when the view are finished rendering.
<T: View> T.afterMeasured(crossinline f: T.() -> Unit)
```

### ViewModel Extensions
```kotlin
// Fetch the SharedViewModel instance, if the instance does not exist yet, returns a new one.
<reified T : Any> fetchSharedViewModel(vm: ViewModel): T

// Destroys the SharedViewModel instance.
destroySharedViewModel(vm: ViewModel)

// Returns the ViewModel tag.
getViewModelTag(vm: ViewModel)
```

## Available Functions

### Date Functions
```kotlin
// Adds minutes to a Date object.
addMinutesToDate(date: Date, minutes: Int): Date

// Formats a 24 hour to the 12 hour format.
// Example: val format formatTimeTo12HourFormat(hour = 23)
// -- output: format["dayHour"] = 11, format["dayTime"] = pm, 
formatTimeTo12HourFormat(hour: Int): HashMap<String, Any>

// Returns the list of abbreviated week days. Ex: "Sun", "Mon", "Tue"...
getDaysList(): List<String>

// Returns the list of months. Ex: "January", "February"...
getMonthFullNameList(): List<String>

// Returns the list of abbreviated months. Ex: "Jan", "Feb"...
getMonthList(): List<String>

// Returns the ordinal suffix to a date.
// Ex: getDayOrdinalSuffix(1) -- output = 1st, getDayOrdinalSuffix(2) -- output = 2nd 
getDayOrdinalSuffix(dayNum: Int): String

// Returns the days, hours and minutes difference between two gives dates.
getDifferenceBetweenDates(startDate: Date, endDate: Date): DifferenceBetweenDates
```

### Media Functions
```kotlin
// Returns the video URL thumbnail in the bitmap format.
retrieveVideoThumbnail(videoUrl: String?): Bitmap?

// Returns the video duration.
getVideoDuration(videoUrl: String): String

// Converts a image URI to ByteArray.
imageUriToByteArray(context: Context, uri: Uri?): ByteArray

// Converts a video URI to ByteArray.
videoUriToByteArray(context: Context, uri: Uri): ByteArray?
```

### View Functions
```kotlin
// Hides a list of views. (View.GONE)
hideViews(views: List<View>)

// Hides a view. (View.GONE)
hideView(view: View?)

// Shows a list of views. (View.VISIBLE)
showViews(views: List<View>)

// Shows a view. (View.VISIBLE)
showView(view: View?)

// Sets a OnClickAction to a list of views.
setOnClickActionToView(views: List<View>, action: (() -> Unit))

// Sets a OnClickAction to a view.
setOnClickActionToView(view: View, action: (() -> Unit))

// Sets a text to a list of views.
setTextTo(views: List<TextView>, text: String)
```

## Components

### CustomAlertDialog
```kotlin
// Displays a simple alert dialog.
CustomAlertDialog(context).showSimpleDialog(  
    title: String,  
    message: String,  
    positiveButtonCallback: (() -> Unit)? = null,  
    positiveButtonTitle: String? = null  
)

// Displays a confirm dialog.
CustomAlertDialog(context).showConfirmDialog(  
    title: String,  
    message: String,  
    positiveButtonTitle: String,  
    positiveButtonCallback: (() -> Unit)? = null,  
    negativeButtonTitle: String? = "Cancel",  
    negativeButtonCallback: (() -> Unit)? = null  
)

// Displays a options dialog.
CustomAlertDialog(context).showOptionsDialog(  
    options: List<String>,  
    title: String,  
    cancelCallback: (() -> Unit)? = null,  
    selectedCallback: ((selected: String) -> Unit)?  
)
```

## BindingAdapters

### ImageBinding 
```kotlin
// Loads an image from a URL into a ImageView.
app:loadImageViewFromUrl(url: String?)

// Loads an image from a URL into a ShapeableImageView.
app:loadShapeableImageViewFromUrl(url: String?)
```

### ViewBinding
Check the adapter file: [ViewBindingAdapter.kt](https://github.com/maikkkko1/android-arq-base/blob/master/android-base-arq-lib/src/main/java/com/maikkkko1/android_base_arq/arq/common/ViewBindingAdapter.kt)
