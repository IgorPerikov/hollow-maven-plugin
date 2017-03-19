# Hollow-api-generator-plugin

This plugin provides goal for generating hollow consumer api, that is 
described [here](http://hollow.how/getting-started/#consumer-api-generation)

## Usage
In order to use, add plugin to your `pom.xml` file and configure it
```
<plugin>
    <groupId>com.github.igorperikov</groupId>
    <artifactId>hollow-maven-plugin</artifactId>
    <version>0.1.0</version>
    <configuration>
        <packagesToScan>
            <param>org.example.package1</param>
            <param>org.example.package2</param>
        </packagesToScan>
        <apiClassName>MyApiClassName</apiClassName>
        <apiPackageName>org.example.package3.generated.api</apiPackageName>
    </configuration>
</plugin>
```

where:

- `packagesToScan` - packages with your data classes, **note that scan is recursive**
- `apiClassName` - class name for your [api implementation](https://github.com/Netflix/hollow/blob/master/hollow/src/main/java/com/netflix/hollow/api/custom/HollowAPI.java) 
- `apiPackageName` - target package in your project for api-related sources

launch task:

`mvn compile hollow:generate`

N.B. it is important to launch `compile` before `hollow:generate`
because plugin needs .class files to generate consumer api
