#!/bin/bash

# Setup script for EC2 instance
# Run this script on your EC2 instance to prepare it for deployment

set -e

echo "🚀 FSocial EC2 Setup Script"
echo "==========================="
echo ""

# Detect OS
if [ -f /etc/os-release ]; then
    . /etc/os-release
    OS=$ID
else
    echo "❌ Cannot detect OS"
    exit 1
fi

echo "Detected OS: ${OS}"
echo ""

# Update system
echo "📦 Updating system packages..."
if [ "$OS" == "ubuntu" ] || [ "$OS" == "debian" ]; then
    sudo apt update && sudo apt upgrade -y
elif [ "$OS" == "amzn" ] || [ "$OS" == "rhel" ] || [ "$OS" == "centos" ]; then
    sudo yum update -y
else
    echo "⚠️  Unknown OS, skipping system update"
fi

# Install Docker
echo ""
echo "🐳 Installing Docker..."
if [ "$OS" == "ubuntu" ] || [ "$OS" == "debian" ]; then
    sudo apt install -y docker.io
    sudo systemctl start docker
    sudo systemctl enable docker
elif [ "$OS" == "amzn" ] || [ "$OS" == "rhel" ] || [ "$OS" == "centos" ]; then
    sudo yum install -y docker
    sudo service docker start
    sudo systemctl enable docker
fi

# Add user to docker group
echo ""
echo "👤 Adding user to docker group..."
sudo usermod -a -G docker $USER

# Install Docker Compose
echo ""
echo "📦 Installing Docker Compose..."
DOCKER_COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep 'tag_name' | cut -d\" -f4)
sudo curl -L "https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Git
echo ""
echo "📥 Installing Git..."
if [ "$OS" == "ubuntu" ] || [ "$OS" == "debian" ]; then
    sudo apt install -y git
elif [ "$OS" == "amzn" ] || [ "$OS" == "rhel" ] || [ "$OS" == "centos" ]; then
    sudo yum install -y git
fi

# Install curl
echo ""
echo "📡 Installing curl..."
if [ "$OS" == "ubuntu" ] || [ "$OS" == "debian" ]; then
    sudo apt install -y curl
elif [ "$OS" == "amzn" ] || [ "$OS" == "rhel" ] || [ "$OS" == "centos" ]; then
    sudo yum install -y curl
fi

# Verify installations
echo ""
echo "✅ Verifying installations..."
echo "Docker version:"
docker --version

echo ""
echo "Docker Compose version:"
docker-compose --version

echo ""
echo "Git version:"
git --version

echo ""
echo "✅ Setup completed!"
echo ""
echo "⚠️  IMPORTANT: Please logout and login again for docker group changes to take effect"
echo ""
echo "Next steps:"
echo "1. Logout and login again"
echo "2. Clone your repository: git clone <repo-url> fsocial"
echo "3. Copy docker-compose.prod.yml to docker-compose.yml"
echo "4. Create .env file with all environment variables"
echo "5. Setup SSH key for GitHub Actions"

