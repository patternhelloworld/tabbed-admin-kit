rem -DskipTests 를 아래 실행 문구에 추가할 경우, 테스트 코드드 실행이 제외된다. 테스트 코드에 실패하면 jar 파일이 생성되지 않는다.
rem ./mvnw dependency:purge-local-repository 를 하면 라이브러리 모두 다시 받는다.
rem ./mvnw compile 하면 query dsl 만 컴파일 한다.
rem ./mvnw  install:install-file -Dfile="C:/Users/Andrew Kang/.m2/repository/com/autofocus/pms/common/pms-common/0.0.1-SNAPSHOT/pms-common-0.0.1-SNAPSHOT.jar" -DgroupId=com.autofocus.pms.common -DartifactId=pms-common -Dversion=0.0.1 -Dpackaging=jar -DgeneratePom=true
./mvnw clean install

