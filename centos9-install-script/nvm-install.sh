#!/bin/bash

# NVM 설치 스크립트를 다운로드하고 실행
echo "NVM 설치를 시작합니다..."
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash

# 환경 설정 스크립트 실행
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # nvm 스크립트를 로드
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # bash_completion을 로드하여 nvm 명령어 자동완성 활성화

# 설치 확인
echo "NVM 버전을 확인합니다..."
command -v nvm

if [ $? -eq 0 ]; then
    echo "NVM이 성공적으로 설치되었습니다."
    # Node.js v20 설치
    echo "Node.js v20을 설치합니다..."
    nvm install 20
    nvm use 20
    echo "현재 사용 중인 Node.js 버전:"
    node -v
    echo "PM2를 설치합니다..."
    npm i -g pm2
    pm2 -v
else
    echo "NVM 설치에 실패했습니다."
fi
