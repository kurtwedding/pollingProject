provider "aws" {
  region = "ap-southeast-2"
}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*"]
  }

  # Using Canonical's official account to verify the ubuntu image
  owners = ["099720109477"] # Canonical
}

resource "aws_instance" "app_server" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type

  key_name = "gtfs-ssh-keypair"

  vpc_security_group_ids = [aws_security_group.app_sg.id]

  # Exposing to public subnet for SSH
  subnet_id = module.vpc.public_subnets[0]

  associate_public_ip_address = true

  # This will run when the instance is created, auto installing psql and jre
  user_data = <<-EOF
              #!/bin/bash
              sudo apt update
              sudo apt install openjdk-17-jre-headless postgresql-client -y
              EOF

  tags = {
    Name = var.instance_name
  }
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.19.0"

  name = "example-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["ap-southeast-2a", "ap-southeast-2b", "ap-southeast-2c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24"]
  public_subnets  = ["10.0.101.0/24"]

  enable_dns_hostnames    = true
}

module "db" {
  source = "terraform-aws-modules/rds/aws"
  version = ">= 6.0"

  identifier = "polling-app-db"

  engine = "postgres"
  engine_version = "14"
  family = "postgres14"
  instance_class = "db.${var.instance_type}"
  allocated_storage = 20

  db_name = "pollingdb"
  username = "dbadmin"
  manage_master_user_password = true

  port = "5432"
  create_db_subnet_group = true
  subnet_ids = module.vpc.private_subnets

  vpc_security_group_ids = [aws_security_group.db_sg.id]

  backup_retention_period = 0
  skip_final_snapshot = true
  deletion_protection = false
}
