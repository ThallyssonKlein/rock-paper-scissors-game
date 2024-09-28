# About the project

## Hexagonal Architecture

The project was built using hexagonal architecture. You can read the article about the creation of this architectural pattern at this link: https://alistair.cockburn.us/hexagonal-architecture/
The idea is to ensure that nothing communicates with the domain layer without going through an adapter layer. This way, we force ourselves to keep the business logic separate from presentation and data access.

## Move Verification Mechanism

A mechanism has been implemented in this project where the user receives a hash, and then receives the server's move and the salt. With the salt, they can decrypt the hash and know that the result was fair and the server's move was generated before the server had access to the user's move. Perhaps in the frontend, this could be a question mark symbol that instructs the user to check the results (for the more suspicious users).