#!/bin/sh
TARGET_DIR=$(cd "$(dirname "$0")" && pwd)/../target
java -jar -Dspring.main.web-application-type=NONE $TARGET_DIR/gym-roser-parser.jar "$@"