# DevOps Project Overview 🚀
This project showcases a CI/CD Pipeline using Jenkins for continuous deployment, Infrastructure as Code (IaC) via Terraform, and Kubernetes for container orchestration. It automates the deployment of a Flask application onto an AWS infrastructure, with the app hosted in Docker containers managed by Kubernetes.

# Key Components
Environment-specific variables defined in .tfvars files
Modularized Terraform structure: Compute, Network, and Security modules
Deployment of resources in AWS (us-east-1 region)
Provisioning and configuration using Ansible
Integration with Jenkins for automated CI/CD <br>

🚀 Getting Started
Prerequisites 🛠️
Ensure you have the following installed:
AWS Development EC2 
Terraform 🏗️
AWS CLI ☁️
Jenkins (Docker image recommended) 🐳
Python 🐍
Docker 🐋
Kubernetes CLI (kubectl) 🧩
Ansible 📜
AWS CLI Setup 🔑
# 1. Setting Up AWS and DockerHub Credentials in Jenkins:
Go to Manage Jenkins → Credentials and add AWS Access Key and Secret Access Key and DockerHub Creditials.<br>

Open a terminal and enter:<br>
`aws configure`

Input your AWS Access Key ID, Secret Access Key, Region (e.g., us-east-1), and output format (JSON).
Obtaining AWS Access Keys 🔑
Log in to AWS, go to Security Credentials.
Under Access Keys, create a new key and add it during aws configure.
Project Components 🚧

# 2. Terraform Modules for Infrastructure Setup
Network Module: Sets up the VPC, subnets, Internet Gateway, and route tables.
Compute Module: Configures EC2 instances, including the App EC2 instance for the Flask app.
Security Module: Defines security groups and IAM roles.

Execute Terraform Files:
`terraform apply`

# 2. Kubernetes for Containerized Deployment 🧩
Define Kubernetes manifests for namespace, deployment, and load balancer.
Use kubectl to deploy the manifests: 

`kubectl apply -f namespace.yaml` <br>
`kubectl apply -f deployment.yaml` <br>
`kubectl apply -f loadbalancer.yaml` <br>

# 3. Ansible for Configuration Management 📜
Playbook: Installs Docker and Kubernetes on the App EC2 instance, ensuring the app environment is ready.
Run the Ansible playbook:<br>
`ansible-playbook -i inventory.txt dockerk8s.yml`

# 4. Jenkins for CI/CD Integration 🤖
Pipeline Setup:
Clone GitHub repository
Trigger Terraform to provision infrastructure
Run Ansible playbook for configuration
Deploy Flask app using Kubernetes



# 6. Configure a new Pipeline Job with the pipeline script in CICD.groovy.
Select the environment to build with parameters, which triggers the complete deployment.





