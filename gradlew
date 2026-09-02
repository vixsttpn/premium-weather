#!/usr/bin/env sh
#
# Custom wrapper shim that tries jar then falls back to system gradle (for CI without jar binary in offline creation)
#
set -e
APP_HOME=$(cd "$(dirname "$0")" && pwd)
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ -f "$WRAPPER_JAR" ] && [ -s "$WRAPPER_JAR" ]; then
  # Try jar, but check if valid zip
  if unzip -t "$WRAPPER_JAR" >/dev/null 2>&1; then
    exec java -jar "$WRAPPER_JAR" "$@"
  fi
fi
# Fallback to system gradle (installed via setup-gradle action)
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle not found and wrapper jar unavailable. Installing via sdkmanager is required."
  exit 1
fi