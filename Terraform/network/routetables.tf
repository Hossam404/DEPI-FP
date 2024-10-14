resource "aws_route_table" "my_route_public" {
  vpc_id = aws_vpc.my-vpc.id

}

resource "aws_route" "public_route" {
route_table_id = aws_route_table.my_route_public.id
destination_cidr_block = "0.0.0.0/0"
gateway_id = aws_internet_gateway.my_igw.id
  
}

resource "aws_route_table_association" "public_subnet_assoc" {
subnet_id = aws_subnet.public-subnet.id
route_table_id = aws_route_table.my_route_public.id

  
}

