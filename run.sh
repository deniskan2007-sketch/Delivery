#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
sh build.sh
java -Dfile.encoding=UTF-8 -cp build/classes delivery.Main
