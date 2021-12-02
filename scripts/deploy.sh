sleep 3
echo "> 새 애플리케이션 배포"
cd $REPOSITORY/zip/build/libs
JAR_NAME=$(ls -tr ./*.jar | head -n 1)
echo "> JAR Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME 
echo "> $JAR_NAME 실행"
nohup sudo java -jar -Djasypt.encryptor.password=$ENCRYPT_KEY\
 $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
