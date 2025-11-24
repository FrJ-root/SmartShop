# SmartShop Docker Setup

This document explains how to run the SmartShop application using Docker containers.

## Prerequisites

- Docker installed on your system
- Docker Compose installed on your system

## Running the Application

### Option 1: Using Docker Compose (Recommended)

1. Navigate to the project directory:
   ```bash
   cd /home/kali/IdeaProjects/SmartShop
   ```

2. Build and run the application:
   ```bash
   docker-compose up --build
   ```

3. The application will be available at `http://localhost:8080`

4. To run in detached mode (background):
   ```bash
   docker-compose up --build -d
   ```

5. To stop the application:
   ```bash
   docker-compose down
   ```

### Option 2: Using Docker Commands Directly

1. Build the Docker image:
   ```bash
   docker build -t smartshop-app .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 --name smartshop-container smartshop-app
   ```

3. To run in detached mode:
   ```bash
   docker run -d -p 8080:8080 --name smartshop-container smartshop-app
   ```

4. To stop the container:
   ```bash
   docker stop smartshop-container
   ```

5. To remove the container:
   ```bash
   docker rm smartshop-container
   ```

## Useful Docker Commands

- View running containers: `docker ps`
- View all containers: `docker ps -a`
- View application logs: `docker logs smartshop-container`
- Follow logs in real-time: `docker logs -f smartshop-container`
- Execute commands inside container: `docker exec -it smartshop-container /bin/bash`

## Project Structure

```
SmartShop/
├── Dockerfile              # Docker image configuration
├── docker-compose.yml      # Docker Compose configuration
├── .dockerignore           # Files to ignore during build
├── pom.xml                 # Maven configuration
├── src/                    # Source code
└── README-Docker.md        # This file
```

## Configuration

The application is configured to:
- Use OpenJDK 17
- Run on port 8080
- Use 512MB max heap size (-Xmx512m)
- Use 256MB initial heap size (-Xms256m)

You can modify these settings in the `docker-compose.yml` file under the `environment` section.