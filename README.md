# Digital Voting System

**STUDENT OOP PROJECT — DEMO / EDUCATIONAL USE ONLY**

A simple Java Swing + JDBC + SQLite implementation of the Digital Voting System described in the project specification.

## Technologies
- Java 8+
- Swing
- JDBC
- SQLite
- SHA-256
- Maven

## Features
- Admin, Candidate and Voter login
- SHA-256 password hashing
- Candidate management
- Voter management
- One vote per voter
- SQLite database
- Transaction and rollback while voting
- Vote counting and ranking
- Simple audit log
- Swing GUI

## Demo accounts
| Role | Username | Password |
|---|---|---|
| Admin | admin | admin123 |
| Candidate | candidate | candidate123 |
| Voter | voter | voter123 |

The database file `voting.db` is created automatically on first run.

## Run
Install Java and Maven, then run:

```bash
mvn compile exec:java
```

This is a college project implementation and is **not intended for real elections**.
