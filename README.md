# springboot-elasticsearch

Sample project to demonstrate the  use spring boot along with elastic search .

Spring Initialzr was used to bootstrap this project http://start.spring.io/ . 

Since embedded elastic search is used , the following entry had to be manually added to the pom xml

```xml
 <dependency>
        <groupId>net.java.dev.jna</groupId>
        <artifactId>jna</artifactId>
        <scope>runtime</scope>
 </dependency>
 ```
 
 The entry point to the application is App.java where some data is loaded into elastic search and several queries
 have been executed.
 
 

