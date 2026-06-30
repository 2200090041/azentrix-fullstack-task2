# Multi-User Task Management System (Mini Trello)

## Project Overview

The Multi-User Task Management System is a full-stack web application inspired by Trello, designed to help teams collaborate efficiently by organizing projects, assigning tasks, and tracking progress through an intuitive Kanban board.

The application provides secure user authentication, role-based access control, real-time collaboration using polling, task assignment, notifications, calendar integration, and project analytics, making it suitable for small teams and organizations.

This project was developed as part of the Azentrix Digital Services Full Stack Developer Internship Program.

## Features

### User Management

* User Registration
* User Login Authentication (Session-Based)
* User Logout
* Role-Based Access Control (Admin & Member)

### Board Management

* Create Project Boards
* View Multiple Boards
* Organize Projects into Workspaces

### Task Management

* Create Tasks
* Edit Tasks
* Delete Tasks
* Assign Tasks to Team Members
* Set Due Date
* Set Priority (High / Medium / Low)
* Task Description Support

### Kanban Workflow

* To Do Column
* In Progress Column
* Done Column
* Drag and Drop Task Movement using SortableJS
* In-App Notifications
* Email Notifications for Task Assignment and Completion

### Dashboard & Analytics

* Total Boards
* Pending Tasks
* Completed Tasks
* Interactive Charts using Chart.js

### Calendar

* View Task Deadlines using FullCalendar

### Activity Tracking

* Activity Log for Task Creation, Updates, and Completion

### Additional Features

* Responsive User Interface
* Secure Database Persistence using MySQL
* 
## Technology Stack

### Backend

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA

### Frontend

* HTML5
* CSS3
* Bootstrap 5
* JavaScript

### Database

* MySQL

## Architecture

The application follows the MVC (Model-View-Controller) architecture.

* Controller Layer handles incoming user requests.
* Service Layer contains the business logic.
* Repository Layer communicates with the database using Spring Data JPA.
* Entity Layer represents the database models.
* Thymeleaf templates render the user interface.

## Setup Instructions

### Prerequisites

* Java 17 or above
* Maven
* MySQL Server
* Eclipse / IntelliJ IDEA

Update the database configuration inside:

src/main/resources/application.properties

### Configure Email

Update your Gmail credentials:

spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

### Run the Application

Open the project in your IDE and run:
App.java
The application will be available at:

http://localhost:8082

## Future Enhancements
* File Attachments for Tasks
* Comments on Tasks
* Team Chat

