echo "Launching/Updating cloud infrastructure"
(cd infra && terraform apply -auto-approve)

echo "Fetching EC2 public IP address from Terraform"
EC2_IP=$(cd infra && terraform output -raw instance_public_ip)

echo "Compiling spring boot project"
mvn clean package -DskipTests

echo "Copying jar file to remote server"
scp -i .ssh/macos-sshkey.pem target/pollingProject-0.0.1-SNAPSHOT.jar ubuntu@$EC2_IP

echo "Launching app on EC2 instance"
ssh -i .ssh/macos-sshkey.pem ubuntu@$EC2_IP "java -jar ~/pollingProject-0.0.1-SNAPSHOT.jar > app.log"

echo "Deployment completed"