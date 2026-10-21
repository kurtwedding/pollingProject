resource "aws_security_group" "app_sg" {
    vpc_id = module.vpc.vpc_id

    ingress {
        description = "Allow SSH from VPC and specified IPs"
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = [ "10.0.0.0/16", var.ssh_address ]
    }

    ingress {
        description = "Allow http traffic for Spring Boot"
        from_port = 8080
        to_port = 8080
        protocol = "tcp"
        cidr_blocks = [ "0.0.0.0/0" ]
    }

    egress {
        description = "Allow all outbound traffic"
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = [ "0.0.0.0/0" ]
    }
}

resource "aws_security_group" "db_sg" {
    vpc_id = module.vpc.vpc_id

    # Allow only PostgreSQL traffic from the app_sg security group
    ingress {
        description = "allow PostgreSQL from EC2"
        from_port = 5432
        to_port = 5432
        protocol = "tcp"
        security_groups = [ aws_security_group.app_sg.id ]
    }

    egress {
        description = "Allow all outbound traffic"
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = [ "0.0.0.0/0" ]
    }
}