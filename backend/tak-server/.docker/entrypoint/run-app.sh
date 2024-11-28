#!/bin/bash

# Graceful Shutdown
#trap exitHandler EXIT
#exitHandler() {
 #   echo "[INSIDE APP CONTAINER][WARN] Server will be shutdown soon." 1>&2
  #  kill -TERM $(jps | grep jar | awk '{print $1}')
#}

if [ -z "$1" ]; then
    echo "[INSIDE APP CONTAINER][ERROR] No project root path parameter found for the 'run-app.sh'"
    exit 1
fi
if [ -z "$2" ]; then
    echo "[INSIDE APP CONTAINER][ERROR] No file root path parameter found for the 'run-app.sh'"
    exit 1
fi
if [ -z "$3" ]; then
    echo "[INSIDE APP CONTAINER][ERROR] No Xms parameter found for the 'run-app.sh'"
    exit 1
fi
if [ -z "$4" ]; then
    echo "[INSIDE APP CONTAINER][ERROR] No Xmx parameter found for the 'run-app.sh'"
    exit 1
fi

echo "[INSIDE APP CONTAINER][NOTICE] Run : java -Xlog:gc* -Xms${3}m -Xmx${4}m -Xlog:gc*:${2}/logs/auth-gc.log -Dspring.config.location=file:${1}/hq-server/src/main/resources/application.properties -Dlogging.config=file:${1}/hq-server/src/main/resources/logback-spring.xml -jar /app.jar > ${2}/logs/auth-start.log 2>&1 &"
# -XX:+UseZGC -XX:+ZUncommit : ZGC (빠른 어플리케이션 응답속도를 요구하는 상황에서 가장 알맞는 GC입니다. 다만, 메모리가 많이 여유로운 상황에서 이용하는 것을 권장. 기존 Heap 사이즈의 3배. https://velog.io/@gehwan96/Garbage-CollectionGC-2)
java -Xlog:gc* -Xms${3}m -Xmx${4}m -Xlog:gc*:${2}/logs/auth-gc.log -Dspring.config.location=file:${1}/hq-server/src/main/resources/application.properties -Dlogging.config=file:${1}/hq-server/src/main/resources/logback-spring.xml -jar /app.jar > ${2}/logs/auth-start.log 2>&1 &
