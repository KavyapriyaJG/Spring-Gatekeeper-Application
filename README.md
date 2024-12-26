# Gatekeeper Application API
 
This project is a Spring Boot-based REST API for managing visitors in an apartment complex. It enables residents, gatekeepers, and admin users to monitor and control visitor access efficiently.
 
## Features
 
### User Types
1. **Residents**: Schedule visitor entries, manage visitors, blacklist visitors.
2. **Gatekeepers**: Approve, reject, or blacklist visitors.
3. **Visitors**: Verify scheduled entry with an entry pass.
4. **Admin**: Manage residents and gatekeepers (CRUD operations), blacklist any user.
 
### Key APIs
- **User Registration API**:
  - Register residents, gatekeepers, visitors, and admin users.
  - Resident and Gatekeeper registrations require admin approval.
- **Login API**:
  - User authentication using JWT.
  - Includes token expiration for 3 hours and logout functionality.
- **Gatekeeper API**:
  - Admin: Add, modify, and delete residents and gatekeepers.
  - Resident: Schedule and manage visitors.
  - Gatekeeper: Approve/reject visitors and blacklist them.
- **Blacklist Management**:
  - Admin can blacklist any user.
  - Residents and gatekeepers can blacklist visitors.
 
## Tech Stack
- **Backend**: Java 19, Spring Boot 3.x
- **Database**: MongoDB (NoSQL)
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Maven
- **Testing**: JUnit, Mockito
- **Documentation**: Postman API
 
---
 
## Prerequisites
 
1. **Java**: Ensure you have JDK 19 installed.
2. **MongoDB**: Install MongoDB locally or use a cloud-hosted MongoDB instance.
3. **Maven**: Ensure Maven is installed and configured.
 
---
 
## Setup Instructions
 
### Clone the Repository
```bash
git clone https://github.com/KavyapriyaJG/Spring-Gatekeeper-Application.git
```

### Configure MongoDB
example:

```yml
spring:
  data:
    mongodb:
      uri: mongodb://<local-or-connectionstring>/housing_board
```

### Build the project
```bash
mvn clean install
```

### Run the project
```bash
mvn spring-boot:run
```