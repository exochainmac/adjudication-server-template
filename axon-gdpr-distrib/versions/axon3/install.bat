@echo off
@mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=${project.build.finalName}.jar -DpomFile=pom.xml -Djavadoc=${project.build.finalName}-javadoc.jar
