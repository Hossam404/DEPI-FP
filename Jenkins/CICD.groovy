pipeline {
    agent any

    parameters {
        choice(name: 'action', choices: ["Apply", "Destroy"], description: 'Select Terraform Action')
    }

    environment {
        AWS_ACCESS_KEY_ID = credentials('aws_access_key_id')
        AWS_SECRET_ACCESS_KEY = credentials('aws_secret_access_key')
        DOCKER_REGISTRY = 'hussamgamal/flask-app'
        ANSIBLE_PRIVATE_KEY = 'Terraform/tf-key-pairz.pem'
        PLAYBOOK_FILE = 'playbook/dockerk8s.yaml'
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Access Remote Repo') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/Hossam404/DEPI-FP']]])
            }
        }

        stage('Terraform Init and Action') {
            steps {
                script {
                    dir('Terraform') {
                        sh 'terraform init -upgrade'
                        sh 'terraform plan -var-file="network/network.tfvars" -var-file="compute/compute.tfvars"'

                        def tfaction = params.action
                        if (tfaction == 'Apply') {
                            sh 'terraform apply -var-file="network/network.tfvars" -var-file="compute/compute.tfvars" -auto-approve'

                            def inventoryPath = 'inventory.txt'
                            if (fileExists(inventoryPath)) {
                                env.INVENTORY_CONTENT = readFile(inventoryPath)
                            } else {
                                error("inventory.txt not found after apply!")
                            }
                        } else if (tfaction == 'Destroy') {
                            sh 'terraform destroy -var-file="network/network.tfvars" -var-file="compute/compute.tfvars" -auto-approve'
                        } else {
                            error("Invalid choice for 'action' parameter")
                        }
                    }
                }
            }
        }


        stage('Test Inventory') {
            when {
                expression { params.action == 'Apply' && env.INVENTORY_CONTENT }
            }
            steps {
                script {
                    sh "cat Terraform/inventory.txt"
                }
            }
        }
        
        stage('Wait for EC2') {
            when {
                expression { params.action == 'Apply' }
            }
            steps {
                script {
                    echo 'Waiting for EC2 instance to boot up...'
                    sleep(time: 60, unit: 'SECONDS') 
                }
            }
        }

stage('Test SSH') {
    when {
        expression { params.action == 'Apply' && env.INVENTORY_CONTENT }
    }
    steps {
        script {
            def publicIp = sh(script: "grep ansible_user=ec2-user Terraform/inventory.txt | awk '{print \$1}'", returnStdout: true).trim()
            echo "Attempting SSH to EC2 instance at IP: ${publicIp}"
            sh "chmod 400 ${ANSIBLE_PRIVATE_KEY}"
            sh "ls -l"
            sh "ssh -i ${ANSIBLE_PRIVATE_KEY} -o StrictHostKeyChecking=no ec2-user@${publicIp} exit"
        }
    }
}

        
        stage('Run Ansible Playbook') {
            when {
                expression { params.action == 'Apply' && env.INVENTORY_CONTENT }
            }
            steps {
                sh 'ansible-playbook -i Terraform/inventory.txt ${PLAYBOOK_FILE}'
            }
        }

        stage('Copy K8SManifests to EC2') {
            when {
                expression { params.action == 'Apply' && env.INVENTORY_CONTENT }
            }
            steps {
                script {
                    def publicIp = sh(script: "grep ansible_user=ec2-user Terraform/inventory.txt | awk '{print \$1}'", returnStdout: true).trim()

                    sh """
                        scp -i ${ANSIBLE_PRIVATE_KEY} -r K8SManifests ec2-user@${publicIp}:/home/ec2-user/K8SManifests
                    """
                }
            }
        }

        stage('Build and Tag Docker Image') {
            when {
                expression { params.action == 'Apply' }
            }
            steps {
                sh 'docker build -t flask-app:v1 .'
                sh "docker tag flask-app:v1 ${DOCKER_REGISTRY}:v1"
            }
        }

        stage('Configure Docker & Login') {
            when {
                expression { params.action == 'Apply' }
            }
            steps {
                script {
                    sh """
                        echo "${DOCKER_HUB_CREDENTIALS_PSW}" | docker login -u "${DOCKER_HUB_CREDENTIALS_USR}" --password-stdin
                    """
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            when {
                expression { params.action == 'Apply' }
            }
            steps {
                sh 'docker push ${DOCKER_REGISTRY}:v1'
            }
        }

      stage('Deploy App on Minikube') {
    when {
        expression { params.action == 'Apply' }
    }
    steps {
        script {
            def publicIp = sh(script: "grep ansible_user=ec2-user Terraform/inventory.txt | awk '{print \$1}'", returnStdout: true).trim()

            sh """
            ssh -i ${ANSIBLE_PRIVATE_KEY} ec2-user@${publicIp} <<EOF
            sudo usermod -aG docker ec2-user
            newgrp docker  

            minikube start --driver=docker

            sleep 90

            kubectl apply -f K8SManifests/namespace.yml 
            kubectl apply -f K8SManifests/Deploymnet.Yaml 
            kubectl apply -f K8SManifests/LoadBalancer.yaml

            sleep 70
            kubectl get svc -o wide -n flask-app
EOF
            """
        }
    }
}

        stage('Destroy Confirmation') {
            when {
                expression { params.action == 'Destroy' }
            }
            steps {
                echo "SUCCESS: All Resources Destroyed"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}