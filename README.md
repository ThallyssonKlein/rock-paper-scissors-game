# About the project

## Hexagonal Architecture

The project was built using hexagonal architecture. You can read the article about the creation of this architectural pattern at this link: https://alistair.cockburn.us/hexagonal-architecture/
The idea is to ensure that nothing communicates with the domain layer without going through an adapter layer. This way, we force ourselves to keep the business logic separate from presentation and data access.

## Move Verification Mechanism

A mechanism has been implemented in this project where the user receives a hash, and then receives the server's move and the salt. With the salt, they can decrypt the hash and know that the result was fair and the server's move was generated before the server had access to the user's move. Perhaps in the frontend, this could be a question mark symbol that instructs the user to check the results (for the more suspicious users).

## How to run

```
sudo docker-compose up -d
```

## How to use

## Starting a game

You need to be authenticated. You can use this test user created by default:

![capture1](docs/capture1.png)

```
curl --request POST \
  --url http://localhost:8080/api/v1/game/start \
  --header 'Authorization: Basic dGVzdF9wbGF5ZXI6c2VjdXJlX3Bhc3N3b3Jk' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/9.3.3' \
  --cookie JSESSIONID=8DF9D199326BCBE6F3C96914CD384AA6
```

## Getting the server movement

The frontend must always search for the next move from the server for each move, so that when the user goes to play, they already has the move from the server.

The game needs to be open so you can get the server movement. You need to be authenticated. You need to be the owner of the game.

You will receive the hash, so you can check the server movement later.

![capture3](docs/capture3.png)

```
curl --request GET \
  --url http://localhost:8080/api/v1/game/3/next_server_move \
  --header 'Authorization: Basic dGVzdF9wbGF5ZXI6c2VjdXJlX3Bhc3N3b3Jk' \
  --header 'User-Agent: insomnia/10.0.0' \
  --cookie JSESSIONID=8DF9D199326BCBE6F3C96914CD384AA6
```

# Doing a movement

You need to be authenticated. You need to be the owner of the game. You need to inform one of the following values: ROCK, PAPER, SCISSORS. You need to get the server movement before.

![capture4](docs/capture4.png)

```
curl --request POST \
  --url http://localhost:8080/api/v1/game/3/move \
  --header 'Authorization: Basic dGVzdF9wbGF5ZXI6c2VjdXJlX3Bhc3N3b3Jk' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/10.0.0' \
  --cookie JSESSIONID=0449602A5737678E4436BBC4D87B626A \
  --data '{
	"playerMovement": "ROCK"
}'
```

## Ending a game

You need to be authenticated. You need to be the owner of the game.

![capture2](docs/capture2.png)

```
curl --request POST \
  --url http://localhost:8080/api/v1/game/1/end \
  --header 'Authorization: Basic dGVzdF9wbGF5ZXI6c2VjdXJlX3Bhc3N3b3Jk' \
  --header 'User-Agent: insomnia/10.0.0' \
  --cookie JSESSIONID=8DF9D199326BCBE6F3C96914CD384AA6
```

## Getting the statistics

You need to be authenticated. You need to be the owner of the statistics. You need to inform the user id in the path parameter.

![capture5](docs/capture5.png)

```
curl --request GET \
  --url http://localhost:8080/api/v1/statistics/2 \
  --header 'Authorization: Basic dGVzdF9wbGF5ZXI6c2VjdXJlX3Bhc3N3b3Jk' \
  --header 'User-Agent: insomnia/10.0.0' \
  --cookie JSESSIONID=0449602A5737678E4436BBC4D87B626A
```

## Verifing the hash

I added a script python in the repository, so you can verify the server movement, with the movement, the hash and the salt.

![capture6](docs/capture6.png)

# Observability

You can put your datadog api key on the docker-compose.yml to see the application metrics inside the Datadog.
