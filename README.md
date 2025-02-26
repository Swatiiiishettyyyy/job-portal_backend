

# Job Portal - Placement Management System

## Project Overview

The **Job Portal** is a web application designed to streamline the placement process within colleges by providing personalized updates for students and tracking placement rounds. This system eliminates the need for WhatsApp updates, making the process more efficient and transparent.



## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA
- **Frontend**: React
- **Database**: MySQL 8



## Features

### For Students:
- **Personalized Placement Updates**: Receive updates on job opportunities based on eligibility.
- **Eligibility Tracking**: Track eligibility for different job positions.
- **Placement Round Tracking**: View progress through various placement rounds.
- **Profile Management**: Create and update personal profiles.
- **Job Alerts**: Receive automatic alerts for new job opportunities.
- **Feedback System**: Provide feedback on each placement round.

### For Admins:
- **Placement Round Management**: Track student progress in placement rounds.
- **Student Eligibility Management**: Update eligibility criteria for students.
- **Real-Time Data**: Add new job opportunities and monitor student placements.
- **Reports**: Generate placement reports and track student performance.



## Setup and Installation

### Prerequisites:
- **Java 11+**
- **Maven**
- **Node.js** and **npm**
- **MySQL 8**

### Steps to Set Up:
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/job-portal.git
   ```

2. Set up the backend:
   - Create a MySQL database named `job_portal`.
   - Configure the `application.properties` file with your database details.
   - Run the backend with:
     ```bash
     mvn spring-boot:run
     ```

3. Set up the frontend:
   - Navigate to the frontend directory and install dependencies:
     ```bash
     npm install
     ```
   - Start the frontend with:
     ```bash
     npm start
     ```



