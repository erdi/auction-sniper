# Auction Sniper

This is an implementation of the example application from the book ["Growing Object-Oriented Software Guided by Tests"](http://www.growing-object-oriented-software.com/)
by Steve Freeman and Nat Price.

* application code is written in Java 8 with lambda expressions
* tests are written Groovy using [Spock](http://spockframework.org)
* code is built using [Gradle](http://gradle.org/)
* thanks to using [Apache Vysper](https://mina.apache.org/vysper-project/index.html) as the XMPP broker the build is self-contained.

## Running the tests

    ./gradlew test

The above command will download all dependencies and run all tests.