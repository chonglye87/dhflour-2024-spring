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
