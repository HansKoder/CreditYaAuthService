#!/bin/bash
set -e  # if fail finish

APP_NAME="creditya-auth-service"
APP_VERSION="0.1-snapshot"
JAR_NAME="${APP_NAME}.jar"
DOCKERFILE_PATH="Dockerfile ."
JAR_SOURCE="./applications/app-service/build/libs"
TARGET_DIR="./deployment"

echo ">>> 1) Cleaning and building the project"
./gradlew clean build -x test

echo ">>> 2) Copying jar built in the path from Dockerfile..."
cp ${JAR_SOURCE}/*.jar ${TARGET_DIR}/${JAR_NAME}

cd ${TARGET_DIR}

echo ">>> 3) Deleting image (If exits)..."
if docker images | grep -q "${APP_NAME}"; then
  echo ">>> 3.1) Deleting ${APP_NAME}:${APP_VERSION}..."
  docker rmi -f ${APP_NAME}:${APP_VERSION} || true
fi

echo ">>> 4) Building new image..."
docker build -t ${APP_NAME}:${APP_VERSION} .

echo ">>> 5) Removing ${JAR_NAME}..."
rm -rf ${JAR_NAME}

echo ">>> ✅ Image ${APP_NAME}:${APP_VERSION} was created with successful."
