# 운영 빌드 및 실행 가이드

이 가이드는 프로젝트의 API를 빌드하고 실행하는 방법을 단계별로 설명합니다.

## 1. 폴더 이동

먼저, 프로젝트의 API 디렉토리로 이동합니다. 다음 명령어를 사용하세요:

```bash
cd {프로젝트폴더}/api
```

## 2. 빌드

Gradle을 사용하여 프로젝트를 빌드합니다. 다음 명령어를 실행하세요:

```bash
gradle build
```

## 3. 실행

빌드가 완료되면 실행 파일이 생성된 디렉토리로 이동합니다:

```bash
cd {프로젝트폴더}/api/build/libs
```

### 개발 환경에서 실행

개발 환경에서 실행하려면 다음 명령어를 사용하세요:

```bash
java -jar api.jar --spring.profiles.active=dev
```

### 운영 환경에서 실행

운영 환경에서 실행하려면 다음 명령어를 사용하세요:

```bash
java -jar api.jar --spring.profiles.active=prod
```

## 4. 데몬 실행

운영 환경에서 애플리케이션을 백그라운드에서 실행하려면 다음 명령어를 사용합니다:

```bash
nohup java -jar api.jar --spring.profiles.active=prod > logs/app.log 2>&1 &
```

이 명령어는 애플리케이션을 백그라운드에서 실행하고, 로그를 `logs/app.log` 파일에 기록합니다.

이로써 프로젝트의 API를 빌드하고 실행하는 모든 단계를 완료했습니다.