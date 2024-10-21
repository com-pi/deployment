Vagrant.configure("2") do |config|
  config.vm.box = "bento/ubuntu-24.04"
  config.vbguest.auto_update = false
  config.vm.synced_folder "./", "/vagrant", disabled: true

  # Master Node
  config.vm.define "master.comppi" do |node|
    node.vm.hostname = "master"
    node.vm.network "public_network", ip: "수동_할당", bridge: "네트워크_인터페이스_명"
    node.disksize.size = "64GB"
    node.vm.provision :shell, path: "./install_k8s_environment.sh"
    node.vm.provision :shell, path: "./init_k8s_cluster.sh"
    node.vm.provider :virtualbox do |vb|
      vb.memory = 6144
      vb.cpus = 4
    end

    # 마스터 노드가 준비되면 나머지 워커 노드를 올리는 트리거 설정
    node.trigger.after :up do
      run "vagrant up worker1.comppi"
      run "vagrant up worker2.comppi"
      run "vagrant up worker3.comppi"
    end
  end

  # Worker Node 1
  config.vm.define "worker1.comppi", autostart: false do |node|
    node.vm.hostname = "worker1"
    node.vm.network "public_network", ip: "수동_할당", bridge: "네트워크_인터페이스_명"
    node.disksize.size = "64GB"
    node.vm.provision :shell, path: "./install_k8s_environment.sh"
    node.vm.provision :shell, path: "./join_k8s_cluster.sh"
    node.vm.provider :virtualbox do |vb|
      vb.memory = 6144
      vb.cpus = 4
    end
  end

  # Worker Node 2
  config.vm.define "worker2.comppi", autostart: false do |node|
    node.vm.hostname = "worker2"
    node.vm.network "public_network", ip: "수동_할당", bridge: "네트워크_인터페이스_명"
    node.disksize.size = "64GB"
    node.vm.provision :shell, path: "./install_k8s_environment.sh"
    node.vm.provision :shell, path: "./join_k8s_cluster.sh"
    node.vm.provider :virtualbox do |vb|
      vb.memory = 6144
      vb.cpus = 4
    end
  end

  # Worker Node 3
  config.vm.define "worker3.comppi", autostart: false do |node|
    node.vm.hostname = "worker3"
    node.vm.network "public_network", ip: "172.30.1.53", bridge: "wlx588694fb5b61"
    node.disksize.size = "64GB"
    node.vm.provision :shell, path: "./install_k8s_environment.sh"
    node.vm.provision :shell, path: "./join_k8s_cluster.sh"
    node.vm.provider :virtualbox do |vb|
      vb.memory = 6144
      vb.cpus = 4
    end
  end
end