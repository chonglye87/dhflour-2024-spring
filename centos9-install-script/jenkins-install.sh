#!/bin/bash

# OpenJDK 17 설치
echo "OpenJDK 17 설치를 시작합니다..."
sudo dnf install -y java-17-openjdk
echo "Java 설치 완료:"
java -version

echo "wget 설치를 시작합니다..."
sudo dnf install -y wget

# Jenkins 저장소 추가 및 키 임포트
echo "Jenkins 저장소 설정 중..."
sudo wget -O /etc/yum.repos.d/jenkins.repo \
    https://pkg.jenkins.io/redhat/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat/jenkins.io-2023.key

# Jenkins 설치
echo "Jenkins 설치를 시작합니다..."
sudo dnf install -y jenkins

# Jenkins 서비스 시작 및 부팅 시 자동 시작 설정
echo "Jenkins 서비스를 시작하고, 시스템 부팅 시 자동 시작하도록 설정합니다..."
sudo systemctl start jenkins
sudo systemctl enable jenkins

# 상태 확인
echo "Jenkins 서비스 상태:"
sudo systemctl status jenkins | grep "active (running)"

# 방화벽 설정 (필요한 경우)
echo "방화벽에서 8080 포트를 열어 Jenkins에 접근을 허용합니다..."
sudo firewall-cmd --permanent --zone=public --add-port=8080/tcp
sudo firewall-cmd --reload

echo "Jenkins 설치 및 설정이 완료되었습니다. 웹 브라우저를 통해 'http://<your_server_ip>:8080' 주소로 접속하여 Jenkins를 설정하세요."
