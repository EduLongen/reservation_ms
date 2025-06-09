@echo off
chcp 65001 >nul
cls

echo 🚀 Starting Room Reservation System - Microservices
echo ========================================================

echo.
echo 📋 System Architecture:
echo   ├── 🌐 API Gateway (localhost:8080) - Provides Web Interface
echo   ├── 👥 User Service (localhost:8081)
echo   ├── 🏢 Room Service (localhost:8082)
echo   ├── 📅 Reservation Service (localhost:8083)
echo   ├── 🐘 PostgreSQL (localhost:5432)
echo   └── 📨 Kafka (localhost:9092)

echo.
echo 🔧 Checking dependencies...

REM Check if Docker is installed
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker is not installed. Please install Docker first.
    pause
    exit /b 1
)

REM Check if Docker Compose is installed
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker Compose is not installed. Please install Docker Compose first.
    pause
    exit /b 1
)

echo ✅ Dependencies successfully verified!

echo.
echo 🛠️ Building and starting containers...
echo This may take a few minutes on first run...

REM Stop existing containers
echo 🔄 Stopping existing containers...
docker-compose down

REM Build and start
echo 🏗️ Building and starting containers...
docker-compose up --build -d

echo.
echo ⏳ Waiting for services to become available...
timeout /t 30 /nobreak >nul

echo.
echo 🎉 System started successfully!
echo.
echo 📍 Access URLs:
echo   🌐 Web Interface: http://localhost:8080 (redirects to /reservations)
echo.
echo 📍 Direct APIs:
echo   👥 Users: http://localhost:8081/api/users
echo   🏢 Rooms: http://localhost:8082/api/rooms
echo   📅 Reservations: http://localhost:8083/api/reservations
echo.
echo 📖 To stop the system, run: docker-compose down
echo 📊 To view logs, run: docker-compose logs -f
echo.
echo Happy coding! 🚀
echo.
pause 