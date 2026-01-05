pipeline {
    agent any
    
    tools {
        // Configure Maven if available in Jenkins
        // Go to Jenkins > Manage Jenkins > Global Tool Configuration
        // Add Maven installation and name it 'Maven'
        maven 'Maven'
    }
    
    environment {
        // Maven settings
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=512m'
        // Adjust JAVA_HOME based on your JDK tool name in Jenkins
        // Or set it to your system Java path
        // JAVA_HOME = tool 'java17'
    }
    
    options {
        // Discard old builds to save disk space
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Timeout after 30 minutes
        timeout(time: 30, unit: 'MINUTES')
        // Add timestamps to console output
        timestamps()
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '========================================'
                echo 'Stage: Checkout from Git SCM'
                echo '========================================'
                checkout scm
                
                // Display Git information
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                    def gitBranch = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                    echo "Git Branch: ${gitBranch}"
                    echo "Git Commit: ${gitCommit}"
                }
            }
        }
        
        stage('Build') {
            steps {
                echo '========================================'
                echo 'Stage: Build Project'
                echo '========================================'
                sh '''
                    echo "Building Spring Boot application..."
                    mvn clean compile
                    echo "Build completed successfully!"
                '''
            }
            post {
                success {
                    echo '✓ Build stage completed successfully'
                }
                failure {
                    echo '✗ Build stage failed'
                }
            }
        }
        
        stage('Test') {
            steps {
                echo '========================================'
                echo 'Stage: Run Tests'
                echo '========================================'
                sh '''
                    echo "Running unit tests..."
                    mvn test
                    echo "Tests completed!"
                '''
            }
            post {
                always {
                    // Publish test results
                    junit 'target/surefire-reports/*.xml'
                    
                    // Publish test coverage if available
                    // publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]
                }
                success {
                    echo '✓ All tests passed'
                }
                failure {
                    echo '✗ Some tests failed'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '========================================'
                echo 'Stage: Package Application'
                echo '========================================'
                sh '''
                    echo "Packaging Spring Boot application..."
                    mvn package -DskipTests
                    echo "Packaging completed!"
                '''
            }
            post {
                success {
                    echo '✓ Application packaged successfully'
                    // Archive the JAR artifact
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: false
                    
                    // Display artifact information
                    script {
                        def jarFiles = sh(returnStdout: true, script: 'ls -la target/*.jar').trim()
                        echo "Generated artifacts:\n${jarFiles}"
                    }
                }
                failure {
                    echo '✗ Packaging failed'
                }
            }
        }
    }
    
    post {
        always {
            echo '========================================'
            echo 'Pipeline Execution Summary'
            echo '========================================'
            // Clean up workspace (optional - uncomment if needed)
            // cleanWs()
        }
        success {
            echo '✓✓✓ Pipeline succeeded! ✓✓✓'
            // You can add notifications here (email, Slack, etc.)
            // emailext (
            //     subject: "Pipeline Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
            //     body: "Build succeeded. Check console output at ${env.BUILD_URL}",
            //     to: "your-email@example.com"
            // )
        }
        failure {
            echo '✗✗✗ Pipeline failed! ✗✗✗'
            // You can add failure notifications here
            // emailext (
            //     subject: "Pipeline Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
            //     body: "Build failed. Check console output at ${env.BUILD_URL}",
            //     to: "your-email@example.com"
            // )
        }
        unstable {
            echo '⚠ Pipeline is unstable!'
        }
        cleanup {
            echo 'Cleaning up...'
        }
    }
}
