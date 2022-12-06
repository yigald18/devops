#!/usr/bin/env groovy

pipeline {
    agent { label "${params.JENKINS_AGENT}" }

    options {
        ansiColor('xterm')
    }

    parameters {
        choice(name: 'ACTION',            choices: ["destroy", "create"], description: '')
        choice(name: 'AWS_REGION',        choices: ["us-east-1", "us-east-2", "us-west-1", "us-west-2", "eu-west-1", "eu-west-2", "me-south-1"], description: '')
        separator(name: "",sectionHeader: "Misc")
        separator(name: "",sectionHeader: "")
        string(name: 'SCHED_DESTROY',     defaultValue: '0', description: 'if to schedule cluster destroy in X (>0) hours')
        string(name: 'JENKINS_AGENT', defaultValue: 'static-generic', description: 'Jenkins Agent Label')
    }

    stages {
        stage("Init") {
            steps {
                script {
                    try {
                        env.LOGGED_USER  = currentBuild.rawBuild.getCause(Cause.UserIdCause).getUserId()
                    }
                    catch(e) {
                        println("Error occurred: " + e)
                        error("Error occurred: " + e)
                    }
                    currentBuild.description = "AWS website {ACTION}"
                }
            }
        }

        stage('Env action') {
            steps {
                withCredentials([file(credentialsId: "aws-credentials-$LOGGED_USER-priv", variable: "AWS_CREDS")]) {
                    script {
                        sh """
                            podman run -it --rm --net=host --pull=always -e AWS_PROFILE=yigald2 \
                            -e AWS_REGION=us-east-1 -v $AWS_CREDS:/root/.aws/credentials:Z quay.io/yigald/dev
                        """
                    }
                }
            }
            post {
                always {
                    script {
                        if (params.ACTION == 'create') {
                            if (params.SCHED_DESTROY != '0') {
                                build job: env.JOB_NAME, parameters: [
                                    [$class: 'StringParameterValue', name: 'ACTION', value: "destroy"],
                                ], wait: false, propagate: false, quietPeriod: env.SCHED_DESTROY.toInteger() * 60 * 60
                            }
                        }
                    }
                }
            }
        }
    }
}
