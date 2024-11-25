# Library for Managing Database Migrations using JDBC
**Features**
- Manage migrations with SQL files
- Version control for migrations
- Error handling for migrations
- Configuration through a properties file
- Migrations synchronization (locking mechanism)
- Support for PostgreSQL
- Logging of migration history
- Report generation for migration results

**Setup instructions**

**Installation**
<br> **1.** Clone the repository
git clone https://github.com/Auramenka/DatabaseMigrationLibrary.git
<br> **2.** Open the project in IDE
<br> **3.** Download the PostgreSQL image and run PostgreSQL container in Docker
docker pull postgres:latest
docker run --name postgres -e POSTGRES_USER=your_username -e POSTGRES_PASSWORD=your_password -e POSTGRES_DB=your_database -p 5432:5432 -d postgres
Replace your_username, your_password, your_database with your values
<br> **4.** Build the project
<br> **5.** Publish to local Maven repository

**Usage**
<br> **1.** Create a test Java project
<br> **2.** Add the library dependency to your build.gradle and include mavenLocal() in the repositories {} section
<br> **3.** Create an application.properties file in the resources folder
<br> **4.** Update application.properties file in src/main/resources with your PostgreSQL credentials 
<br> **5.** Create a migrations folder in the resources folder and add your SQL files there
<br> **6.** In the Main class, initiate the migrations using the following command: MigrationTool.runMigrations();
