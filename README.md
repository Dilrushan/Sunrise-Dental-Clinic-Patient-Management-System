# Sunrise Dental Clinic Patient Management System

A patient management system for Sunrise Dental Clinic, built as a Java desktop application (Swing) alongside a web-based interface. It helps clinic staff manage patients, appointments, treatments and billing through role-based dashboards for Admin, Receptionist and Doctor users.

## Features

- Role-based login (Admin, Receptionist, Doctor) with a Swing desktop UI and a browser-based web UI
- Patient registration and management with treatment history
- Appointment booking, viewing and status tracking
- Treatment fee lookup and billing
- Staff (user) registration and management
- Email notifications and in-app alerts/pop-ups
- Embedded HTTP web server serving an HTML/CSS/JS frontend
- JUnit unit tests for models, controllers and web utilities

## Tech Stack

- Java (Swing for the desktop app)
- Embedded HTTP web server (no external web container required)
- HTML, CSS, JavaScript (web frontend)
- MySQL database
- NetBeans with Apache Ant build
- JUnit for testing

## Project Structure

```
├── src/          Java source code
│   ├── Model/       Data models (User, Patient, Appointment)
│   ├── View/        Swing UI forms and dashboards
│   ├── Controller/  Business logic
│   ├── dao/         Database access objects
│   ├── db/          Database connection
│   ├── web/         Embedded HTTP server, routes and helpers
│   └── sunrise/dental/clinic/  Application entry point
├── web/static/   Web frontend (HTML, CSS, JavaScript)
├── Database/     SQL dump of the database
├── test/         JUnit tests and test runners
└── schema.sql    Database schema and seed data
```

## Database Setup

1. Ensure MySQL is installed and running locally.
2. Run `schema.sql` to create the `sunrise_dental_clinic_db` database, its tables and seed data.
3. The application connects via `jdbc:mysql://localhost/sunrise_dental_clinic_db`.

Default login accounts (see `schema.sql`):

| Username    | Password   | Role          |
|-------------|------------|---------------|
| admin       | admin123   | Admin         |
| drsmith     | doctor123  | Doctor        |
| reception1  | recept123  | Receptionist  |

## Running the Application

1. Open the project in NetBeans.
2. Run the main class `sunrise.dental.clinic.SunriseDentalClinic` (or `Run` from the project menu).