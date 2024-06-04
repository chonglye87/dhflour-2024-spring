#!/bin/bash

# OpenJDK 11 설치
echo "OpenJDK 11 설치를 시작합니다..."
#sudo dnf install -y java-11-openjdk
sudo dnf install -y java-17-amazon-corretto

# 설치 확인
echo "설치된 Java 버전을 확인합니다..."
java -version

if [ $? -eq 0 ]; then
    echo "Java가 성공적으로 설치되었습니다."
else
    echo "Java 설치에 실패했습니다."
fi
