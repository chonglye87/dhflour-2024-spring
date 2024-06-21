# CentOS9 기준 Docker 및 Docker Compose 설치
### 1. Docker 및 Docker Compose 설치
#### a. Docker 설치
```
sudo yum install -y docker

sudo service docker start

sudo usermod -aG docker ec2-user

sudo chmod 666 /var/run/docker.sock

docker ps
```
#### b. Docker Compose 설치
```bash
# Docker Compose 최신 버전 다운로드 및 설치
sudo curl -L "https://github.com/docker/compose/releases/download/$(sudo curl -s https://api.github.com/repos/docker/compose/releases/latest | jq -r .tag_name)/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Docker Compose 실행 권한 부여
sudo chmod +x /usr/local/bin/docker-compose

# Docker Compose 설치 확인
docker-compose --version
```

### 2. Docker Compose 실행

작성한 `docker-compose.yml` 파일이 있는 디렉토리에서 다음 명령어를 실행합니다.

```bash
# 도터 apppush 설치 폴더 이동 
cd ~/docker/apppush
# Docker Compose 서비스 시작
sudo docker-compose up -d
```

이 명령어는 Docker Compose를 사용하여 컨테이너를 백그라운드에서 실행합니다.

### 3. Docker Compose 서비스 확인

Docker Compose가 올바르게 실행되었는지 확인하려면 다음 명령어를 사용하여 실행 중인 컨테이너를 확인할 수 있습니다.

```bash
sudo docker-compose ps
```

### 4. (선택 사항) Docker Compose 종료

서비스를 종료하려면 다음 명령어를 사용합니다.

```bash
sudo docker-compose down
```

이 명령어는 모든 컨테이너를 중지하고 네트워크를 제거합니다.

### 5. (선택 사항) Docker Compose 명령어 자동 완성 설정

편의성을 위해 Docker Compose 명령어의 자동 완성을 설정할 수 있습니다.

```bash
sudo curl -L https://raw.githubusercontent.com/docker/compose/1.29.2/contrib/completion/bash/docker-compose -o /etc/bash_completion.d/docker-compose
```
