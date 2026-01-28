#!/bin/bash

# Rollback script for FSocial microservices
# Usage: ./rollback.sh [backup_file]

set -e

DEPLOY_PATH=${1:-/home/ec2-user/fsocial}
BACKUP_FILE=${2:-}

cd ${DEPLOY_PATH}

echo "🔄 FSocial Rollback Script"
echo "==========================="

# Find latest backup if not specified
if [ -z "${BACKUP_FILE}" ]; then
    BACKUP_FILE=$(ls -t docker-compose.yml.backup.* 2>/dev/null | head -1)
fi

if [ -z "${BACKUP_FILE}" ] || [ ! -f "${BACKUP_FILE}" ]; then
    echo "❌ Error: No backup file found"
    echo "Available backups:"
    ls -la docker-compose.yml.backup.* 2>/dev/null || echo "  (none)"
    exit 1
fi

echo "📋 Rolling back to: ${BACKUP_FILE}"
echo ""

# Restore backup
cp ${BACKUP_FILE} docker-compose.yml
echo "✅ Restored docker-compose.yml from backup"

# Pull images from backup version (if version is in filename)
VERSION=$(echo ${BACKUP_FILE} | grep -oP '\d+\.\d+\.\d+' || echo "latest")
echo "📥 Pulling images (version: ${VERSION})..."

docker-compose pull

# Restart services
echo ""
echo "🔄 Restarting services..."
docker-compose up -d

# Wait for services
echo ""
echo "⏳ Waiting for services to start..."
sleep 15

# Health check
echo ""
echo "🏥 Running health checks..."
SERVICES=("accountservice" "messageservice" "notificationservice" "postservice" "profileservice" "apigateway" "relationshipservice" "timelineservice")

for svc in "${SERVICES[@]}"; do
    if docker-compose ps ${svc} | grep -q "Up"; then
        echo "✅ ${svc} is running"
    else
        echo "⚠️  ${svc} is not running"
    fi
done

echo ""
echo "📊 Service status:"
docker-compose ps

echo ""
echo "✅ Rollback completed!"

