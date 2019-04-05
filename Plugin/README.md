# Testify - Android Plugin

Testify provides a selection of utility tasks that can be used for advanced use cases.

### deviceLocale

Displays the locale currently set on the device

```
~/: ./gradlew Sample:deviceLocale
> Task :Sample:deviceLocale
------------------------------------------------------------
Displays the locale currently set on the device
------------------------------------------------------------
  Locale               = en-US
```

### deviceTimeZone

Displays the time zone currently set on the device.

```
~/: ./gradlew Sample:deviceTimeZone
> Task :Sample:deviceTimeZone
------------------------------------------------------------
Displays the time zone currently set on the device
------------------------------------------------------------
  Time zone            = America/Toronto
```

### disableSoftKeyboard

Disables the soft keyboard on the device. The soft keyboard can interfere with your screenshots, so it's good to turn it off.

```
~/: ./gradlew Sample:disableSoftKeyboard
> Task :Sample:disableSoftKeyboard
------------------------------------------------------------
Disables the soft keyboard on the device
------------------------------------------------------------
  Success

```

### generateDiffImages

Create high-contrast images highlighting differences between the current screenshot and the baseline.

### hidePasswords

Hides passwords fully on the device.

```
~/: ./gradlew Sample:hidePasswords
> Task :Sample:hidePasswords
------------------------------------------------------------
Hides passwords fully on the device
------------------------------------------------------------
  Success
```

### removeDiffImages

Delete all generated diff images from your local machine.

### testifyDevices

Displays the connected devices.

```
~/: ./gradlew Sample:testifyDevices
> Task :Sample:testifyDevices
------------------------------------------------------------
Displays Testify devices
------------------------------------------------------------
  Connected devices    = 1
------------------------------------------------------------
  -Pdevice=0           = emulator-5554

  Add -Pdevice=N to any command to target a specific device
```

### testifyKey

Displays the Testify output key for the current device. Testify uses the key as the baseline for the given device.

```
~/: ./gradlew Sample:testifyKey
> Task :Sample:testifyKey
------------------------------------------------------------
Displays the Testify output key for the current device
------------------------------------------------------------
  Format: {api_version}-{width_in_pixels}x{height_in_pixels}@{dpi}-{language}
  key                  = 21-768x1280@320dp-en
```

### testifySettings

Testify infers several project properties. You can view these properties with the `testifySettings` command.

```
~/: ./gradlew Sample:testifySettings
> Task :Sample:testifySettings
------------------------------------------------------------
Displays the Testify gradle extension settings
------------------------------------------------------------
  applicationPackageId = com.shopify.testify.sample
  baselineSourceDir    = /Users/shopify/Sample/src/androidTest/assets/screenshots
  moduleName           = Sample
  outputFileNameFormat = null
  pullWaitTime         = 0
  testRunner           = androidx.test.runner.AndroidJUnitRunner
  useSdCard            = false
  testContextId        = com.shopify.testify.sample
  testPackageId        = com.shopify.testify.sample.test
```

### testifyVersion

Displays the Testify plugin version

```
~/: ./gradlew Sample:testifyVersion
> Task :Sample:testifyVersion
------------------------------------------------------------
Displays the Testify plugin version
------------------------------------------------------------
  Vendor               = Shopify
  Title                = Testify
  Version              = 1.0.0-alpha1
```

### verifyImageMagick

Verify that the ImageMagick tools required for diff images are installed. The `generateDiffImages` command can create high-contrast images to make subtle image differences easier to spot. To do this, Testify uses the [ImageMagick](https://www.imagemagick.org/) command.

```
~/: ./gradlew Sample:verifyImageMagick
> Task :Sample:verifyImageMagick
------------------------------------------------------------
Verify that the ImageMagick tools required for diff images are installed
------------------------------------------------------------
  Compare               = true
```