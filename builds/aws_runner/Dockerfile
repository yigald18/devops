FROM registry.access.redhat.com/ubi8/python-38
WORKDIR /root
USER root
COPY run_deploy.sh .
ARG ENV_AWS_SHARED_CREDENTIALS_FILE=/root/.aws/credentials
ENV AWS_SHARED_CREDENTIALS_FILE=$ENV_AWS_SHARED_CREDENTIALS_FILE

RUN echo "stating runner build ... "
RUN yum install -y unzip wget && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && aws/install && rm -rf aws* && \
    mkdir .aws
RUN pip install ansible
RUN chmod +x run_deploy.sh
RUN echo "Build completed !! "
#ENTRYPOINT ["aws/deploy/deploy.sh"]
CMD ["./run_deploy.sh"]