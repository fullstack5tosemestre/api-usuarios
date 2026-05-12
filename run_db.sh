docker run -d \
    --name db_usuario \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=db_usuario \
    -p 3308:3306 \
    mysql:8.0
