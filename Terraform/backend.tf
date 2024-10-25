terraform {
  backend "s3" {
    bucket  = "my-s3-bkt-tf"
    key     = "terraform.tfstate"
    region  = "us-east-1"
    encrypt = true
  }
}