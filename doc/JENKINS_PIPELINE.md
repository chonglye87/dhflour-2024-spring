# Jenkins 설정 및 배포

## 파이프라인 설정
```json
pipeline {
    agent any

    environment {
        BRANCH = 'hyperx0'
        JAVA_HOME = '/usr/local/java/jdk17' // JAVA_HOME 환경 변수 설정
        PATH = "${JAVA_HOME}/bin:${env.PATH}" // PATH에 JAVA_HOME/bin 추가
    }

    tools {
        gradle 'Gradle 6.8'
    }

    stages {

        stage('Git Clone') {
            steps {
                git branch: BRANCH,
                credentialsId: 'chonglye87',
                url: 'https://github.com/chonglye87/dhflour-2024-spring/tree/hyperx0'
            }
        }

        stage('Build Gradle') {
            steps {
                script {
                    sh 'gradle clean api:build'
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    // Define the JAR file name
                    def jarFile = "api/build/libs/api.jar"
                    def remoteDir = "/api"
                    def logFile = "application.log"
                    def script = """
                    bash -c '
                    # Find and kill the process using port 8080 if it exists
                    if lsof -i:8080; then
                        lsof -t -i:8080 | xargs kill -9
                        echo "Existing application stopped on port 8080"
                    else
                        echo "No existing application running on port 8080"
                    fi
                    nohup /usr/local/java/jdk17/bin/java -jar --spring.profiles.active=prod ~${remoteDir}/api.jar > ~${remoteDir}/logs/${logFile} 2>&1 &
                    '
                    """

                    // Use Publish Over SSH to copy the jar file and execute commands
                    sshPublisher(
                        publishers: [
                            sshPublisherDesc(
                                configName: 'Dhflour-Test',
                                transfers: [
                                    sshTransfer(
                                        sourceFiles: jarFile,
                                        removePrefix: 'api/build/libs/',
                                        remoteDirectory: remoteDir,
                                        execTimeout: 10000000,
                                        execCommand: script
                                    )
                                ],
                                usePromotionTimestamp: false,
                                verbose: true,
                            )
                        ]
                    )
                }
            }
        }
    }
}

```
