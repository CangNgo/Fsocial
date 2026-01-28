#!/bin/bash

# Deployment script for FSocial microservices
# Usage: ./deploy.sh [version] [service]

set -e

VERSION=${1:-latest}
SERVICE=${2:-all}
DEPLOY_PATH=${3:-/home/ec2-user/fsocial}

echo "🚀 FSocial Deployment Script"
echo "=============================="
echo "Version: ${VERSION}"
echo "Service: ${SERVICE}"
echo "Path: ${DEPLOY_PATH}"
echo ""

cd ${DEPLOY_PATH}

# Check if docker-compose.yml exists
if [ ! -f docker-compose.yml ]; then
    echo "❌ Error: docker-compose.yml not found in ${DEPLOY_PATH}"
    exit 1
fi

# Backup current docker-compose.yml
if [ -f docker-compose.yml ]; then
    BACKUP_FILE="docker-compose.yml.backup.$(date +%Y%m%d_%H%M%S)"
    cp docker-compose.yml ${BACKUP_FILE}
    echo "✅ Backup created: ${BACKUP_FILE}"
fi

# Update image tags if version is specified
if [ "${VERSION}" != "latest" ]; then
    echo "📝 Updating image tags to version ${VERSION}..."
    
    # Update each service image tag
    SERVICES=("accountservice" "messageservice" "notificationservice" "postservice" "profileservice" "apigateway" "relationshipservice" "timelineservice")
    
    for svc in "${SERVICES[@]}"; do
        IMAGE_NAME="cangngo/fsocial-${svc}"
        sed -i "s|${IMAGE_NAME}:.*|${IMAGE_NAME}:${VERSION}|g" docker-compose.yml
    done
    
    echo "✅ Image tags updated"
fi

# Pull latest images
echo ""
echo "📥 Pulling Docker images..."
if [ "${SERVICE}" == "all" ]; then
    docker-compose pull
else
    docker-compose pull ${SERVICE}
fi

# Health check function
health_check() {
    local service=$1
    local max_attempts=30
    local attempt=1
    
    echo ""
    echo "🏥 Health checking ${service}..."
    
    # Try to get port from docker-compose
    local port=$(docker-compose port ${service} 2>/dev/null | cut -d: -f2 || echo "")
    
    if [ -z "${port}" ]; then
        echo "⚠️  Cannot determine port for ${service}, checking container status instead"
        # Check if container is running
        if docker-compose ps ${service} | grep -q "Up"; then
            echo "✅ ${service} container is running"
            return 0
        else
            echo "❌ ${service} container is not running"
            return 1
        fi
    fi
    
    # Try health check endpoints
    while [ ${attempt} -le ${max_attempts} ]; do
        if curl -f -s http://localhost:${port}/actuator/health > /dev/null 2>&1 || \
           curl -f -s http://localhost:${port}/health > /dev/null 2>&1 || \
           curl -f -s http://localhost:${port}/ > /dev/null 2>&1; then
            echo "✅ ${service} is healthy! (port ${port})"
            return 0
        fi
        echo "⏳ Attempt ${attempt}/${max_attempts} - waiting for ${service}..."
        sleep 5
        attempt=$((attempt + 1))
    done
    
    echo "❌ Health check failed for ${service} after ${max_attempts} attempts"
    return 1
}

# Deploy services
echo ""
echo "🔄 Deploying services..."
if [ "${SERVICE}" == "all" ]; then
    # Deploy all services
    docker-compose up -d --no-deps --remove-orphans
    
    echo ""
    echo "⏳ Waiting for services to start..."
    sleep 15
    
    # Health check all services
    echo ""
    echo "🏥 Running health checks..."
    SERVICES=("accountservice" "messageservice" "notificationservice" "postservice" "profileservice" "apigateway" "relationshipservice" "timelineservice")
    
    FAILED_SERVICES=()
    for svc in "${SERVICES[@]}"; do
        if ! health_check ${svc}; then
            FAILED_SERVICES+=(${svc})
        fi
    done
    
    if [ ${#FAILED_SERVICES[@]} -gt 0 ]; then
        echo ""
        echo "❌ Health check failed for: ${FAILED_SERVICES[*]}"
        echo "📋 Checking logs..."
        for svc in "${FAILED_SERVICES[@]}"; do
            echo ""
            echo "--- Logs for ${svc} ---"
            docker-compose logs --tail=50 ${svc}
        done
        exit 1
    fi
else
    # Deploy specific service
    docker-compose up -d --no-deps ${SERVICE}
    
    echo ""
    echo "⏳ Waiting for ${SERVICE} to start..."
    sleep 10
    
    if ! health_check ${SERVICE}; then
        echo ""
        echo "❌ Health check failed for ${SERVICE}"
        echo "📋 Checking logs..."
        docker-compose logs --tail=50 ${SERVICE}
        exit 1
    fi
fi

# Clean up old images
echo ""
echo "🧹 Cleaning up unused Docker images..."
docker image prune -f

# Show status
echo ""
echo "📊 Service status:"
docker-compose ps

echo ""
echo "✅ Deployment completed successfully!"
echo ""
echo "📋 Summary:"
echo "  - Version: ${VERSION}"
echo "  - Service: ${SERVICE}"
echo "  - Backup: ${BACKUP_FILE:-N/A}"

