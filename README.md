This project showcases the power of Automating Deployment Through Jenkins , Infrastructure as Code (IAC) through Terraform.

It orchestrates the deployment of a Virtual Private Cloud (VPC) with a public subnet , setting up internet gateway, and configuring public route table.

The project encompasses the following components:

Definition of environment-specific variables in .tfvars files.
Network resources separated into a reusable module.
Deployment of resources in us-east-1 region.
Local execution of a provisioner to print the public IP of a Bastion EC2 instance.
Integration with Jenkins for automated provisioning.
🚀 Getting Started
Prerequisites 🛠️
Before you begin, ensure you have the following installed:

Terraform 🏗️
AWS CLI ☁️
Jenkins (Docker image recommended) 🐳
Python 🐍
Docker 🐋
How To configure AWS credentials after installing the AWS CLI 🔑
Open a terminal window.
Run the following command:
aws configure
You will be prompted to enter the following information:
AWS Access Key ID: This is your AWS access key.
AWS Secret Access Key: This is your AWS secret access key.
Default region name: Enter your desired AWS region (eg. us-east-1).
Default output format: Default JSON.
How to Get AWS Access Key ID & AWS Secret Access Key 🔑
Log in to your AWS account.
Click on your account name in the top right corner.
Hover over "Security Credentials." 🔑
Find "Access keys" and click "Create Access key." 🔑
Copy and paste the AWS Access Key ID & AWS Secret Access Key into the terminal after running 'aws configure'.
---- 🌟 ----


Use the terraform apply command to apply configurations.
Definition of environment-specific variables in .tfvars files

Use the Variable Files with each enviroment, we execute the apply command by specifying the var-file for the command:

Example:

terraform apply -var-file=dev.tfvars #for the "dev" environment
Warning

You might encounter this problem now and in the future.

Warning

If you encounter issues switching between workspaces, import your key in the selected workspace: 🔑

terraform import aws_key_pair.tf-key-pairz tf-key-pairz
---- 🌟 ----

Network resources separated into a reusable module
Create a directory for the Terraform files responsible for network resources, such as VPCs, subnets, route tables, and internet gateways.

Inside the created directory, create two files: variables.tf and outputs.tf.

variables.tf: Define the input variables for your network module.
outputs.tf: Define any outputs you want to expose from the network module.
In the parent directory, create a network.tf file that references the module:
module "network" {

  source              = "./network"
  cidr_block          = var.cidr_block
  public_subnet_cidr  = var.public_subnet_cidr

}
Use the variables from the mynetwork module in your configuration:
the variables should be the same name as the variables in the outputs.tf
then we use the variables from the mynetwork module 
Then apply the configuration For each enviroment
terraform apply 
---- 🌟 ----

Deployment of resources in us-east-1 region.
Example: compute.tfvars:

  ami               = var.ami_id
  instance_type     = var.instance_type
  security_groups   = [var.public_SG]
  subnet_id         = var.public-subnet
  associate_public_ip_address = true
  key_name          = aws_key_pair.tf-key-pairz.id
  availability_zone = var.availability_zone 


Then apply the configuration For each enviroment
terraform apply 

Note
the ami_id you can find it by going to aws ec2 go to launch instance you will find on the right info about the machine and the ami id starting by ami- followed by unique id copy it and add it as your ami_id no need to create the instance you can find that the ami is diffrent for both region that is because you need to specify the region first and then get the ami-id for the selected region.

---- 🌟 ----

Local execution of a provisioner to print the public IP of a Bastion EC2 instance.
you will need to add a provisioner in the terraform file for creating the Bastion EC2 instance Example
 provisioner "local-exec" {
    command = "echo 'Bastion Public IP: ${self.public_ip}' > inventory.txt"
  }
Note

Make Sure to apply the configuration in the default workspace to get the inventory.txt file & also make sure that the generated ip is the same as the public ip in the Bastion EC2 instance on the AWS console.

---- 🌟 ----

Integration with Jenkins for automated provisioning. 🤖
Create a directory called jenkins-terraform and place a Dockerfile inside it.
This Dockerfile uses the official Jenkins base image, installs the necessary packages (curl and unzip), and then installs Terraform.
Build the Docker Image
 docker build -t jenkins-terraform:latest .
Run the Jenkins Container
 docker run -d -p 8080:8080 -p 50000:50000 --name my-jenkins jenkins-terraform:latest
Access the Container's Shell
docker exec -it my-jenkins /bin/bash
Access Jenkins Homepage Jenkins.

Retrieve the Jenkins Admin Password:

 cat /var/jenkins_home/secrets/initialAdminPassword
Enter the generated password from your terminal into your initial password on Jenkins and press "Install Suggested Plugins" and create the admin user.

Exit the Container : exit

inisde jenkins the first thing you need to do is to Configure AWS Credentials

Go to manage jenkins
Select Credentials
Press the global Hyperlink
Choose "Add Credentials"
Choose "Secret text" as the kind
in the first field enter your AWS Access Key ID and then name it
then create onther credential and repeat the same steps for the AWS Secret Access Key
Note

you can get your keys by finding the credentials file

 ~/.aws/credentials on Linux or macOS
C:\Users\ USERNAME \.aws\credentials on Windows
Then Create a New Jenkins Pipeline Job:

Go to your Dashboard.
Click on "New Item" to create a new Jenkins job.
Enter a name for your job (e.g., "TerraformPipeline").
Choose "Pipeline" as the job type and click "OK."
Configure the Pipeline:

In the job configuration page:
Scroll down to the "Pipeline" section and choose the "Pipeline script" option.
Then Add the code in the pipeline.groovy file.
Then save the job.
Chooe build with paramaters
Choose the desired enviroment and then build
Check your AWS account to ensure all instances have been created successfully.
---- 🌟 ----