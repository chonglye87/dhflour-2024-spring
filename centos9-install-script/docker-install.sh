#!/bin/bash

sudo yum install -y docker

sudo service docker start

sudo usermod -aG docker ec2-user

sudo chmod 666 /var/run/docker.sock

docker ps

if [ $? -eq 0 ]; then
    echo "Docker가 성공적으로 설치되었습니다."
else
    echo "Docker 설치에 실패했습니다."
fi

# Docker Compose 최신 버전 다운로드 및 설치
sudo curl -L "https://github.com/docker/compose/releases/download/$(sudo curl -s https://api.github.com/repos/docker/compose/releases/latest | jq -r .tag_name)/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Docker Compose 실행 권한 부여
sudo chmod +x /usr/local/bin/docker-compose

# Docker Compose 설치 확인
docker-compose --version