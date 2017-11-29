# Automation Tests in Java
This is a Java based framework that was created to accomplish items that Codeception is not able to. 

## Installing Automation Framework
1. Install Java 1.8 JDK - http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Update `~/.bash_profile` with the attribute JAVA_HOME pointing to the correct Java location.<br>   
    ```
    export PATH="$PATH:/usr/local/:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$JAVA_HOME/bin"
    export JAVA_HOME=$(/usr/libexec/java_home)
    ``` 
    a. edit the file `open -e ~/.bash_profile` - Add the path variable and then save the file <br><br>
    **If you do not have a .bash_profile file then run the following command**:<br> create a ~/.bash_profile file: `touch ~/.bash_profile` <br><br>
    b. source the file `source ~/.bash_profile`
3. Install Maven - `brew install maven`<br><br>
   **If brew command is not found run the following command**:<br> `/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"` <br><br>
4. Update `~/.bash_profile` with the following attributes:
   ```
      export M2_HOME="/usr/local/bin/mvn"
      export M2=$M2_HOME/bin
      export PATH=$M2:$PATH
   ```
5. Git Clone this repository:
    `git clone git@github.com:CreditCardsCom/automation-link-java.git`
6. Go to the folder of the repository `cd automation-link-java/BaseDriver`
7. Run Maven install without tests `mvn install -DskipTests`
8. Go to the folder of the repository `cd automation-link-java`
9. Run Maven install without tests `mvn install -DskipTests`
10. Go to the properties folder `cd resources/properties`
11. Copy any files that have the extension `.example` into the directory to create a properties file needed to run tests. `cp database.properties.example database.properties`
12. Find someone that has the real credentials.
13. Go back to the `automation-link-java` folder before running the tests.<br><br>

## Installing Appium for Mobile Testing
*Note: Make sure that you have Java 1.8 installed. Follow number 1 from installing the framework.*
1. Install Xcode - https://itunes.apple.com/us/app/xcode/id497799835?mt=12
2. Install xcode-tools from the terminal `xcode-select --install`
3. Install appium-doctor `npm install -g appium-doctor`
4. Install appium `npm install -g appium`
5. Install carthage `brew install carthage`
6. Install node `brew install node` or make sure it is version 3.7.3 or greater `node -v`
7. Download all the Android SDK necessary tools, that includes mandatory platform-tools and build-tools.<br>
    a. https://dl.google.com/android/repository/sdk-tools-darwin-3859397.zip <br>
    b. Extract the file and move it to another folder something like `/User/{username}/` *REMEMBER LOCATION*
8. Update your `~/.bash_profile`<br>
    a. `export ANDROID_HOME=/Users/your-user-name/android-sdk-macosx/sdk` <br>
    b. Add the Android sdk paths to your existing PATH=$PATH variable. e.g. `:/Users/your-user-name/android-sdk-macosx/sdk/tools:/Users/your-user-name/android-sdk-macosx/sdk/platform-tools:/Users/your-user-name/android-sdk-macosx/sdk/build-tools:` <br>
    The first line of PATH variable should look like this:<br>
    `    export PATH="$PATH:/usr/local/:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools:$JAVA_HOME/bin"`
9.  Run command `emulator -help` - there should be a long list of available commands
10. Run `android sdk`
11. Select: <br>
    a. Android SDK Tools - Latest Version <br>
    b. Android SDK Platform-tools - Latest Version <br>
    c. Android SDK Build-tools - Latest version <br>
12. Run `adb` see `Android Debug Bridge` with version number
13. Please open a finder window and navigate to the android-sdk-macosx folder. Then double click the intelHAXM_*.dmg installer and follow the install instructions.
14. In the terminal run `appium-doctor`<br>

Output should look like this:<br>
```
info AppiumDoctor Appium Doctor v.1.4.3
info AppiumDoctor ### Diagnostic starting ###
info AppiumDoctor  ✔ The Node.js binary was found at: /usr/local/bin/node
info AppiumDoctor  ✔ Node version is 8.2.1
info AppiumDoctor  ✔ Xcode is installed at: /Applications/Xcode.app/Contents/Developer
info AppiumDoctor  ✔ Xcode Command Line Tools are installed.
info AppiumDoctor  ✔ DevToolsSecurity is enabled.
info AppiumDoctor  ✔ The Authorization DB is set up properly.
info AppiumDoctor  ✔ Carthage was found at: /usr/local/bin/carthage
info AppiumDoctor  ✔ HOME is set to: /Users/..
info AppiumDoctor  ✔ ANDROID_HOME is set to: /Users/../android-sdk-macosx
info AppiumDoctor  ✔ JAVA_HOME is set to: /Library/Java/JavaVirtualMachines/jdk1.8.0_141.jdk/Contents/Home
info AppiumDoctor  ✔ adb exists at: /Users/../android-sdk-macosx/platform-tools/adb
info AppiumDoctor  ✔ android exists at: /Users/../android-sdk-macosx/tools/android
info AppiumDoctor  ✔ emulator exists at: /Users/../android-sdk-macosx/tools/emulator
info AppiumDoctor  ✔ Bin directory of $JAVA_HOME is set
info AppiumDoctor ### Diagnostic completed, no fix needed. ###
info AppiumDoctor
info AppiumDoctor Everything looks good, bye!
info AppiumDoctor
```
#### iOS Application Installation
https://creditcards.atlassian.net/wiki/spaces/MOBAPPS/pages/59777013/CupMaker

