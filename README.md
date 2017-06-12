# Hollow-api-generator-plugin

This plugin provides goal for generating hollow consumer api, that is 
described [here](http://hollow.how/getting-started/#consumer-api-generation)

## Usage
The hollow api generator works on compiled classes. The easiest way to work with this
is using multiple maven modules like this:

```xml
<modules>
    <module>my-hollow-domain-objects</module>
    <module>my-hollow-consumer-api</module>
    <module>my-hollow-publisher</module>
</modules>
```



In a project that has a dependency on your domain classes, you can then set up the plugin like this:
```xml
<dependencies>
   ...
   <dependency>
        <groupId>my.groupId</groupId>
        <artifactId>my.artifactId</artifactId>
        <version>my.version</version>
        <!-- there's no need for the domain definition files
             to be included transitively by other modules, so you can
             mark this dependency as "optional" -->
        <optional>true</optional>
    <dependency>
    ...
```

and in your plugins section:
```xml


<plugin>
    <groupId>com.github.igorperikov</groupId>
    <artifactId>hollow-maven-plugin</artifactId>
    <version>0.2.1</version>

    <executions>
        <execution>
            <id>generate-hollow-stufff</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <packagesToScan>
                    <param>org.example.package1</param>
                    <param>org.example.package2</param>
                </packagesToScan>
                <apiClassName>MyApiClassName</apiClassName>
                <apiPackageName>org.example.package3.generated.api</apiPackageName>
            </configuration>
        </execution>
    </executions>

</plugin>
```

where:

- `packagesToScan` - packages with your data classes, **note that scan is recursive**
- `apiClassName` - class name for your [api implementation](https://github.com/Netflix/hollow/blob/master/hollow/src/main/java/com/netflix/hollow/api/custom/HollowAPI.java) 
- `apiPackageName` - target package in your project for api-related sources

when running a normal build (e.g. `mvn clean install`) the generator plugin will scan the compile classpath of its dependencies,
generate sources under the `target/generated-sources/hollow` directory, and then add those to the compilation phase so that
the end result will be a jar that contains the generated api that you can then use to consume the hollow data.

