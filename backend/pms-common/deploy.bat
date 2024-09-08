@echo off
REM 현재 디렉토리의 절대 경로를 얻음
setlocal
cd %~dp0
set "local_maven_repo=%CD%/merz-belotero-server/lib"

REM Maven Wrapper 명령을 실행
mvnw.cmd -DaltDeploymentRepository=in-project::file:///%local_maven_repo% clean deploy
endlocal
