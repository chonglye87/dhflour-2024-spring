# Getting Started

## Multi Modules 구축

### 모듈형 구조

프로젝트는 다음과 같은 모듈형 구조로 구성됩니다:

- **api**: Webflux 비동기 API 서버 모듈
- **core**: 공통 로직 관리 모듈
- **batch**: 스프링 배치 기반 일괄 처리 모듈
- **jpa**: JPA 기반 데이터베이스 관리 모듈

### 디렉토리 구조

다음은 각 모듈의 디렉토리 구조입니다:

#### API 모듈 (Netty/논블록킹 비동기적 아키텍처 API)

```
api/
├── src/
│   ├── main/
│   │   ├── java/ : JAVA
│   │   └── resources/ : 리소스
│   └── test/
├── build.gradle
```

#### 일괄처리(배치) 모듈 (Undertow)

```
batch/
├── src/
│   ├── main/
│   │   ├── java/ : JAVA
│   │   └── resources/ : 리소스
│   └── test/
├── build.gradle
```

#### 공통 모듈

```
core/
├── src/
│   ├── main/
│   │   ├── java/ : JAVA
│   │   └── resources/ : 리소스
│   └── test/
├── build.gradle
```

#### JPA 데이터베이스 관리 모듈

```
jpa/
├── src/
│   ├── main/
│   │   ├── java/ : JAVA
│   │   └── resources/ : 리소스
│   └── test/
├── build.gradle
```

#### 프로젝트 관리 (Gradle)

```
build.gradle
settings.gradle
gradlew
gradlew.bat
.gitignore
```

## 프로젝트 세부 구조

모듈 내부는 다음과 같은 패키지 구조를 따릅니다:

```
{패키지명}/config    : 설정 파일
{패키지명}/domain    : 데이터베이스 테이블 관련 클래스
{패키지명}/service   : 비즈니스 로직 클래스
{패키지명}/types     : 객체 타입 정의
{패키지명}/utils     : 유틸리티 클래스
{패키지명}/web       : API 및 Swagger 관련 클래스
```
이 구조를 통해 각 모듈과 패키지가 명확히 분리되어 관리되며, 역할에 따라 코드가 조직화됩니다.

## 설정 세부 구조
```
{패키지명}/config/api     : API / Web 관련 설정
{패키지명}/config/auth    : 인가/인증, 권한 관련 설정
{패키지명}/config/db      : 데이터베이스 관련 설정
{패키지명}/config/logger  : 로그, 옵져빌리티 관련 설정 
{패키지명}/config/secure  : 보안 관련 설정
```

---

## 프로젝트 설치 및 실행 가이드

### 1. Nginx 설치 및 실행
Nginx 설치 및 실행 방법은 [NGINX_INSTALL.md](./doc/NGINX_INSTALL.md) 파일을 참고하세요.

### 2. JDK 17 설치 및 실행
JDK 17 설치 및 실행 방법은 [JDK17_INSTALL.md](./doc/JDK17_INSTALL.md) 파일을 참고하세요.

### 3. Docker / Docker Compose 설치 및 실행
Docker 설치 및 실행 방법은 [DOCKER_INSTALL.md](./doc/DOCKER_INSTALL.md) 파일을 참고하세요.

### 4. NVM(Node Version Manager) / PM2 설치
NVM 설치 방법은 [NVM_INSTALL.md](./doc/NVM_INSTALL.md) 파일을 참고하세요.

### 5. Git 설치
Git 설치 방법은 [GIT_INSTALL.md](./doc/GIT_INSTALL.md) 파일을 참고하세요.

### 6. Jenkins 설치
Jenkins 설치 방법은 [JENKINS_INSTALL.md](./doc/JENKINS_INSTALL.md) 파일을 참고하세요.

---

## API 명세 확인
```bash
cd api
gradle clean bootRun
```
[Swagger API](http://localhost:8080/swagger-ui.html) 바로가기
