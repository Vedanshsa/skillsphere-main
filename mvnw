#!/bin/sh
# Maven Wrapper script for Unix/Linux/Mac

set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
    if command -v curl > /dev/null; then
        curl -fsSL -o "$MAVEN_WRAPPER_JAR" "$MAVEN_WRAPPER_URL"
    elif command -v wget > /dev/null; then
        wget -q -O "$MAVEN_WRAPPER_JAR" "$MAVEN_WRAPPER_URL"
    else
        echo "Error: curl or wget required"
        exit 1
    fi
fi

exec java -jar "$MAVEN_WRAPPER_JAR" "$@"