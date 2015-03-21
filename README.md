Pre-requisites for running project:

*  Have an installation of Maven.  For more information, see http://maven.apache.org/
*  Have an installation of JDK version 6 or later.  For more information, see http://www.oracle.com/technetwork/java/javaee/overview/index.html

A couple notes about this source for this chapter that make it different than other chapters:

1. It's designed to be used iteratively in a follow-along fashion with its chapter. We leverage source control to version and tag each different iteration of the topology as we address various tuning concerns. If you have arrived at this code without reading the chapter, know that each of tagged version corresponds to a certain point in the chapter.

2. Unlike other chapters that make sense to run in local mode, there isn't much value in running this code in local mode, as its reason to exist is to guide you through using the Storm UI to tune your topology. That said, if you want to run this topology in local mode, it's possible. We've deviated from other chapters by providing a local cluster running profile for running the topology.

To run this topology in local mode:

*  Run the maven local cluster profile.  Do this by running the following on the command-line:

```
mvn clean compile -P local-cluster
```

To Run this topology in remote mode:

*  Compile the project source, producing a jar file.  Do this by running the following on the command-line:

```
mvn clean package
```

*  Deploy the resulting jar to your storm cluster (see Chapter 5 for how to do this)

Your jar file should have a name that corresponds to the following pattern:

```
target/flash-sale-recommender-VERSION-jar-with-dependencies.jar 
```

where

``
VERSION
```

will change based on the version of the topology you are deploying.

