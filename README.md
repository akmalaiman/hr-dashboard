# HR - DASHBOARD GUIDELINE

___

## Introduction

This document is a guideline for the HR Dashboard project. The purpose of this project is to create a dashboard that
will help the HR department and management to monitor the performance of the employees and the company. The dashboard
will provide a summary of the staff performance, the company's performance, and the overall health of the organization.

___

## Project Structure

The project is divided into the following sections:

- hr-dashboard-api: This is the backend API for the HR Dashboard. It is built using Java Spring Boot and provides
  endpoints for fetching data from the database.
- hr-dashboard-ui: This is the frontend UI for the HR Dashboard. It is built using Angular and provides a user interface
  for interacting with the data.

___

## Technologies Used

The following technologies are used in this project:

### Frontend

- Framework: Angular 19
- Styling: Bootstrap 5, ng-bootstrap
- Package Manager: npm

### Backend

- Framework: Java Spring Boot
- Language: Java JDK 21
- Database: PostgreSQL
- Database Management: Liquibase
- Build Tool: Maven
- API Documentation: Swagger
- Security: Spring security, JWT
- Testing: JUnit, Mockito
- Logging: Log4j

### Deployment

- Containerization: Docker
- Web Server: Nginx

___

## Setup and Installation

### Prerequisites

- Docker and Docker Compose
- Java JDK 21
- NodeJS V22
- Environment variables in .env file

### Steps to Run the Project

```shell
docker compose build
docker compose up -d
```

### Steps to Stop the Project

```shell
docker compose down
```

### Accessing the Application

- [Frontend UI](http://localhost:4200)
- [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

___

## Development Guidelines

### Coding Standards

- Use meaningful variable and function names
- Use comments to explain complex logic
- Use proper indentation and formatting
- Use linters and formatters to maintain code quality
- Write unit tests for all the code

### Git Workflow

- Use feature branches for development
- Create a pull request for code review
- Squash commits before merging
- Use a rebase workflow to keep the commit history clean
- Use meaningful commit messages

___

## Conclusion

This document provides a guideline for the HR Dashboard project. It covers the project structure, technologies used,
setup and installation, development guidelines, and documentation. By following these guidelines, the project team can
ensure the successful development and deployment of the HR Dashboard.