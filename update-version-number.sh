#!/bin/bash

find . -name 'build.sbt' | xargs -- sed -i "s/version := \".*\"/version := \"$1\"/g"

