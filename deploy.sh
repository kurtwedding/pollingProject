echo "Launching/Updating cloud infrastructure"
(cd infra && terraform apply -auto-approve)

echo "Fetching EC2 public IP address from Terraform"
EC2_IP=$(cd infra && terraform output -raw instance_public_ip)
RDS_ENDPOINT=$(cd infra && terraform output -raw rds_endpoint)

echo "Fetching secrets from AWS"
DB_PASSWORD=$(aws secretsmanager get-secret-value \
    --secret-id "pollingproject/db_password" \
    --region ap-southeast-2 \
    --query SecretString \
    --output text)
EXTERNAL_API_KEY=$(aws secretsmanager get-secret-value \
    --secret-id pollingproject/external_api_key \
    --region ap-southeast-2 \
    --query SecretString \
    --output text)
GTFS_API_KEY=$(aws secretsmanager get-secret-value \
    --secret-id pollingproject/gtfs_api_key \
    --region ap-southeast-2 \
    --query SecretString \
    --output text)
GTFS_ENDPOINT=$(aws secretsmanager get-secret-value \
    --secret-id pollingproject/gtfs_endpoint \
    --region ap-southeast-2 \
    --query SecretString \
    --output text)

echo "Writing .env.prod file"
cat > .env.prod << EOF
# This data is all created in deploy.sh - with values pulled from AWS secretsmanager

GTFS_API_KEY=${GTFS_API_KEY}
GTFS_API_URL=${GTFS_ENDPOINT}

SPRING_DATASOURCE_URL=jdbc:postgresql://${RDS_ENDPOINT}/pollingdb
SPRING_DATASOURCE_USERNAME=dbadmin
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
POLLINGPROJECT_API_KEY=${EXTERNAL_API_KEY}
EOF

echo "Compiling Spring Boot project"
mvn clean package -DskipTests

echo "Uploading .env file to the EC2 instance"
scp -i .ssh/macos-sshkey.pem .env.prod ubuntu@$EC2_IP:~/.env

echo "Copying jar file to remote server"
scp -i .ssh/macos-sshkey.pem target/pollingProject-0.0.1-SNAPSHOT.jar ubuntu@$EC2_IP:~/

echo "Launching app on EC2 instance"
ssh -i .ssh/macos-sshkey.pem ubuntu@$EC2_IP "nohup java -jar ~/pollingProject-0.0.1-SNAPSHOT.jar > app.log 2>&1 &"

echo "Deployment completed"