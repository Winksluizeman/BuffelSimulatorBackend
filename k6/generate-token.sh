#!/bin/bash
# Haal een token op via de login endpoint
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpassword"}' \
  | grep -o '"token":"[^"]*"' \
  | cut -d'"' -f4)

echo "JWT_TOKEN=$TOKEN" >> $GITHUB_ENV
echo "Token opgehaald: $TOKEN"