# CentOS 9에서 Nginx 서버 설치 및 설정 가이드

이 가이드는 CentOS 9에서 Nginx를 설치하고 기본 프록시 설정을 적용하는 방법을 단계별로 설명합니다.

## 1. Nginx 설치

### 1.1. Nginx yum 설치 저장소 업데이트

먼저, Nginx 패키지를 설치할 수 있도록 yum 저장소를 업데이트해야 합니다. 이를 위해 다음 명령어를 실행하여 저장소 설정 파일을 생성합니다:

```bash
sudo tee /etc/yum.repos.d/nginx.repo <<EOF
[nginx-stable]
name=nginx stable repo
baseurl=http://nginx.org/packages/centos/\$releasever/\$basearch/
gpgcheck=1
enabled=1
gpgkey=https://nginx.org/keys/nginx_signing.key
module_hotfixes=true
EOF
```

### 1.2. Nginx 설치

이제 Nginx를 설치할 수 있습니다. 다음 명령어를 실행하세요:

```bash
sudo dnf install -y nginx
```

### 1.3. Nginx 서비스 시작

설치가 완료되면 Nginx 서비스를 시작합니다:

```bash
sudo systemctl start nginx
```

### 1.4. 재부팅 시 Nginx 자동 시작 설정

Nginx가 시스템 재부팅 시 자동으로 시작되도록 설정합니다:

```bash
sudo systemctl enable nginx
```

### 1.5. Nginx 상태 확인

Nginx 서비스가 정상적으로 실행 중인지 확인합니다:

```bash
sudo systemctl status nginx | grep "active (running)"
```

## 2. Nginx Proxy 설정

### 2.1. Nginx 설정 파일 생성

기본 프록시 설정을 적용하기 위해 Nginx 설정 파일을 생성합니다. 다음 명령어를 실행하여 `/etc/nginx/conf.d/default.conf` 파일을 생성하고 설정을 추가합니다:

```bash
sudo tee "/etc/nginx/conf.d/default.conf" > /dev/null <<EOF
server {
    listen 80 default_server;
    server_name localhost;
    
    charset utf-8;
    access_log  /var/log/nginx/default-access.log main;
    error_log   /var/log/nginx/default-error.log warn;
    
    location / {
        proxy_redirect     off;
        proxy_set_header   Host             \$host;
        proxy_set_header   X-Real-IP        \$remote_addr;
        proxy_set_header   X-Forwarded-For  \$proxy_add_x_forwarded_for;
        
        proxy_pass http://127.0.0.1:8000;
    }
    
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
}
EOF
```

### 2.2. 설정 파일 확인

생성한 설정 파일의 내용을 확인합니다:

```bash
sudo cat "/etc/nginx/conf.d/default.conf"
```

### 2.3. 설정 적용 (Nginx 재기동)

변경된 설정을 적용하기 위해 Nginx를 재기동합니다:

```bash
sudo nginx -s reload
```

이로써 Nginx 설치와 기본 프록시 설정이 완료되었습니다. 이제 Nginx 서버가 80번 포트에서 요청을 받아 127.0.0.1:8000으로 프록시합니다.