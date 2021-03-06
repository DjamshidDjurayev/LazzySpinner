# LazzySpinner
Spinner based on a TextView with HintText and HintTextColor
## Usage
Firstly, add the following code to your xml file
``` xml
<com.dzhuraev.dddlazzy_spinner.utils.LazzySpinner
    android:id="@+id/spinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="your hint text"/>
```
Then find it by id and do the following
``` java
lazzySpinner = (LazySpinner) findViewById(R.id.spinner);
lazzySpinner.withAdapter(new ArrayAdapter<>(this, R.layout.item, string_array));
lazzySpinner.withActivity(this);
```
#### Set dialog ***WIDTH*** and ***HEIGHT***
``` javaScript
lazzySpinner.setDialogWidth(LazzyDimens.FULL_SIZE); // for Full size
lazzySpinner.setDialogHeight(LazzyDimens.HALF_SIZE); // for Half size
```
## How to add it ?
#### Gradle:
``` java
dependencies {
  compile 'com.github.djurayev:lazzyspinner:0.2.4'
}
```
#### Maven:
``` java
<dependency>
  <groupId>com.github.djurayev</groupId>
  <artifactId>lazzyspinner</artifactId>
  <version>0.2.4</version>
  <type>pom</type>
</dependency>
```
## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
