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

## Installation

### 1. Install Java

The project requires Java 8 or newer. Java 17 is recommended.

#### Ubuntu / Debian Linux

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

Check the installation:

```bash
java -version
```

#### Windows

Install a Java JDK 8 or newer and make sure Java is available in your system PATH.

Check in PowerShell or Command Prompt:

```powershell
java -version
```

### 2. Install Maven

Maven is used to compile and start the application.

#### Ubuntu / Debian Linux

```bash
sudo apt update
sudo apt install maven
```

Check:

```bash
mvn -version
```

#### Windows

Install Apache Maven and add Maven's `bin` directory to your PATH.

Check:

```powershell
mvn -version
```

## Download / Clone the Project

Clone the repository:

```bash
git clone https://github.com/SKN5/DigitalVotingSystem.git
cd DigitalVotingSystem
```

If you already downloaded the repository as a ZIP file, extract it and open a terminal inside the extracted `DigitalVotingSystem` folder.

## Run the Application

From the project root directory, run:

```bash
mvn compile exec:java
```

Maven will download the SQLite JDBC dependency, compile the Java source files, and start the Swing application.

### Linux

```bash
cd ~/DigitalVotingSystem
mvn compile exec:java
```

If the project is stored somewhere else, replace the path with your actual project location.

### Windows PowerShell

For a project inside Documents:

```powershell
cd "$HOME\Documents\DigitalVotingSystem"
mvn compile exec:java
```

## Database

The application uses SQLite.

The database file:

```text
voting.db
```

is created automatically when the application starts for the first time.

The database contains the users, candidates, votes and audit log tables required by the application.

## Demo Login Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Candidate | `candidate` | `candidate123` |
| Voter | `voter` | `voter123` |

The same credentials are also available in [LOGIN_CREDENTIALS.md](LOGIN_CREDENTIALS.md).

## Basic Usage

1. Start the application with `mvn compile exec:java`.
2. Select the required role on the login screen.
3. Enter the corresponding username and password.
4. Use the dashboard for that role.
5. Voters can select a candidate and cast one vote.
6. Admins can manage candidates and voters and view results/audit logs.
7. Candidates can view the current voting results.

## Troubleshooting

### `mvn: command not found`

Install Maven.

Ubuntu / Debian:

```bash
sudo apt update
sudo apt install maven
```

### `java: command not found`

Install a Java JDK and make sure Java is available in PATH.

Ubuntu / Debian:

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### The GUI does not open on Linux

Make sure you are running the application inside a graphical desktop session. A Java Swing application will not display a window in a headless terminal/server session.

## Project Structure

```text
DigitalVotingSystem/
├── pom.xml
├── README.md
├── LOGIN_CREDENTIALS.md
└── src/
    └── main/
        └── java/
            └── voting/
                ├── Main.java
                ├── User.java
                ├── Candidate.java
                ├── Security.java
                ├── VotingSystem.java
                ├── LoginFrame.java
                ├── AdminFrame.java
                ├── CandidateFrame.java
                └── VoterFrame.java
```

This is a college project implementation and is **not intended for real elections**.
