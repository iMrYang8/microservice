pipeline {
    agent any
    environment {
        docker_registry = 'registry.cn-hangzhou.aliyuncs.com'
//service_list ='eureka-service gateway-service product-service product-client'
        service_list = 'eureka-service'
//version = "${env.BUILD_NUMBER}"
//如果每次根据版本生成镜像，则将下面的去掉，用上面那个
        version = "latest"
    }
    stages {
        stage('Checkout') {
            steps {
                // 拉取代码
                withCredentials([usernamePassword(credentialsId: 'gitee-credentials', usernameVariable: 'GITEE_USERNAME', passwordVariable: 'GITEE_PASSWORD')]) {
                    git credentialsId: 'gitee-credentials', url: 'git clone https://gitee.com/frankgy/microservice-demo-n.git', branch: 'master'
                }
            }
        }

        stage('Build') {
            steps {
                // 使用maven进行编译打包
                sh 'mvn package'
            }
        }

        stage('Build Images') {
            steps {
                script {
                    def work_dir = pwd()
                    for (service in service_list.split()) {
                        dir("$work_dir/$service") {
                            service = service.replaceAll(/-.*$/, '')
                            def image_name = "${docker_registry}/my-stock/${service}"
                            echo "${image_name}"
                            sh "nerdctl build -f DockerfileJ -t ${image_name} ."
                            sh "nerdctl push ${image_name}"

                        }
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh 'echo $version'
                    // 推送Docker镜像到阿里云
                    withCredentials([usernamePassword(credentialsId: 'aliyun-credentials', usernameVariable: 'ALIYUN_USERNAME', passwordVariable: 'ALIYUN_PASSWORD')]) {
                        sh "nerdctl login --username=$ALIYUN_USERNAME --password=$ALIYUN_PASSWORD registry.cn-hangzhou.aliyuncs.com"
                        for (service in service_list.split()) {
                            def image_name = "${docker_registry}/my-stock/${service}"
                            sh "nerdctl tag ${image_name}:latest ${image_name}:${version}"
                            sh "nerdctl push ${image_name}:${version}"
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {

                    withCredentials([certificate(aliasVariable: '', credentialsId: 'k8s-certificate', keystoreVariable: 'CERTIFICATE_KEYSTORE', passwordVariable: 'CERTIFICATE_PASSWORD')]) {
                        sh 'echo $version'
                        // 如果每次部署时镜像的版本都是不同的，可以在Jenkins Pipeline的脚本中使用变量来动态指定镜像版本
                        // sh "sed -i 's/image: myimage:image: myimage:${IMAGE_VERSION}/' eruka.yaml"
                        def work_dir = pwd()
                        sh 'echo $work_dir'
                        dir("$work_dir/k8s") {
                            sh 'ls'
                            for (service in service_list.split()) {
                                sh "kubectl create -f $service.yaml"
                            }
                        }
                    }
                }
            }
        }
    }

}
