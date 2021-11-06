#!/bin/bash
REPOSITORY=/home/ubuntu/deploy
PROJECT_NAME=bobsPlus
echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/
echo "> 8080포트 kill"
sudo fuser -k 8080/tcp
sleep 3
echo "> 8443포트 kill"
sudo fuser -k 443/tcp
sleep 3
echo "> 새 애플리케이션 배포"
cd $REPOSITORY/zip/build/libs
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | head -n 1)
echo "> JAR Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME 
echo "> $JAR_NAME 실행"
nohup sudo java -jar  -Djasypt.encryptor.password=$ENCRYPT_KEY\
 $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
