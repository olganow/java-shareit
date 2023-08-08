#!/bin/bash
cd gateway
rm -fr target
mvn clean package

cd ..

cd server
rm -fr target
mvn clean package



