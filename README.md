# Getting Started

### Multi Modules 구축

#### 모듈형 프로젝트 구조

- api: API 서버 모듈
- core: 비즈니스 로직과 데이터 모델
- admin: 관리자 관련 기능 모듈

````
project-root/
|-- api/
|-- core/
|-- admin/
|-- build.gradle
|-- settings.gradle
|-- gradlew
|-- gradlew.bat
|-- .gitignore
````

#### settings.gradle 설정

````
rootProject.name = 'dhflour-demo-1'
include 'api', 'core', 'admin'
````

#### build.gradle 사용 방법

1. {project}/build.gradle

- 모든 모듈에 공통으로 사용되는 모듈 의존

2. {project}/core/build.gradle

-

3. {project}/api/build.gradle

-   