docker run -d \
    --rm \
    -p 8082:8082 \
    --env-file .env \
    xdainz/api-usuario:latest
