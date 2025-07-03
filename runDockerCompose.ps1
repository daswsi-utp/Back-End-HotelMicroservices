./mvnw clean package -DskipTests
docker-compose up --build
#If powershell doesn't let you execute it you write in the terminal:
#Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
