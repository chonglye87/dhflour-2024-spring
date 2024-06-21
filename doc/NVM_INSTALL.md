# NVM 설치 및 Node.js 설정 가이드

이 가이드는 NVM(Node Version Manager)을 설치하고 Node.js v20을 설정하는 방법을 단계별로 설명합니다. 또한 PM2를 설치하여 Node.js 애플리케이션을 관리하는 방법도 포함되어 있습니다.

### 1. NVM 설치

NVM을 설치하기 위해 다음 명령어를 실행합니다:

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
```

이 명령어는 NVM 설치 스크립트를 다운로드하고 실행합니다.

### 2. 환경 설정

NVM을 설치한 후, 환경 변수를 설정하여 NVM을 활성화합니다:

```bash
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # nvm 스크립트를 로드
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # bash_completion을 로드하여 nvm 명령어 자동완성 활성화
```

### 3. 설치 확인

NVM이 정상적으로 설치되었는지 확인합니다:

```bash
command -v nvm
```

### 4. Node.js v20 설치 및 사용

NVM을 사용하여 Node.js v20을 설치하고 사용 설정합니다:

```bash
nvm install 20
nvm use 20
```

현재 사용 중인 Node.js 버전을 확인합니다:

```bash
node -v
```

### 5. PM2 설치

Node.js 애플리케이션을 관리하기 위해 PM2를 설치합니다:

```bash
npm i -g pm2
pm2 -v
```

이제 NVM, Node.js, 그리고 PM2가 성공적으로 설치되고 설정되었습니다.

이 문서를 참고하여 NVM을 설치하고 Node.js 및 PM2를 설정할 수 있습니다. 필요한 경우 스크립트를 실행하여 자동으로 설치 및 설정할 수 있습니다.