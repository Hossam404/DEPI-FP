resource "aws_instance" "appdep" {
  ami               = var.ami_id
  instance_type     = var.instance_type
  security_groups   = [var.public_SG]
  subnet_id         = var.public-subnet
  associate_public_ip_address = true
  key_name          = aws_key_pair.tf-key-pairz.id


  user_data = <<-EOF
    #!/bin/bash
    echo '${tls_private_key.rsa-key.private_key_pem}' > /home/ec2-user/private-key.pem
    chmod 400 /home/ec2-user/private-key.pem
  EOF

  provisioner "local-exec" {
    command = <<-EOC
      echo "[all]" > inventory.txt
      echo "${self.public_ip} ansible_user=ec2-user ansible_ssh_private_key_file=/home/ec2-user/private-key.pem" >> inventory.txt
    EOC
  }

  tags = {
    Name = "App EC2"
  }
}
