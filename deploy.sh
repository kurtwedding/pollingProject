MAGENTA='\033[0;35m'
NC='\033[0m'

printf "[$MAGENTA$(date +"%T")$NC] Launching/Updating cloud infrastructure\n"
(cd infra && terraform apply -auto-approve)

printf "\n[$MAGENTA$(date +"%T")$NC] Fetching required outputs from Terraform\n"
EC2_IP=$(cd infra && terraform output -raw instance_public_ip)
RDS_ENDPOINT=$(cd infra && terraform output -raw rds_endpoint)
RDS_DB_SECRET_ID=$(cd infra && terraform output -raw rds_db_secret_id)

printf "[$MAGENTA$(date +"%T")$NC] Fetching secrets from AWS\n"
DB_SECRET=$(aws secretsmanager get-secret-value \
    --secret-id "${RDS_DB_SECRET_ID}" \
    --region ap-southeast-2 \
    --query SecretString \
    --output text)
DB_PASSWORD=$(echo "$DB_SECRET" | jq -r '.password')
DB_USERNAME=$(echo "$DB_SECRET" | jq -r '.username')
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

printf "[$MAGENTA$(date +"%T")$NC] Writing .env.prod file\n"
cat > .env.prod << EOF
# This data is all created in deploy.sh - with values pulled from AWS secretsmanager
# DO NOT PUSH THIS TO THE REPO

GTFS_API_KEY=${GTFS_API_KEY}
GTFS_API_URL=${GTFS_ENDPOINT}

SPRING_DATASOURCE_URL=jdbc:postgresql://${RDS_ENDPOINT}/pollingdb
SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
POLLINGPROJECT_API_KEY=${EXTERNAL_API_KEY}
EOF

printf "[$MAGENTA$(date +"%T")$NC] Compiling Spring Boot project\n"
mvn clean package -DskipTests

printf "[$MAGENTA$(date +"%T")$NC] Uploading .env file to the EC2 instance\n"
scp -i .ssh/macos-sshkey.pem .env.prod ubuntu@$EC2_IP:~/.env

printf "[$MAGENTA$(date +"%T")$NC] Copying jar file to remote server\n"
scp -i .ssh/macos-sshkey.pem target/pollingProject-0.0.1-SNAPSHOT.jar ubuntu@$EC2_IP:~/

printf "[$MAGENTA$(date +"%T")$NC] Launching app on EC2 instance\n"
ssh -i .ssh/macos-sshkey.pem ubuntu@$EC2_IP "nohup java -jar ~/pollingProject-0.0.1-SNAPSHOT.jar > app.log 2>&1 &"

printf "[$MAGENTA$(date +"%T")$NC] Deployment completed"