# Hollow-api-generator-plugin

This plugin provides goal for generating hollow consumer api, that is 
described [here](http://hollow.how/getting-started/#consumer-api-generation)

## Versions mapping
It's **not recommended** to use plugin with version that is not listed below. This rule is temporary, until the stable release comes
| Plugin version | Hollow version | 
| --- | --- |
| 0.3.0 | 2.6.3 |

## Usage
You can use plugin in two different configurations: 

* In case you want to have hollow consumer api classes directly in your project and VCS - just stick to the single-module configuration. 
* In case you don't want to have all this classes in your VCS - stick to the multi-module configuration. 

## Single module
TBD
[complete example](https://github.com/IgorPerikov/hollow-maven-plugin-examples/tree/master/single-module-example)

## Multi module
You need to setup 2 modules, one for your data model classes and the other one for your consumer api.
TBD
[complete example](https://github.com/IgorPerikov/hollow-maven-plugin-examples/tree/master/multi-module-example)

## Plugin configuration properties
- `packagesToScan` - packages with your data classes, **note that scan is recursive**
- `apiClassName` - class name for your [api implementation](https://github.com/Netflix/hollow/blob/master/hollow/src/main/java/com/netflix/hollow/api/custom/HollowAPI.java) 
- `apiPackageName` - target package in your project for api-related sources

when running a normal build (e.g. `mvn clean install`) the generator plugin will scan the compile classpath of its dependencies,
generate sources under the `target/generated-sources/hollow` directory, and then add those to the compilation phase so that
the end result will be a jar that contains the generated api that you can then use to consume the hollow data.
