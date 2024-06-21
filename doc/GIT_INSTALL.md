# Git 설치 가이드

## 1. 시스템 패키지 업데이트

먼저, 시스템의 패키지 목록을 업데이트합니다. 터미널에서 다음 명령어를 실행하세요:

```bash
sudo dnf update -y
```

## 2. Git 설치

Git을 설치하려면 다음 명령어를 실행하세요:

```bash
sudo dnf install git -y
```

## 3. 설치 확인

Git이 정상적으로 설치되었는지 확인하려면 다음 명령어를 실행하세요:

```bash
git --version
```

정상적으로 설치되었다면, 설치된 Git의 버전이 출력됩니다. 예를 들어:

```
git version 2.32.0
```

## 4. Git 기본 설정

Git을 설치한 후에는 사용자 이름과 이메일을 설정해야 합니다. 다음 명령어를 사용하세요:

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

설정 내용을 확인하려면 다음 명령어를 실행하세요:

```bash
git config --global --list
```

## 5. SSH 키 생성 (선택 사항)

GitHub, GitLab 등 원격 저장소와 SSH를 사용하여 연결하려면 SSH 키를 생성해야 합니다. 다음 명령어를 실행하세요:

```bash
ssh-keygen -t rsa -b 4096 -C "your.email@example.com"
```

명령어를 실행한 후, 기본 경로(예: `~/.ssh/id_rsa`)를 사용하고, 필요한 경우 패스프레이즈를 입력합니다. 생성된 SSH 키를 출력하려면 다음 명령어를 실행하세요:

```bash
cat ~/.ssh/id_rsa.pub
```

출력된 SSH 키를 원격 저장소 서비스(GitHub, GitLab 등)의 SSH 키 설정에 추가합니다.

## 6. 설치 완료

이로써 Amazon Linux 2023에서 Git 설치 및 기본 설정이 완료되었습니다. 이제 Git을 사용하여 소스 코드를 관리할 수 있습니다.

이 가이드를 참고하여 Git을 설치하고 설정하세요. 필요한 경우 추가 설정을 통해 환경을 맞춤화할 수 있습니다.