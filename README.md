# DevOps Project Overview 🚀
This project showcases a CI/CD Pipeline using Jenkins for continuous deployment, Infrastructure as Code (IaC) via Terraform, and Kubernetes for container orchestration. It automates the deployment of a Flask application onto an AWS infrastructure, with the app hosted in Docker containers managed by Kubernetes.

# Key Components
Environment-specific variables defined in .tfvars files
Modularized Terraform structure: Compute, Network, and Security modules
Deployment of resources in AWS (us-east-1 region)
Provisioning and configuration using Ansible
Integration with Jenkins for automated CI/CD <br>

🚀 Getting Started <br>
Prerequisites 🛠️ <br>
Ensure you have the following installed:  <br>
AWS Development EC2  <br>
Terraform 🏗️  <br>
AWS CLI ☁️  <br>
Jenkins (Docker image recommended) 🐳  <br>
Python 🐍  <br>
Docker 🐋  <br>
Kubernetes CLI (kubectl) 🧩  <br>
Ansible 📜  <br>
AWS CLI Setup 🔑  <br>
<h2> 1. Setting Up AWS and DockerHub Credentials in Jenkins: </h2>
Go to Manage Jenkins → Credentials and add AWS Access Key and Secret Access Key and DockerHub Creditials.<br>

Open a terminal and enter:<br>
`aws configure`

Input your AWS Access Key ID, Secret Access Key, Region (e.g., us-east-1), and output format (JSON).
Obtaining AWS Access Keys 🔑
Log in to AWS, go to Security Credentials.
Under Access Keys, create a new key and add it during aws configure.

<h2> 2. Terraform Modules for Infrastructure Setup </h2>
Network Module: Sets up the VPC, subnets, Internet Gateway, and route tables.
Compute Module: Configures EC2 instances, including the App EC2 instance for the Flask app.
Security Module: Defines security groups and IAM roles.

Execute Terraform Files:
`terraform apply`

<h2> 3. Kubernetes for Containerized Deployment 🧩</h2>
Define Kubernetes manifests for namespace, deployment, and load balancer.
Use kubectl to deploy the manifests: 

`kubectl apply -f namespace.yaml` <br>
`kubectl apply -f deployment.yaml` <br>
`kubectl apply -f loadbalancer.yaml` <br>

<h2> 4. Ansible for Configuration Management 📜</h2>
Playbook: Installs Docker and Kubernetes on the App EC2 instance, ensuring the app environment is ready.
Run the Ansible playbook: <br>
`ansible-playbook -i inventory.txt dockerk8s.yml`

<h2> 5. Jenkins for CI/CD Integration 🤖</h2>
Pipeline Setup:
Clone GitHub repository <br>
Trigger Terraform to provision infrastructure <br>
Run Ansible playbook for configuration <br>
Deploy Flask app using Kubernetes <br>







