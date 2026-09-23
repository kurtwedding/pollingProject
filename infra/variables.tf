variable "instance_name" {
  description = "Value of the EC2 instance's Name tag."
  type        = string
  default     = "terraform-test"
}

variable "instance_type" {
  description = "The EC2 instance's type."
  type        = string
  default     = "t3.micro"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "ssh_address" {
    description = "Allowed IP to ssh into EC2 instance from"
    type = string
    sensitive = true
}