#### Create / Start an Android Emulator
**Note: Make sure that the emulator is running before the test runs.*
1. Download https://developer.android.com/studio/index.html Android Studios and Install
2. Open Android Studios and create a sample application (This is just a place holder) <br>
    a. Click "Start a new Android Studio Project"<br>
    b. Follow the prompts until the project is created.
3. Go to "Tools" menu and select "Android", then select "AVD Manager"
4. Create an emulator based on the tests specs

## Running Tests
### Run from command line
`mvn test -DtestSuite={folder/xmlFile}`

Check results at `target/surefire-reports/emailable-report.html`

## Run from an IDE
- Need to have a test-output directory present
- Check `test-output/html/index.html` for current run results

## Writing Tests
### TestNG file parameter requirements:
- The file needs to belong in `src/test/resources/testNG` folder.

<table>
  <tr>
    <th>Name</th>
    <th>Options</th>
    <th>Required</th>
  </tr>
<tr><td>proxyOn</td><td> true or false (Lowercase)</td><td> required</td></tr>
<tr><td>capabilities</td><td>  Example: <pre>javaScriptEnabled=true,unexpectedAlertBehaviour=accept</pre> </td><td> Yes, but <i>value</i> can be left empty</td></tr>
<tr><td>driver</td><td>  <ul><li>webDriver</li><li> cbt</li><li>appium</li></ul></td><td>
<ul><li> <b>webDriver</b> Parameters required:
    <ul><li> browser : chrome, ieEdge, firefox, ie, or safari</li></ul>
    <li><b>cbt</b> Parameters required:<ul> 
    <li> username: qa1, qa2, qa3, qa4</li>
    <li> authkey</li></ul></li>
<li><b>appium</b> Parameters required:<ul>
    <li> mobilePlatform: android or iOS</li> 
    <li> hubURL</li><li>deviceName</li>
    <li> testingApp: web or native <ul>
        <li> web requires browser parameter</li> 
        <li> native requires appPath parameter</li></ul></li></li></ul>
<li> The rest of the requirments need to be in <i>capabilities</i> parameter.</li></ul> </td></tr></table>

#### Example:
**Local WebDriver**
```xml
    <parameter name="proxyOn" value="true"/>
    <parameter name="capabilities" value=""/>
    <parameter name="driver" value="webDriver"/>
    <parameter name="browser" value="chrome"/>
```
**CBT Parameters Example**
```xml
  <parameter name="proxyOn" value="false"/>
     <parameter name="driver" value="cbt"/>
     <parameter name="capabilities"
                      value="name=SampleApplication, browser_api_name=Edge15, os_api_name=Win10, screen_resolution=1366x768, record_video=true, build=1"/>
     <parameter name="username" value="qa3"/>
     <parameter name="authkey" value=""/>
```
**iOS Parameters Example**
```xml
        <parameter name="proxyOn" value="false"/>
        <parameter name="capabilities" value="platformVersion=10.3"/>
        <parameter name="driver" value="appium"/>
        <parameter name="mobilePlatform" value="iOS"/>
        <parameter name="hubURL" value="http://127.0.0.1:4723/wd/hub"/>
        <parameter name="testingApp" value="web"/>
        <parameter name="browser" value="safari"/>
        <parameter name="deviceName" value="iPhone 7"/>
 ```
        
**Android Parameters Example**
```xml
    <parameter name="proxyOn" value="false"/>
    <parameter name="capabilities" value=""/>
    <parameter name="driver" value="appium"/>
    <parameter name="mobilePlatform" value="android"/>
    <parameter name="hubURL" value="http://127.0.0.1:4723/wd/hub"/>
    <parameter name="testingApp" value="web"/>
    <parameter name="browser" value="chrome"/>
    <parameter name="deviceName" value="Nugget"/>
```

### Start writing a test / Best Practices
- The test class must extend either `BaseWebTest`, or `BaseMobileTest`. 
- The driver is initiated in the beginning of the suite and is closed at the end of the suite, do not open or close the items in @BeforeClass, or @AfterClass.


##### Current Errors in Framework
Mobile side:
1. Error activated when PageFactory is used for MobileElement: <br>`java.lang.NoSuchMethodError: org.openqa.selenium.support.ui.FluentWait.until(Lcom/google/common/base/Function;)Ljava/lang/Object;`
 
*Licence for extent reports: http://extentreports.com/about/license/*