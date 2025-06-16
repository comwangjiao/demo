//下面注释的代码描述：/* 注释的代码是前端样例构建，放开时需测试验证 */  //注释的代码是将镜像推送到 对应环境的环境仓库
def createVersion() {
    // 定义一个版本号作为当次构建的版本，输出结果 20191210
    return new Date().format('yyyyMMdd')
}
def defaultExpireTime() {
    // 默认过期时间为当前编译开始一个月
    return new Date().plus(60).format('yyyy-MM-dd HH:mm')
}
pipeline {
    agent any
    parameters {
	    /* booleanParam(name: 'SELECT_FRONT', defaultValue: false, description: '编译前端项目') */
        booleanParam(name: 'SELECT_SERVER', defaultValue: true, description: '编译后端代码')
        choice(name: 'BRANCH', choices: ['develop', 'release', '自定义'], description: '分支名称')
        string(name: 'CUSTOM_BRANCH', description: '上面选择自定义时，输入分支名称', defaultValue:  env.CUSTOM_BRANCH ?: 'develop')
        choice(name: 'SERVER', choices: ['172.18.18.174','172.18.18.175'], description: '选择编译运行的服务器')
        choice(name: 'ACTION', choices: ['编译并运行', '仅编译', '编译运行发布'], description: '编译操作')
        booleanParam(
          defaultValue: false,
          name: 'ForceExpire'
        )
        string(
          defaultValue: defaultExpireTime(), 
          name: 'ExpireTime', 
          trim: true
        )
    }
    environment {
        SERVER_IP = "${params.SERVER}"
        CONTAINER_NAME = "demo-server"
        IMAGE_NAME_SERVER = "demo"
    }
    stages {
        stage('source') {
            steps {
                cleanWs()
                script {
                    def branchName = params.BRANCH == '自定义' ? params.CUSTOM_BRANCH : params.BRANCH
                    git branch: branchName, credentialsId: 'git', url: 'http://172.18.18.174:3000/gitea/demo.git'
                }
            }
        }
        stage('Build') {
            steps {
                script {
	 		       /*
                    if (params.SELECT_FRONT) {
                        buildFront()
                    } */
                    if (params.SELECT_SERVER) {
                        buildServer()
                    }
                }
            }
        }
        stage('Run') {
            steps {
                script {
                    if (params.ACTION == '编译并运行') {
                        runServer()
                        //pushDev()
                    }
                    if (params.ACTION == '编译运行发布') {
                        runServer()
                        //release()
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                docker.withServer(env.DOCKER_SERVER, 'docker-ca-client-dev') {
                    sh 'docker image prune -f'
                }
            }
        }
    }
}


def buildServer() {
 /*sh 'mkdir -p demo/src/main/resources/static'
    sh 'rm -rf demo/src/main/resources/static/*'
    
    // 检查目标目录是否为空
    def distDir = '/var/jenkins_home/workspace/demo/dist/'
    def isEmpty = sh(script: "if [ -z \"`ls -A ${distDir}`\" ]; then exit 0; else exit 1; fi", returnStatus: true) == 0

    // 如果目录为空，先执行指定的 Jenkins 任务
    if (isEmpty) {
        echo "Directory is empty. Triggering 'demo-front' Jenkins job."
        build job: 'demo-front', wait: true
    }

    sh "cp -r ${distDir} demo/src/main/resources/static" */
    sh 'chmod +x ./gradlew'
    sh './gradlew clean bootJar'
    sh 'ls -la build/libs || true'
    docker.withServer(env.DOCKER_SERVER, 'docker-ca-client-dev') {
       sh "docker build -f Dockerfile . -t demo --pull" //这里的Dockerfile位置根据项目目录结构来 sh "docker build --file=./sales-server/Dockerfile ./sales-server/ -t ${IMAGE_NAME_SERVER} --pull"
    }
}

def runServer() {
    docker.withServer(env.DOCKER_SERVER, 'docker-ca-client-dev') {
        sh 'docker rm -f ${CONTAINER_NAME}'
        sh '''docker create --name ${CONTAINER_NAME} -p 8086:8086 \
        -v /var/run/docker.sock:/var/run/docker.sock \
        -v /etc/localtime:/etc/localtime:ro \
        -e DB_HOST=mysql8:3306 \
        -e DB_DATABASE=arena-neo \
        -e DB_USERNAME=arena \
        -e DB_PASSWORD=e6T6RwAtGz6eJWZ2 \
        --restart unless-stopped ${IMAGE_NAME_SERVER}'''
        sh 'docker start ${CONTAINER_NAME}'
    }
}
/*
def buildFront() {
    build 'arena-sales-front'
}

*/

//将本地构建好的镜像（如 demo）推送到 测试环境 的私有仓库 172.18.18.174:5000
 def pushDev() {
    docker.withServer(env.DOCKER_SERVER, 'docker-ca-client-dev') {
         sh "docker tag ${IMAGE_NAME_SERVER} 172.18.18.174:5000/${IMAGE_NAME_SERVER}"
         sh "docker push 172.18.18.174:5000/${IMAGE_NAME_SERVER}"
     }
 }

//只是把镜像推送到 生产环境仓库 172.18.21.25:5000
// def release() {
//     docker.withServer(env.DOCKER_SERVER, 'docker-ca-client-dev') {
//         sh "docker tag ${IMAGE_NAME_SERVER} 172.18.21.25:5000/${IMAGE_NAME_SERVER}"
//         sh "docker push 172.18.21.25:5000/${IMAGE_NAME_SERVER}"
//     }
// }
