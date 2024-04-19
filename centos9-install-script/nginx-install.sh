#!/bin/bash

# Nginx의 저장소 설정 파일 추가
echo "Nginx 저장소를 설정합니다..."
sudo tee /etc/yum.repos.d/nginx.repo <<EOF
[nginx-stable]
name=nginx stable repo
baseurl=http://nginx.org/packages/centos/\$releasever/\$basearch/
gpgcheck=1
enabled=1
gpgkey=https://nginx.org/keys/nginx_signing.key
module_hotfixes=true
EOF

# Nginx 설치
echo "Nginx를 설치합니다..."
sudo dnf install -y nginx

# Nginx 서비스 시작
echo "Nginx 서비스를 시작합니다..."
sudo systemctl start nginx

# Nginx 부팅 시 자동 시작 설정
echo "Nginx를 부팅 시 자동으로 시작하도록 설정합니다..."
sudo systemctl enable nginx

# 설치 및 실행 확인
echo "Nginx 설치 및 실행 상태를 확인합니다..."
sudo systemctl status nginx | grep "active (running)"

if [ $? -eq 0 ]; then
    echo "Nginx가 성공적으로 설치되어 실행 중입니다."
else
    echo "Nginx 설치 또는 실행에 문제가 발생했습니다."
fi