# Hollow-api-generator-plugin

This plugin provides goal for generating hollow consumer api, that is 
described [here](http://hollow.how/getting-started/#consumer-api-generation)

## Versions mapping
It's **not recommended** to use plugin with version that is not listed below. This rule is temporary, until the stable release comes

| Plugin version | Hollow version | Java version | Maven |
| --- | --- | --- | --- |
| 0.4.0 | 2.6.3 | 1.7 | 3.2.5 |
| 0.5.0 | 2.6.3 | 1.7 | 3.2.5 |
| 1.0.0 | 4.0.3 | 1.8 | 3.3.9 |

## Usage
You can use plugin in two different configurations: 

* In case you want to have hollow consumer api classes directly in your project and VCS - just stick to the single-module configuration. 
* In case you don't want to have all this classes in your VCS - stick to the multi-module configuration. 

## Single module
All you need is to add this blocks into your `pom.xml`:
```
    <dependencies>
        <dependency>
            <groupId>com.netflix.hollow</groupId>
            <artifactId>hollow</artifactId>
            <version>2.6.3</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.github.igorperikov</groupId>
                <artifactId>hollow-maven-plugin</artifactId>
                <version>0.5.0</version>
                <configuration>
                    <packagesToScan>
                        <param>your.package.datamodel</param>
                    </packagesToScan>
                    <apiClassName>EntityApi</apiClassName>
                    <apiPackageName>your.package.api</apiPackageName>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

Execute `mvn compile hollow:generate-as-project-sources` to create consumer api classes under `src/main/java/{apiPackageName}`

[complete example](https://github.com/IgorPerikov/hollow-maven-plugin-examples/tree/master/single-module-example)

## Multi module
You need to setup 2 modules, one for your data model classes and the other one for your consumer api. Module with data classes 
is a plain module with your classes and the 2nd module purpose is to store consumer api classes. They will be created as a 
generated sources of your final jar file, to use them, add this module as a dependency in the consumer application(which is probably the 3rd module) 

Add this blocks to your consumer-api artifact
```
<dependencies>
        <dependency>
            <groupId>your.package</groupId>
            <artifactId>your-data-model-artifact</artifactId>
            <version>${version}</version>
        </dependency>
        <dependency>
            <groupId>com.netflix.hollow</groupId>
            <artifactId>hollow</artifactId>
            <version>2.6.3</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.github.igorperikov</groupId>
                <artifactId>hollow-maven-plugin</artifactId>
                <version>0.5.0</version>

                <executions>
                    <execution>
                        <id>generate-hollow-consumer-api</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>generate-as-target-sources</goal>
                        </goals>
                        <configuration>
                            <packagesToScan>
                                <param>your.package.datamodel</param>
                            </packagesToScan>
                            <apiClassName>EntityApi</apiClassName>
                            <apiPackageName>your.package.api</apiPackageName>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
`mvn clean install` and you get a jar which contains consumer api classes, simply add this jar as a dependency to your application

[complete example](https://github.com/IgorPerikov/hollow-maven-plugin-examples/tree/master/multi-module-example)

## Plugin configuration properties
- `packagesToScan` - packages with your data classes, **note that scan is recursive**
- `apiClassName` - class name for your [api implementation](https://github.com/Netflix/hollow/blob/master/hollow/src/main/java/com/netflix/hollow/api/custom/HollowAPI.java) 
- `apiPackageName` - target package in your project for api-related sources

when running a normal build (e.g. `mvn clean install`) the generator plugin will scan the compile classpath of its dependencies,
generate sources under the `target/generated-sources/hollow` directory, and then add those to the compilation phase so that
the end result will be a jar that contains the generated api that you can then use to consume the hollow data.
