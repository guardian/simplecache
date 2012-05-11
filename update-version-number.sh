#!/bin/bash

if ([ -z "$1" ]); then
  echo "usage: $0 <version>"
  exit
fi

find . -name 'build.sbt' | xargs -- sed -i "s/version := \".*\"/version := \"$1\"/g"

