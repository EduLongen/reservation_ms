#!/bin/bash

echo "🚀 Starting Room Reservation System - Microservices"
echo "========================================================"

echo ""
echo "📋 System Architecture:"
echo "  ├── 🌐 API Gateway (localhost:8080) - Provides Web Interface"
echo "  ├── 👥 User Service (localhost:8081)"
echo "  ├── 🏢 Room Service (localhost:8082)"
echo "  ├── 📅 Reservation Service (localhost:8083)"
echo "  ├── 🐘 PostgreSQL (localhost:5432)"
echo "  └── 📨 Kafka (localhost:9092)"

echo ""
echo "🔧 Checking dependencies..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Define Docker Compose command
if command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE="docker-compose"
elif docker compose version &> /dev/null; then
    DOCKER_COMPOSE="docker compose"
else
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "✅ Dependencies successfully verified!"

echo ""
echo "🛠️ Building and starting containers..."
echo "This may take a few minutes on first run..."

# Stop existing containers
echo "🔄 Stopping existing containers..."
$DOCKER_COMPOSE down

# Build and start
echo "🏗️ Building and starting containers..."
$DOCKER_COMPOSE up --build -d

echo ""
echo "⏳ Waiting for services to become available..."
sleep 30

echo ""
echo "🔍 Checking service status..."

# Function to check if a service is running
check_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1

    while [ $attempt -le $max_attempts ]; do
        if curl -s "http://localhost:$port" > /dev/null 2>&1; then
            echo "✅ $service_name is running (localhost:$port)"
            return 0
        fi
        sleep 2
        attempt=$((attempt + 1))
    done
    echo "❌ $service_name is not responding (localhost:$port)"
    return 1
}

# Check services
check_service "API Gateway" 8080
check_service "User Service" 8081
check_service "Room Service" 8082
check_service "Reservation Service" 8083

echo ""
echo "🎉 System started successfully!"
echo ""
echo "📍 Access URLs:"
echo "  🌐 Web Interface: http://localhost:8080 (redirects to /reservations)"
echo ""
echo "📍 Direct APIs:"
echo "  👥 Users: http://localhost:8081/api/users"
echo "  🏢 Rooms: http://localhost:8082/api/rooms"
echo "  📅 Reservations: http://localhost:8083/api/reservations"
echo ""
echo "📖 To stop the system, run: $DOCKER_COMPOSE down"
echo "📊 To view logs, run: $DOCKER_COMPOSE logs -f"
echo ""
echo "Happy coding! 🚀" 