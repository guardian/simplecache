#!/bin/bash

java -Xmx2048M -XX:MaxPermSize=512m \
    -jar `dirname $0`/dev/sbt-launch-0.11.0.jar "$@"
