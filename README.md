# Hollow-api-generator-plugin

This plugin provides goal for generating hollow consumer api, that is 
described [here](http://hollow.how/getting-started/#consumer-api-generation)

## Versions mapping
It's **not recommended** to use plugin with version that is not listed below. This rule is temporary, until the stable release comes

| Plugin | Hollow | Java | Maven |
| --- | --- | --- | --- |
| 0.4.0 | 2.6.3 | 1.7 | 3.2.5 |
| 0.5.0 | 2.6.3 | 1.7 | 3.2.5 |
| 1.0.0 | 4.0.3 | 1.8 | 3.3.9 |

## Usage
You can use plugin in two different configurations: 

* In case you want to have hollow consumer api classes directly in your project and VCS - just stick to the single-module configuration. 
* In case you don't want to have all this classes in your VCS - stick to the multi-module configuration.

[Usage examples](https://github.com/IgorPerikov/hollow-maven-plugin-examples)

## Mandatory configuration properties:
- `packagesToScan` - packages with your data classes, **note that scan is recursive**
- `apiClassName` - class name for your [api implementation](https://github.com/Netflix/hollow/blob/master/hollow/src/main/java/com/netflix/hollow/api/custom/HollowAPI.java)
- `apiPackageName` - target package in your project for api-related sources

## Optional properties:
- `parameterizeAllClassNames`
- `useAggressiveSubstitutions`
- `useBooleanFieldErgonomics`
- `reservePrimaryKeyIndexForTypeWithPrimaryKey`
- `useHollowPrimitiveTypes`
- `useVerboseToString`
- `useErgonomicShortcuts`
- `usePackageGrouping`
- `restrictApiToFieldType`

default values for those params can be found [here](/src/main/java/com/github/igorperikov/hollow/mojo/AbstractHollowMojo.java)
