# Install docker on RHEL ec2 
https://docs.docker.com/engine/install/rhel/
   ```
    sudo yum-config-manager \
        --add-repo \
        https://download.docker.com/linux/rhel/docker-ce.repo
   ```  
- Fix the yum repo issue with docker on RHEL  
  https://access.redhat.com/discussions/6249651
  Just add the following part to your yum.repos.d-directory should work. (delete all pre existing docker related entries)
  ``` 
    Raw
    [docker-ce-stable]
    name=Docker CE Stable - $basearch
    baseurl=https://download.docker.com/linux/centos/$releasever/$basearch/stable
    enabled=1
    gpgcheck=1
    gpgkey=https://download.docker.com/linux/centos/gpg
  
  sudo yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
   ```
- Create the docker image

    ```
    sudo podman tag 61a8b6b50bb4 quay.io/yigald/test.py:v1
    sudo podman push --creds=yigald+yigald_robo:XXXX quay.io/yigald/dev/main-test-py:v1
    sudo podman run -it --rm quay.io/yigald/test.py:v1
    ```
 - Create stack 
    ```
   aws cloudformation create-stack --stack-name ec2-website --template-body file:///home/ydalal/projects/devops1-webapp/aws/cloudformation/templates/ec2.yaml --parameters file:///home/ydalal/projects/devops1-webapp/aws/cloudformation/templates/ec2_params.json --profile yigald2
   ssh -i yigald2/key1.pem ec2-user@ec2-54-163-195-179.compute-1.amazonaws.com
   docker run -it quay.io/yigald/test.py:v1
   aws cloudformation delete-stack --stack-name ec2-website --profile yigald2
   aws cloudformation wait stack-delete-complete --stack-name ec2-website --profile yigald2
   ```
