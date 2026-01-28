#!/bin/bash

# Health check script for FSocial microservices
# Usage: ./health-check.sh [service]

set -e

SERVICE=${1:-all}
DEPLOY_PATH=${2:-/home/ec2-user/fsocial}

cd ${DEPLOY_PATH}

echo "🏥 FSocial Health Check"
echo "======================="
echo ""

# Service ports mapping
declare -A SERVICE_PORTS=(
    ["accountservice"]="8081"
    ["messageservice"]="8082"
    ["postservice"]="8083"
    ["timelineservice"]="8084"
    ["profileservice"]="8085"
    ["relationshipservice"]="8086"
    ["notificationservice"]="8087"
    ["apigateway"]="8888"
)

# Health check function
check_service() {
    local service=$1
    local port=${SERVICE_PORTS[$service]}
    
    echo "Checking ${service}..."
    
    # Check container status
    if ! docker-compose ps ${service} | grep -q "Up"; then
        echo "  ❌ Container is not running"
        return 1
    fi
    
    echo "  ✅ Container is running"
    
    # Check port if available
    if [ -n "${port}" ]; then
        if curl -f -s http://localhost:${port}/actuator/health > /dev/null 2>&1; then
            echo "  ✅ Health endpoint (/actuator/health) is responding"
        elif curl -f -s http://localhost:${port}/health > /dev/null 2>&1; then
            echo "  ✅ Health endpoint (/health) is responding"
        elif curl -f -s http://localhost:${port}/ > /dev/null 2>&1; then
            echo "  ✅ Service is responding on port ${port}"
        else
            echo "  ⚠️  Service is running but not responding on port ${port}"
        fi
    fi
    
    # Check logs for errors
    local error_count=$(docker-compose logs --tail=100 ${service} 2>&1 | grep -i "error\|exception\|failed" | wc -l)
    if [ ${error_count} -gt 0 ]; then
        echo "  ⚠️  Found ${error_count} error(s) in recent logs"
    else
        echo "  ✅ No errors in recent logs"
    fi
    
    echo ""
}

# Run health checks
if [ "${SERVICE}" == "all" ]; then
    SERVICES=("accountservice" "messageservice" "notificationservice" "postservice" "profileservice" "apigateway" "relationshipservice" "timelineservice")
    
    for svc in "${SERVICES[@]}"; do
        check_service ${svc}
    done
else
    check_service ${SERVICE}
fi

# Overall status
echo "📊 Overall Status:"
docker-compose ps

echo ""
echo "💾 Resource Usage:"
docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}"

