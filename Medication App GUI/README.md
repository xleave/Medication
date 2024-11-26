# Medication Reminder Application

Welcome to the **Medication Reminder Application**, a Java-based console application designed to help users manage their medications efficiently. This application supports both regular users and administrators, allowing for personalized and comprehensive medication management.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Directory Structure](#directory-structure)
- [Usage](#usage)
  - [Running the Application](#running-the-application)
  - [User Operations](#user-operations)
  - [Admin Operations](#admin-operations)
- [Creating an Admin Account](#creating-an-admin-account)
- [Example Usage](#example-usage)

## Features

- **User Registration & Login:** Securely create and log into user accounts.
- **Admin Privileges:** Administrators can manage all users' medications.
- **Medication Management:** Add, remove, and view medications.
- **Organized Data Storage:** Stores user and medication data in CSV files within a structured directory.

## Prerequisites

- **Java Development Kit (JDK):** Ensure you have JDK 8 or later installed. You can download it from [here](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html).
- **IDE (Optional):** An Integrated Development Environment like Visual Studio Code or IntelliJ IDEA for easier development and management.

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-username/medication-reminder-app.git
   ```

2. **Navigate to the Project Directory:**

   ```bash
   cd medication-reminder-app
   ```

3. **Compile the Java Files:**

   ```bash
   javac -d bin src/*.java src/modules/*.java
   ```

4. **Ensure Directory Structure:**

   The application uses a `lists` directory within the `src` folder to store medication CSV files. The application will automatically create this directory if it doesn't exist upon user registration.

## Directory Structure

```markdown
medication-reminder-app/
├── src/
│   ├── lists/                 # Stores medication CSV files
│   ├── modules/
│   │   └── Medicine.java      # Medicine class definition
│   ├── User.java              # User management and medication operations
│   └── Main.java              # Entry point of the application
├── bin/                       # Compiled Java classes
├── users.csv                  # Stores user credentials and roles
└── 

README.md

                  # Project documentation
```

## Usage

### Running the Application

After compiling, run the application using the following command:

```bash
java -cp bin Main
```

### User Operations

Upon running the application, users can perform the following operations:

1. **Add Medicine**
2. **Remove Medicine**
3. **View Medicines**
4. **Logout**

#### Example

```text
Please enter your details
Name: john
Password: johnpassword
Login successful.

Medicine Management:

1. Add Medicine
2. Remove Medicine
3. View Medicines
4. Logout
Choose an option:
```

### Admin Operations

Administrators have elevated privileges allowing them to manage all users' medications in addition to the regular user operations.

1. **Add Medicine**
2. **Remove Medicine**
3. **View Medicines**
4. **Manage All Users' Medicines**
5. **Logout**

### Example

```text
Please enter your details
Name: admin
Password: admin123
Login successful.

Medicine Management:

1. Add Medicine
2. Remove Medicine
3. View Medicines
4. Manage All Users' Medicines
5. Logout
Choose an option:
```

## Creating an Admin Account

**Note:** The current implementation does not provide a direct way to create an admin account through the application interface. You need to manually add an admin account to the `users.csv` file.

1. **Locate the `users.csv` File:**

   The `users.csv` file is located in the `src` directory.

2. **Open `users.csv` in a Text Editor:**

3. **Add an Admin Entry:**

   Add a new line with the following format:

   ```text
   admin_username,admin_password,admin
   ```

   **Example:**

   ```text
   admin,admin123,admin
   ```

   - Replace `admin` with your desired admin username.
   - Replace `admin123` with a strong password of your choice.

4. **Save the File:**

   Ensure that the entry is saved correctly without any additional spaces or characters.

5. **Run the Application and Log in as Admin:**

   Use the admin credentials you just created to log in and access admin functionalities.

## Example Usage

### Registering a New User

```text
Please enter your details
Name: alice
Password: alicepassword
Account created successfully. Please log in.
Hello alice. Please enter your Password.
Login successful.

Medicine Management:

1. Add Medicine
2. Remove Medicine
3. View Medicines
4. Logout
Choose an option:
```

### Adding a Medicine as a Regular User

```text
Choose an option: 1
Medicine Name: Ibuprofen
Dosage: 200mg
Quantity: 30
Time to Take: Morning
Medicine added successfully.
```

### Managing All Users' Medicines as Admin

```text
Choose an option: 4
All Users' Medicine Management:
1. View User's Medicines
2. Add Medicine to User
3. Remove Medicine from User
4. Back to Main Menu
Choose an option:
```
