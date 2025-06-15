pipeline {
    // 1. 全局 agent 设置为 none, 允许每个阶段自定义 agent
    agent any

    // 2. 定义流水线中要用到的工具 (名称需与 Jenkins -> Global Tool Configuration 中配置的完全一致)
    tools {
        jdk 'JDK8'
        maven 'maven'
    }

    // 3. 定义环境变量，方便统一管理
    environment {
        // 【请修改】这里替换成您自己的 Docker Hub 用户名
        DOCKER_REGISTRY      = 'mryang8'
        // Docker Hub 凭据的 ID (我们在 Jenkins 中创建的)
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        // Kubernetes 连接凭据的 ID (我们在 Jenkins 中创建的)
        K8S_CREDENTIALS_ID   = 'k8s-jenkins-token'
        // 【请确认】所有需要处理的微服务列表，用空格隔开
        SERVICE_LIST         = 'eureka-service gateway-service user-service product-service stock-client stock-service'
        // 定义 K8S 命名空间
        K8S_NAMESPACE        = 'stockmgr'
    }

    stages {
        // =========================================================
        // 阶段一：检出代码 (Checkout)
        // =========================================================
        stage('Checkout') {
            agent any // 这个简单阶段可以在 Jenkins Master 上运行
            steps {
                echo '--> 1. Checking out source code from GitHub...'
                // 清理工作区，确保每次都是全新的开始
                cleanWs()
                // 使用我们配置好的 SSH 密钥凭据来拉取 GitHub 代码
                git credentialsId: 'Github-Private-Key', url: 'git@github.com:iMrYang8/microservice.git', branch: 'main'
            }
        }

        // =========================================================
        // 阶段二：编译和打包 (Build)
        // =========================================================
        stage('Build with Maven') {
            agent any
            steps {
                echo '--> 2. Building all microservices with Maven...'
                // 使用 mvn 命令，-DskipTests 会跳过单元测试以加快构建速度
                sh 'mvn clean package -DskipTests'
            }
        }

        // =========================================================
        // 阶段三：构建并推送 Docker 镜像 (Build & Push)
        // =========================================================
        stage('Build & Push Docker Images') {
            agent any
            steps {
                script {
                    echo '--> 3. Building and pushing Docker images...'
                    // 使用我们创建的 Docker Hub 凭据登录
                    withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                    }

                    // 循环处理每一个微服务
                    def services = env.SERVICE_LIST.split(' ')
                    for (s in services) {
                        def serviceName = s.trim()
                        // 定义镜像的完整名称和标签，例如：your_dockerhub_username/eureka-service:1
                        def imageName = "${env.DOCKER_REGISTRY}/${serviceName}:${env.BUILD_NUMBER}"

                        // 进入每个服务的子目录中执行 docker build
                        dir(serviceName) {
                            echo "--- Building Docker image for ${serviceName} ---"
                            // 使用标准 docker 命令，而不是 nerdctl
                            sh "docker build -t ${imageName} ."

                            echo "--- Pushing Docker image ${imageName} ---"
                            sh "docker push ${imageName}"
                        }
                    }
                }
            }
        }

        // =========================================================
        // 阶段四：部署到 Kubernetes (Deploy)
        // =========================================================
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo '--> 4. Deploying all services to Kubernetes...'
                    // 使用我们之前配置好的 K8S 连接凭证
                    withKubeconfig([credentialsId: env.K8S_CREDENTIALS_ID]) {

                        // 循环处理每一个微服务
                        def services = env.SERVICE_LIST.split(' ')
                        for (s in services) {
                            def serviceName = s.trim()
                            def imageName = "${env.DOCKER_REGISTRY}/${serviceName}:${env.BUILD_NUMBER}"
                            def yamlFile = "k8s/${serviceName}.yaml"

                            echo "--- Updating image in ${yamlFile} to ${imageName} ---"
                            // 【核心】使用 sed 命令动态替换 YAML 文件中的 image 占位符
                            sh "sed -i 's|image:.*|image: ${imageName}|g' ${yamlFile}"

                            echo "--- Applying ${yamlFile} to Kubernetes namespace ${env.K8S_NAMESPACE} ---"
                            // 使用 kubectl apply 命令进行部署，这个命令是幂等的，比 create 更好
                            sh "kubectl apply -f ${yamlFile} -n ${env.K8S_NAMESPACE}"
                        }
                    }
                }
            }
        }
    }

    // post 部分定义了流水线运行结束后无论成功失败都需要执行的操作
    post {
        always {
            echo '--> Pipeline finished. Cleaning up...'
            // 清理 Docker 登录信息，是一个好习惯
            sh 'docker logout'
            // 清理工作区
            cleanWs()
        }
    }
}

