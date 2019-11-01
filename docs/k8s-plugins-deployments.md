# Kubernetes cluster and plugins deployment instructions

This instruction will show you how to deploy kubernetes cluster and plugins in two example scenarios:
  * One node (All-in-one)
  * Three nodes (one master, two worker nodes)

> Please refer to the installation.md for how to install and configure the O-RAN INF platform.

## 1. One node (All-in-one) deployment example

### 1.1 Change the hostname

```
# Assuming the hostname is oran-aio, ip address is <aio_host_ip>
# please DO NOT copy and paste, use your actaul hostname and ip address
root@intel-x86-64:~# echo oran-aio > /etc/hostname
root@intel-x86-64:~# export AIO_HOST_IP="<aio_host_ip>"
root@intel-x86-64:~# echo "$AIO_HOST_IP oran-aio" >> /etc/hosts
```

### 1.2 Disable swap for Kubernetes

```
root@intel-x86-64:~# sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab
root@intel-x86-64:~# systemctl mask dev-sda4.swap
```

### 1.3 Set the proxy for docker (Optional) 

* If you are under a firewall, you may need to set the proxy for docker to pull images
```
root@intel-x86-64:~# HTTP_PROXY="http://<your_proxy_server_ip>:<port>"
root@intel-x86-64:~# mkdir /etc/systemd/system/docker.service.d/
root@intel-x86-64:~# cat << EOF > /etc/systemd/system/docker.service.d/http-proxy.conf
[Service]
Environment="HTTP_PROXY=$HTTP_PROXY" "NO_PROXY=localhost,127.0.0.1,localaddress,.localdomain.com,$AIO_HOST_IP,10.244.0.0/16"
EOF
```

### 1.4 Reboot the target

### 1.5 Initialize kubernetes cluster master

```
root@oran-aio:~# kubeadm init --kubernetes-version v1.15.2 --pod-network-cidr=10.244.0.0/16
root@oran-aio:~# mkdir -p $HOME/.kube
root@oran-aio:~# cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
root@oran-aio:~# chown $(id -u):$(id -g) $HOME/.kube/config
```

### 1.6 Make the master also works as a worker node

```
root@oran-aio:~# kubectl taint nodes oran-aio node-role.kubernetes.io/master-
```

### 1.7 Deploy flannel

```
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/flannel/kube-flannel.yml
```

* Check that the aio node is ready after flannel is successfully deployed and running

```
root@oran-aio:~# kubectl get pods --all-namespaces |grep flannel
kube-system   kube-flannel-ds-amd64-bwt52        1/1     Running   0          3m24s

root@oran-aio:~# kubectl get nodes
NAME       STATUS   ROLES    AGE     VERSION
oran-aio   Ready    master   3m17s   v1.15.2-dirty
```

### 1.8 Deploy kubernetes dashboard

```
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/kubernetes-dashboard/kubernetes-dashboard-admin.rbac.yaml
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/kubernetes-dashboard/kubernetes-dashboard.yaml
```

* Verify that the dashboard is up and running

```
# Check the pod for dashboard
root@oran-aio:~# kubectl get pods --all-namespaces |grep dashboard
kube-system   kubernetes-dashboard-5b67bf4d5f-ghg4f   1/1     Running   0          64s

```

* Access the dashboard UI in a web browser with the url: https://<aio_host_ip>:30443

* For detail usage, please refer to [doc for dashboard](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/)

### 1.9 Deploy Multus-CNI

```
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/multus-cni/multus-daemonset.yml
```

* Verify that the multus-cni is up and running

```
root@oran-aio:~# kubectl get pods --all-namespaces | grep -i multus
kube-system   kube-multus-ds-amd64-hjpk4              1/1     Running   0          7m34s
```

* For further validating, please refer to the [multus quick start](https://github.com/intel/multus-cni/blob/master/doc/quickstart.md)

### 1.10 Deploy NFD (node-feature-discovery)

```
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/node-feature-discovery/nfd-master.yaml
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/node-feature-discovery/nfd-worker-daemonset.yaml
```

* Verify that nfd-master and nfd-worker are up and running

```
root@oran-aio:~# kubectl get pods --all-namespaces |grep nfd
default       nfd-master-7v75k                        1/1     Running   0          91s
default       nfd-worker-xn797                        1/1     Running   0          24s
```

* Verify that the node is labeled by nfd:

```
root@oran-aio:~# kubectl describe nodes|grep feature.node.kubernetes
                   feature.node.kubernetes.io/cpu-cpuid.AESNI=true
                   feature.node.kubernetes.io/cpu-cpuid.AVX=true
                   feature.node.kubernetes.io/cpu-cpuid.AVX2=true
                   (...snip...)
```

### 1.11 Deploy CMK (CPU-Manager-for-Kubernetes)


* Build the CMK docker image

```
root@oran-aio:~# cd /opt/kubernetes_plugins/cpu-manager-for-kubernetes/
root@oran-aio:/opt/kubernetes_plugins/cpu-manager-for-kubernetes# make
```

* Verify that the cmk docker images is built successfully

```
root@oran-aio:/opt/kubernetes_plugins/cpu-manager-for-kubernetes# docker images|grep cmk
cmk          v1.3.1              3fec5f753b05        44 minutes ago      765MB
```

* Edit the template yaml file for your deployment:
  * The template file is: /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-cluster-init-pod-template.yaml
  * The options you may need to change:
```
    # You can change the value for the following env:
    env:
    - name: HOST_LIST
      # Change this to modify the the host list to be initialized
      value: "oran-aio"               
    - name: NUM_EXCLUSIVE_CORES
      # Change this to modify the value passed to `--num-exclusive-cores` flag
      value: "4"                  
    - name: NUM_SHARED_CORES
      # Change this to modify the value passed to `--num-shared-cores` flag
      value: "1"
    - name: CMK_IMG
      # Change his ONLY if you built the docker images with a different tag name
      value: "cmk:v1.3.1"

```
  * Or you can also refer to [CMK operator manual](https://github.com/intel/CPU-Manager-for-Kubernetes/blob/master/docs/operator.md)

```
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-rbac-rules.yaml
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-serviceaccount.yaml
root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-cluster-init-pod-template.yaml
```

* Verify that the cmk cluster init completed and the pods for nodereport and webhook deployment are up and running

```
root@oran-aio:/opt/kubernetes_plugins/cpu-manager-for-kubernetes# kubectl get pods --all-namespaces |grep cmk
default       cmk-cluster-init-pod                         0/1     Completed   0          11m
default       cmk-init-install-discover-pod-oran-aio       0/2     Completed   0          10m
default       cmk-reconcile-nodereport-ds-oran-aio-qbdqb   2/2     Running     0          10m
default       cmk-webhook-deployment-6f9dd7dfb6-2lj2p      1/1     Running     0          10m
```

* For detail usage, please refer to [CMK user manual](https://github.com/intel/CPU-Manager-for-Kubernetes/blob/master/docs/user.md)

## 2. Three nodes deployment example

TBD
