docker stop $(docker ps -q)
docker rm $(docker ps -q -a)
docker rmi $(docker images -q)
docker volume rm $(docker volume ls -q)
docker builder prune -a
docker-compose up -d
