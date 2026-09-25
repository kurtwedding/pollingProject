output "instance_hostname" {
  description = "Private DNS name of the EC2 instance."
  value       = aws_instance.app_server.private_dns
}

output "instance_public_ip" {
  description = "Public IP address of the EC2 instance."
  value = aws_instance.app_server.public_ip
}

output "rds_endpoint" {
  description = "Endpoint of RDS server."
  value = module.db.db_instance_endpoint
  sensitive = true
}

output "rds_db_secret_id" {
  description = "The secret id of the aws managed database password"
  value = module.db.db_instance_master_user_secret_arn
  sensitive = true
}