#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mkdir -p build/classes
javac -encoding UTF-8 --release 17 -d build/classes src/delivery/*.java test/delivery/DeliveryTests.java
java -Dfile.encoding=UTF-8 -cp build/classes delivery.DeliveryTests
