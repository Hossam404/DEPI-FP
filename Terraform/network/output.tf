output "public-subnet" {
  value = aws_subnet.public-subnet.id
}


output "vpc-id" {
  value = aws_vpc.my-vpc.id
}

