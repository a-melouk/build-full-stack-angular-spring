# Full-Stack Angular and Spring Boot Application

This project is a full-stack web application built with Angular for the frontend and Spring Boot for the backend. It provides a platform for users to register, log in, subscribe to topics, and engage in discussions by creating and commenting on posts.

## Features

-   **User Authentication:** Secure user registration and login using JWT.
-   **Topic Subscriptions:** Users can subscribe to various topics of interest.
-   **Post Creation and Discussion:** Create new posts within topics and comment on them.
-   **Profile Management:** Users can view and update their profile information.

## Prerequisites

Before you begin, ensure you have the following installed:

-   [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) (version 11 or higher)
-   [Maven](https://maven.apache.org/download.cgi)
-   [Node.js and npm](https://nodejs.org/en/download/)
-   [Angular CLI](https://angular.io/cli)
-   [MySQL](https://www.mysql.com/downloads/) (or any other compatible database)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/build-full-stack-angular-spring.git
cd build-full-stack-angular-spring
```

### 2. Backend Setup

The backend is a Spring Boot application that provides a RESTful API for the frontend.

#### Environment Variables

Create a `.env` file in the `back` directory and add the following environment variables:

```ini
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
```

Replace `your_database_username` and `your_database_password` with your actual database credentials.

#### Running the Backend

1.  Navigate to the `back` directory:
    ```bash
    cd back
    ```
2.  Build the application using Maven:
    ```bash
    mvn clean install
    ```
3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

The backend will start on `http://localhost:3001`.

### 3. Frontend Setup

The frontend is an Angular application that consumes the backend API.

#### Running the Frontend

1.  Navigate to the `front` directory:
    ```bash
    cd front
    ```
2.  Install the required dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    ng serve
    ```

The frontend will be available at `http://localhost:4200`.

## API Endpoints

The backend exposes the following RESTful endpoints:

-   **Authentication:**
    -   `POST /api/auth/register`: Register a new user.
    -   `POST /api/auth/login`: Log in an existing user.
-   **Topics:**
    -   `GET /api/topics`: Get a list of all topics.
    -   `GET /api/topics/{id}`: Get a specific topic by its ID.
-   **Posts:**
    -   `GET /api/posts`: Get all posts.
    -   `GET /api/posts/{id}`: Get a specific post by its ID.
    -   `POST /api/posts`: Create a new post.
-   **Comments:**
    -   `GET /api/posts/{postId}/comments`: Get all comments for a specific post.
    -   `POST /api/posts/{postId}/comments`: Add a new comment to a post.
-   **Subscriptions:**
    -   `POST /api/topics/{topicId}/subscribe`: Subscribe to a topic.
    -   `DELETE /api/topics/{topicId}/unsubscribe`: Unsubscribe from a topic.
-   **Profile:**
    -   `GET /api/me`: Get the current user's profile.
    -   `PUT /api/me`: Update the current user's profile.

## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue if you find a bug or have a suggestion for a new feature.