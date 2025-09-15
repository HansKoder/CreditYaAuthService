#!/bin/bash
set -e  # if fail finish

APP_NAME="creditya-auth-service"
APP_VERSION="0.1-snapshot"

echo ">>> 1) Deleting image (If exits)..."
if docker images | grep -q "${APP_NAME}"; then
  echo ">>> 1.1) Deleting ${APP_NAME}:${APP_VERSION}..."
  docker rmi -f ${APP_NAME}:${APP_VERSION} || true
fi

echo ">>> 2) Building new image..."
docker build -t ${APP_NAME}:${APP_VERSION} .

echo ">>> ✅ Image ${APP_NAME}:${APP_VERSION} was created with successful."

