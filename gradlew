#!/usr/bin/env sh
set -e
APP_HOME=$(cd "$(dirname "$0")" && pwd)
# в CI есть системный gradle, его и используем
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
# fallback если вдруг нет системного
exec java -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
