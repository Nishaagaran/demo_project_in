# Jenkins Pipeline Setup Guide

This guide explains how to set up and use the Jenkinsfile for the Inventory Management Spring Boot application.

## Prerequisites

1. **Jenkins** installed and running
2. **Git** repository with your code
3. **Maven** installed on the Jenkins server (or use Maven wrapper)
4. **Java 17** installed on the Jenkins server

## Jenkins Configuration

### 1. Install Required Plugins

Go to **Jenkins > Manage Jenkins > Manage Plugins** and install:
- **Pipeline** (usually pre-installed)
- **Git** plugin
- **JUnit** plugin (for test results)
- **Maven Integration** plugin (optional)

### 2. Configure Global Tools

Go to **Jenkins > Manage Jenkins > Global Tool Configuration**:

#### Configure Maven:
1. Click **"Maven installations"**
2. Click **"Add Maven"**
3. Name: `Maven`
4. Check **"Install automatically"** or provide path to existing Maven installation
5. Click **"Save"**

#### Configure JDK:
1. Click **"JDK installations"**
2. Click **"Add JDK"**
3. Name: `java17`
4. Provide JAVA_HOME path (e.g., `C:\Program Files\Java\jdk-17`)
5. Click **"Save"**

### 3. Create a New Pipeline Job

1. Go to **Jenkins Dashboard**
2. Click **"New Item"**
3. Enter a name (e.g., "Inventory-Management-Pipeline")
4. Select **"Pipeline"**
5. Click **"OK"**

### 4. Configure Pipeline

#### Option A: Pipeline from SCM (Recommended)
1. In the pipeline configuration, select **"Pipeline script from SCM"**
2. **SCM**: Select **"Git"**
3. **Repository URL**: Enter your Git repository URL
   - Example: `https://github.com/yourusername/inventory-management.git`
4. **Credentials**: Add credentials if repository is private
5. **Branches to build**: `*/main` or `*/master` (or your branch name)
6. **Script Path**: `Jenkinsfile`
7. Click **"Save"**

#### Option B: Pipeline Script (Direct)
1. In the pipeline configuration, select **"Pipeline script"**
2. Copy the contents of `Jenkinsfile` into the script text area
3. Click **"Save"**

## Pipeline Stages

The Jenkinsfile includes the following stages:

### 1. **Checkout**
- Checks out code from Git SCM
- Displays Git branch and commit information

### 2. **Build**
- Runs `mvn clean compile`
- Compiles the Spring Boot application

### 3. **Test**
- Runs `mvn test`
- Executes all unit tests
- Publishes test results as JUnit XML

### 4. **Package**
- Runs `mvn package -DskipTests`
- Creates the JAR file
- Archives the artifact for download

## Running the Pipeline

1. Go to your pipeline job in Jenkins
2. Click **"Build Now"**
3. Click on the build number to view progress
4. Click **"Console Output"** to see detailed logs

## Viewing Results

- **Test Results**: Click on the build → "Test Result" link
- **Artifacts**: Click on the build → "Artifacts" to download the JAR file
- **Console Output**: Click on the build → "Console Output" for detailed logs

## Troubleshooting

### Maven Not Found
- Ensure Maven is installed on the Jenkins server
- Or configure Maven in Global Tool Configuration
- Or use `Jenkinsfile.simple` which uses system Maven

### Java Version Issues
- Ensure Java 17 is installed
- Set JAVA_HOME environment variable
- Or configure JDK in Global Tool Configuration

### Git Authentication
- Add Git credentials in Jenkins:
  - Go to **Jenkins > Manage Jenkins > Credentials**
  - Add credentials for your Git repository

### Build Failures
- Check console output for detailed error messages
- Verify all dependencies are available
- Ensure H2 database tests don't require external services

## Customization

### Using Maven Wrapper
If you're using Maven wrapper, update the Jenkinsfile:
```groovy
sh './mvnw clean compile'
sh './mvnw test'
sh './mvnw package -DskipTests'
```

### Adding Deployment Stage
You can add a deployment stage after packaging:
```groovy
stage('Deploy') {
    steps {
        echo 'Deploying application...'
        // Add your deployment commands here
    }
}
```

### Email Notifications
Uncomment the email notification sections in the `post` block and configure:
```groovy
emailext (
    subject: "Pipeline Status: ${env.JOB_NAME}",
    body: "Build status: ${currentBuild.currentResult}",
    to: "your-email@example.com"
)
```

## Alternative: Simple Jenkinsfile

If you encounter issues with tool configuration, use `Jenkinsfile.simple` which:
- Doesn't require Maven tool configuration
- Uses system Maven directly
- Simpler and more straightforward

Just rename `Jenkinsfile.simple` to `Jenkinsfile` or update your pipeline configuration to use it.

