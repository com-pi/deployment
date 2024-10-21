#!/bin/bash

username="comppi"

# 새로운 사용자 추가
echo "방화벽을 비활성화하는 중..."
sudo useradd -m -s /bin/bash $username # 유저 이름
echo "$username:mypassword" | sudo chpasswd
sudo usermod -aG sudo $username

# 방화벽 끄기
echo "방화벽을 비활성화하는 중..."
sudo ufw disable

# 서버 시간대 설정 (서울)
echo "서버의 시간대를 서울로 설정하는 중..."
sudo timedatectl set-timezone Asia/Seoul

# 스왑 메모리 해제
echo "스왑 메모리를 비활성화하는 중..."
sudo swapoff -a

# /etc/fstab 파일에서 스왑 파티션 제거하여 부팅 시 스왑이 사용되지 않도록 설정
echo "부팅 시 스왑을 비활성화하도록 설정하는 중..."
sudo sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab

echo "호스트 이름 설정중..."
cat << EOF >> /etc/hosts
172.30.1.50 master
172.30.1.51 worker1
172.30.1.52 worker2
172.30.1.53 worker3
172.30.1.54 worker4
172.30.1.55 worker5
172.30.1.60 cicd
172.30.1.70 nfs
EOF

echo "컨테이너 런타임(도커) 설치중..."
for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do sudo apt-get remove $pkg; done
sudo apt-get update
sudo apt-get install ca-certificates curl -y
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io -y

echo "쿠버네티스(1.31v) 설치중..."
sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates curl gpg
curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.31/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.31/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
sudo systemctl enable --now kubelet

