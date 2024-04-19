#!/bin/bash

# 설정 파일 경로 정의
config_file="/etc/nginx/conf.d/default.conf"

# Nginx 설정 파일 작성
echo "Nginx 설정 파일을 생성합니다..."
sudo tee $config_file > /dev/null <<EOF
server {
    listen 80   default_server;
    server_name localhost;

    charset utf-8;
    access_log  /var/log/nginx/default-access.log    main;
    error_log   /var/log/nginx/default-error.log   warn;

    location / {
        proxy_redirect     off;
        proxy_set_header   Host             \$host;
        proxy_set_header   X-Real-IP        \$remote_addr;
        proxy_set_header   X-Forwarded-For  \$proxy_add_x_forwarded_for;

        proxy_pass http://127.0.0.1:8080;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
}
EOF

# 설정 파일이 성공적으로 생성되었는지 확인
echo "생성된 설정 파일 내용:"
sudo cat $config_file

# Nginx 구성을 다시 불러옴
echo "Nginx 설정을 리로드합니다..."
sudo nginx -s reload
