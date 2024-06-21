### 운영 빌드 및 실행
1. 폴더 이동
```
cd {프로젝트폴더}/api
```

2. 빌드
```
gradle build
``` 

3. 실행
```
cd {프로젝트폴더}/api/build/libs

[개발계 실행]
java -jar api.jar --spring.profiles.active=dev

[운영계 실행]
java -jar api.jar --spring.profiles.active=prod
```

4. 데몬 실행
```
nohup java -jar api.jar --spring.profiles.active=prod > app.log 2>&1 &
```