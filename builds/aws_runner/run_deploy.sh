#!/usr/bin/bash
set -x
echo "Deploy runner INIT, print packages version ...\n"
aws --version
python -V
ansible --version
export AWS_PROFILE=yigald2
aws ec2 describe-key-pairs

TEMPLATE_BODY=file:///home/ydalal/projects/devops1-webapp/aws/cloudformation/templates/ec2.yaml
PARAMETERS_FILE=file:///home/ydalal/projects/devops1-webapp/aws/cloudformation/templates/ec2_params.json

echo "Starting deploy action: $ACTION"
if [[ "${ACTION}" == "deploy" ]]; then
      aws cloudformation create-stack --stack-name ec2-website --template-body $TEMPLATE_BODY --parameters $PARAMETERS_FILE
      aws cloudformation wait stack-create-complete --stack-name ec2-website
      aws cloudformation describe-stacks --stack-name ec2-website
      EC2_ID=$(aws cloudformation describe-stacks --stack-name ec2-website --outpu json | jq -r '.Stacks |.[].Outputs|.[] |select(.OutputKey=="EC2InstanceID").OutputValue')
      EC2_PUB_IP=$(aws ec2 describe-instances --instance-ids $EC2_ID --output json | jq '.Reservations | .[0].Instances | .[0].PublicIpAddress')
elif
  [[ "${ACTION}" == "destroy" ]]; then
    aws cloudformation describe-stacks --stack-name ec2-website
    aws cloudformation delete-stack --stack-name ec2-website
    aws cloudformation wait stack-delete-complete --stack-name ec2-website
else
    echo "*** Error *** ACTION: $ACTION Not valid."
    exit 1
fi
