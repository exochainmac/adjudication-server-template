#!/usr/bin/env bash

rm -rf logs && java -Dspring.profiles.active=dev,mariadb4j,nosecurity,internal -jar boot-server/target/bc-api-server-0.0.1-SNAPSHOT.jar
