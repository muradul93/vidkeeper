# VidKeeper

VidKeeper is a video management application built with Java, Spring Boot, and React.

## Prerequisites

- Java 11 or higher
- Maven
- Node.js
- npm
- Docker
- Docker Compose

## Getting Started

### Backend

1. **Clone the repository:**

    ```sh
    git clone https://github.com/muradul93/vidkeeper.git
    cd vidkeeper
    ```

2. **Navigate to the backend directory:**

    ```sh
    cd backend
    ```

3. **Build the project:**

    ```sh
    mvn clean install
    ```

4. **Run the application:**

    ```sh
    mvn spring-boot:run
    ```

   The backend server will start on `http://localhost:8080`.

### Frontend

1. **Navigate to the frontend directory:**

    ```sh
    cd frontend
    ```

2. **Install dependencies:**

    ```sh
    npm install
    ```

3. **Run the application:**

    ```sh
    npm start
    ```

   The frontend application will start on `http://localhost:3000`.

### Docker

1. **Build and run the containers:**

    ```sh
    docker-compose up --build
    ```

   The backend server will start on `http://localhost:8080` and the frontend application will start on `http://localhost:3000`.

2. **Stop the containers:**

    ```sh
    docker-compose down
    ```

## Running Tests

### Backend Tests

To run backend tests, navigate to the backend directory and use Maven:

```sh
cd backend
mvn test
