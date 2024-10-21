#!/bin/bash

REMOTE_HOST="172.30.1.50"
REMOTE_USER=""   # control-plane 의 유저 아이디
REMOTE_PASS=""   # 비밀번호
REMOTE_SCRIPT_PATH="/home/comppi/join"
LOCAL_SCRIPT_PATH="./join_script.sh"  # 로컬에 저장할 경로

echo "쿠버네티스 클러스터에 참여하는 중..."
# sshpass 설치
sudo apt-get install sshpass

# containerd 재설정
sudo rm /etc/containerd/config.toml
sudo systemctl restart containerd

# SSH를 사용하여 원격 호스트에 접속하고, 쿠버네티스 join 스크립트를 실행
sudo sshpass -p "$REMOTE_PASS" scp -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_SCRIPT_PATH} ${LOCAL_SCRIPT_PATH}
chmod +x ${LOCAL_SCRIPT_PATH}
bash ${LOCAL_SCRIPT_PATH}
