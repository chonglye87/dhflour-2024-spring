# 1. OpenJDK 17 설치

1. **OpenJDK 17 설치 시작:**
    ```bash
    echo "OpenJDK 17 설치를 시작합니다..."
    sudo dnf install -y java-17-openjdk
    ```

2. **설치 완료 확인:**
    ```bash
    echo "Java 설치 완료:"
    java -version
    ```

### 2. wget 설치

1. **wget 설치 시작:**
    ```bash
    echo "wget 설치를 시작합니다..."
    sudo dnf install -y wget
    ```

### 3. Jenkins 저장소 추가 및 키 임포트

1. **Jenkins 저장소 설정:**
    ```bash
    echo "Jenkins 저장소 설정 중..."
    sudo wget -O /etc/yum.repos.d/jenkins.repo \
        https://pkg.jenkins.io/redhat/jenkins.repo
    ```

2. **Jenkins 키 임포트:**
    ```bash
    sudo rpm --import https://pkg.jenkins.io/redhat/jenkins.io-2023.key
    ```

3. **시스템 업데이트:**
    ```bash
    sudo dnf update
    ```

### 4. Jenkins 설치

1. **Jenkins 설치 시작:**
    ```bash
    echo "Jenkins 설치를 시작합니다..."
    sudo dnf install -y jenkins
    ```

### 5. Jenkins 서비스 시작 및 부팅 시 자동 시작 설정

1. **Jenkins 서비스 시작:**
    ```bash
    echo "Jenkins 서비스를 시작하고, 시스템 부팅 시 자동 시작하도록 설정합니다..."
    sudo systemctl start jenkins
    ```

2. **부팅 시 자동 시작 설정:**
    ```bash
    sudo systemctl enable jenkins
    ```

3. **서비스 상태 확인:**
    ```bash
    echo "Jenkins 서비스 상태:"
    sudo systemctl status jenkins | grep "active (running)"
    ```

### 6. 방화벽 설정 (필요한 경우)

1. **8080 포트 열기:**
    ```bash
    echo "방화벽에서 8080 포트를 열어 Jenkins에 접근을 허용합니다..."
    sudo firewall-cmd --permanent --zone=public --add-port=8080/tcp
    ```

2. **방화벽 설정 재시작:**
    ```bash
    sudo firewall-cmd --reload
    ```

### 7. Jenkins 설정 완료 안내

1. **Jenkins 설치 및 설정 완료 메시지:**
    ```bash
    echo "Jenkins 설치 및 설정이 완료되었습니다. 웹 브라우저를 통해 'http://<your_server_ip>:8080' 주소로 접속하여 Jenkins를 설정하세요."
    ```

이제 이 단계를 순서대로 따라가면 OpenJDK 17과 Jenkins를 성공적으로 설치하고 설정할 수 있습니다. 설치가 완료되면 웹 브라우저에서 `http://<your_server_ip>:8080`으로 접속하여 Jenkins 초기 설정을 진행하십시오.