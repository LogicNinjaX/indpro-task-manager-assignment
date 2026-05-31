# Task Manager App

A full-stack **Task Manager Application** built as part of an internship assignment.

The application allows users to register/login and manage tasks across three stages:

* **Todo**
* **In Progress**
* **Done**

Built with a clean responsive frontend using **HTML, CSS, and JavaScript**, and a secure backend REST API using **Java + Spring Boot** with **JWT Authentication**.

---

## Live Demo

### Frontend

[Frontend Live Link](https://task-manager-app-wyua.onrender.com)

### Backend API

[Backend Live Link](https://task-manager-api-d71w.onrender.com)

---

## GitHub Repository

[Repository Link](YOUR_GITHUB_REPO_LINK)

---

## Tech Stack

### Frontend

* HTML5
* CSS3
* Vanilla JavaScript
* Fetch API

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* MySQL
* JWT Authentication
* MapStruct
* Maven

---

## Features

### Authentication

* User Registration
* User Login
* JWT-based Authentication
* Protected API routes

### Task Management

* Create Task
* View All Tasks
* Update Task
* Delete Task
* Task Stage Management:

    * Todo
    * In Progress
    * Done

### UI/UX

* Clean responsive design
* Loading states
* Error handling
* Toast notifications
* User-friendly task workflow

---

## API Endpoints

### Authentication

#### Register

`POST /api/v1/auth/register`

#### Login

`POST /api/v1/auth/login`

---

### Tasks

#### Create Task

`POST /api/v1/tasks`

#### Get All Tasks

`GET /api/v1/tasks`

#### Update Task

`PATCH /api/v1/tasks/{taskId}`

#### Delete Task

`DELETE /api/v1/tasks/{taskId}`

---

## Project Structure

### Frontend

```bash
frontend/
│── index.html
│── login.html
│── register.html
│── dashboard.html
│── css/
│── js/
```

### Backend

```bash
src/main/java/
├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
├── security
├── exception
└── util
```

---

## Database Schema

### User Entity

* userId
* username
* email
* password
* createdAt
* updatedAt

### Task Entity

* taskId
* taskTitle
* description
* stage
* createdBy
* createdAt
* updatedAt

---

## Task Stages

The application supports three task states:

* **TODO**
* **IN_PROGRESS**
* **DONE**

Default stage when creating a task: **TODO**

---

## Security Implementation

Implemented secure authentication using:

* Spring Security
* JWT Token Generation
* Authentication Manager
* Password Encryption
* Protected task APIs

---

## Environment Variables

Backend requires the following environment variables:

```env
DB_URL=
DB_UNAME=
DB_PASS=

JWT_SECRET=
JWT_EXP=
```

---

## Run Locally

### Backend

Clone the repository

```bash
git clone YOUR_REPO_LINK
```

Navigate to backend

```bash
cd backend
```

Run application

```bash
./mvnw spring-boot:run
```

---

### Frontend

Simply open:

```bash
index.html
```

Or serve locally using Live Server.

---

## Technical Decisions

### Why Spring Boot?

Chosen for:

* Rapid REST API development
* Built-in security support
* Easy JPA integration
* Scalable architecture

### Why JWT?

Used for:

* Stateless authentication
* Secure API communication
* Better frontend-backend separation

### Why Vanilla JavaScript?

Chosen to keep implementation lightweight and aligned with assignment time constraints.

---

## Assumptions

* Each task title must be unique per user.
* Users can only manage their own tasks.
* Newly created tasks are assigned **TODO** stage.
* Frontend stores JWT securely for authenticated requests.

---

## Tradeoffs

### Chosen Simplicity Over Complexity

To complete within the assignment timeline:

* Used vanilla JS instead of a frontend framework
* Used `ddl-auto: update` for faster development
* Focused on core CRUD flow instead of advanced filtering/search

---

## Bonus Implementations

Implemented bonus backend features:

* Custom REST APIs
* MySQL Database Integration
* JWT Authentication
* Layered Backend Architecture
* DTO Mapping using MapStruct

---

## Future Improvements

* Drag-and-drop task movement
* Task filtering and search
* Pagination improvements
* Refresh token support
* Role-based authorization
* Docker deployment

---

## Assignment Submission Notes

This project was built as a **small, complete, and production-structured implementation**, prioritizing clean architecture, secure authentication, and fully working task management flow over unnecessary feature complexity.

---

## Author

**Nitish Sahni**

Backend Developer | Java | Spring Boot | REST APIs
