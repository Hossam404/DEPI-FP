resource "aws_vpc" "my-vpc" {
    
  cidr_block = var.cidr_block
  

  tags = {
    Name = "my-vpc"
  }
}

