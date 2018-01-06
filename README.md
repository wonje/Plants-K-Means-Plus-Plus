# Collecting plants by following their place of origin using K-means++ Clustering
This is machine learning research class final project.
It uses K-means++ Clustering to collect plants by following their place of origin. The origin is substituted by state information.

This project is written in Java with Intellij IDE.

## 1. Technical Documentation for K-means++ Clustering
### 1.1 Code Structure
#### - Class
Plant.java: It implements all of functions for K-means++ clustering.
#### - Resource
plants.data: plant data file to be used for K-means++ clustering.
#### - Structure
#### 1) Parse and construct dataset
Read “plants.data” using BufferedReader. After that, Plant’s constructor parsed each of reading line and constructs data.
#### 2) Randomly choose initial centroids
Choosing initial centroid using K-means++ algorithm. It is implemented it using uniform distribution. At every epoch, the value of “totalProbability” is calculated as the sum of distances from the previous centroid. By using this, the K-means++ algorithm can be implemented.
#### 3) Start clustering
At every epoch, set the nearest centroid to each dataset by calculating distances to each centroid. If there is no change compared to the previous value of centroid, its state will converge. Otherwise, each value of centroid will be set with new mean value using allocated dataset. Then, it will calculate distances to each centroid to find the nearest centroid.
#### 4) Print results
Print values of initial centroids, values of final centroids, running time until finish the process, and the number of members in each cluster.

## How to run this code

To run this code, following this:

- Run following instruction in your shell
```
java -jar Plant_K_Means_Plus_Plus.jar
```

- Then, you will see the following questions:

```
Choose the number of the cluster :

>> You should type an integer number, such as '5'.
```

- Then, this code will work with the number of clusters you selected.

## 2. K-means++ Clustering Evaluation on Real World
### 2.1 Evaluation Data Set
State | Capital | Coordinate | Decimal
------|---------|------------|--------
AK|Juneau|58.3014485°N 134.4216125°W|58.301449, -134.421613
AR|Little Rock|34°44′10″N 92°19′52″W|34.736111, -92.331111
AZ|Phoenix|33°27′N 112°04′W|33.45, -112.066667
CA|Sacramento|38°33′20″N 121°28′08″W|38.555556, -121.468889
CO|Denver|39°45′43″N 104°52′52″W|39.76185, -104.881105
CT|Hartford|41°45′45″N 72°40′27″W|41.7625, -72.674167
DE|Dover|39°09′29″N 75°31′28″W|39.158056, -75.524444
DC|Washington|38°54′17″N 77°00′59″W|38.904722, -77.016389
FL|Tallahassee|30°27′18″N 84°15′12″W|30.455, -84.253333
GA|Atlanta|33°45′18″N 84°23′24″W|33.755, -84.39
HI|Honolulu|21°18′N 157°49′W|21.3, -157.816667
ID|Boise|43°37′N 116°12′W|43.616667, -116.2
IL|Springfield|39°41′54″N 89°37′11″W|39.698333, -89.619722
IN|Indianapolis|39°46′N 86°9′W|39.766667, -86.15
IA|Des Moines|41°35′27″N 93°37′15″W|41.590833, -93.620833
KS|Topeka|39°03′21″N 95°41′22″W|39.055833, -95.689444
KY|Frankfort|38°12′N 84°52′W|38.2, -84.866667
LA|Baton Rouge|30°26′55″N 91°07′33″W|30.448611, -91.125833
ME|Augusta|44°18′38″N 69°46′48″W|44.310556, -69.78
MD|Annapolis|38°58′22.6″N 76°30′4.17″W|38.972944, -76.501158
MA|Boston|42°21′29″N 71°03′49″W|42.358056, -71.063611
MI|Lansing|42°44′1″N 84°32′48″W|42.733611, -84.546667
MN|Saint Paul|44°56′39″N 93°5′37″W|44.944167, -93.093611
MS|Jackson|32°17′56″N 90°11′05″W|32.298889, -90.184722
MO|Jefferson City|38°34′36″N 92°10′25″W|38.576667, -92.173611
MT|Helena|46°35′44.9″N 112°1′37.31″W|46.595806, -112.027031
NE|Lincoln|40°48′38″N 96°40′49″W|40.810556, -96.680278
NV|Carson City|39°9′39″N 119°45′14″W|39.160833, -119.753889
NH|Concord|43°12′24″N 71°32′17″W|43.206667, -71.538056
NJ|Trenton|40.223748°N 74.764001°W|40.223748, -74.764001
NM|Santa Fe|35°40′2″N 105°57′52″W|35.667222, -105.964444
NY|Albany|42°40′51″N 073°45′17″W|42.680833, -73.754722
NC|Raleigh|35°46′N 78°38′W|35.766667, -78.633333
ND|Bismarck|46°48′48″N 100°46′44″W|46.813333, -100.778889
OH|Columbus|39°59′N 82°59′W|39.983333, -82.983333
AB|Edmonton|53°32′N 113°30′W|53.533333, -113.5
AL|Montgomery|32°21′42″N 86°16′45″W|32.361667, -86.279167
BC|Victoria|48°25′43″N 123°21′56″W|48.428611, -123.365556
MB|Winnipeg|49°53′58″N 97°08′21″W|49.899444, -97.139167
NB|Nebraska|40°48′38″N 96°40′49″W|40.810556, -96.680278
LB|Long Beach|33°46′6″N 118°11′44″W|33.768333, -118.195556
NT|Darwin|12°27′0″S 130°50′0″E|-12.45, 130.833333
NS|Halifax|44°38′52″N 63°34′17″W|44.647778, -63.571389
NU|Iqaluit|63°44′55″N 068°31′11″W|63.748611, -68.519722
ON|Toronto|43°42′N 79°24′W|43.7, -79.4
QC|Quebec City|46°49′N 71°13′W|46.816667, -71.216667
SK|Regina|50°27′17″N 104°36′24″W|50.454722, -104.606667
YT|Whitehorse|60°43′N 135°03′W|60.716667, -135.05
DENGL|Nuuk|64°10′30″N 51°44′20″W|64.175, -51.738889
FRASPM|Saint-Pierre|46°46′40″N 56°10′40″W|46.7778, -56.1778
PE|Charlottetown|46°14′20″N 63°07′45″W|46.238889, -63.129167
GL|Unknown|Unknown|Unknown
NF|Unknown|Unknown|Unknown

The above data set is a version of the real world as described the way in the “plant.data” file. Each coordinate shows the coordinate of capitals for each state. These data come from Wiki.
