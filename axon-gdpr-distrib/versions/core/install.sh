#!/bin/bash
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=axoniq-gdpr-core-1.2.jar -DpomFile=pom.xml -Djavadoc=axoniq-gdpr-core-1.2-javadoc.jar
