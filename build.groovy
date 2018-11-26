/* comment */
@Library("devops-groovy-libs") _
 /* comment */
def branchNameGlobal = ""
def fullSemanticVersionGlobal = ""
def appNameGlobal = "getCreative"

pipeline {
    agent any
    stages {      
        stage('Update Build Display') {

            steps {
                script{

                   currentBuild.displayName="" 
                   fullSemanticVersionGlobal = sh(returnStdout: true, script: 'gitversion /showvariable FullSemVer').trim()
                   currentBuild.displayName = fullSemanticVersionGlobal
            
                }
            }
        }

        stage('Set Branch Name') {
            steps {
                script {
                    branchNameGlobal = sh(returnStdout: true, script: 'gitversion /showvariable BranchName').trim().minus("origin/")
                    println "Branch name is $branchNameGlobal"
                }
            }
        }

        stage('Print Env Variables') {
            steps {
                sh 'pwd'
                sh 'printenv'
            }
        }
        
        // stage('Update Submodule') {
        //     steps {
                
        //         sh 'git submodule update --init --recursive --'
        //     } 
        // }
        stage('Update Submodule'){
            parallel{
                stage('Udate IOS'){
                    steps{
                        dir('ios'){
                           sh 'git submodule update --init --recursive --' 
                        }
                    }

                }
                stage('Udate Android'){
                    steps{
                        dir('android'){
                           sh 'git submodule update --init --recursive --' 
                        }
                    }

                }
            }
        }


       stage('Npm Install'){
        parallel{
            stage('Npm Install-Android') {
            steps {
                dir('android') {
                    echo 'Install npm and babel'
                    sh 'git submodule foreach npm install'
                    sh 'git submodule foreach npm run babel'

                }
            }
        }

            stage('Npm Install-IOS') {
                steps {
                    dir('ios') {
                        echo 'Install npm and babel'
                        sh 'git submodule foreach npm install'
                        sh 'git submodule foreach npm run babel'

                    }
                }
            }

        }
       
       }


       

        stage('Copy bundled game') {
            steps {
                echo 'Copying bundled assets into the wrapper'
                sh 'cp -R bundled/ android/app/src/getCreative/assets/bundled'
                sh 'cp -R bundled/ "ios/Games/Get Creative/bundled"'
            }
        }

        stage('Assemble Get Creatiive Android') {
            steps {
                dir('android') {
                    echo "Assembling Get Creative"
                    sh 'ls'
                    sh "./gradlew -Dskip.tests app:clean app:assemblegetCreativeNormaldebug"
                    sh 'ls app/build/outputs/apk/'
                }
            }
        }

        /*Publish task*/
        stage('Publish Get Creative Android to Artifactory') {
            steps {
                dir('android') {
                    echo "Publishing to Artifactory"
                    script {
                        utils.publish("android", branchNameGlobal, fullSemanticVersionGlobal, appNameGlobal)
                    }
                }
            }
        } 

        stage('Assemble Creative IOS') {
            steps {
                //def PROVISIONING_PROFILE
                dir('ios') {
                    lock('ios-assemble-picknmix') {
                        // remove developer distrubution provisioning profile which might have been left over by releasepipeline.groovy
                        sh "find \$HOME/Library/MobileDevice/Provisioning\\ Profiles -print0 | xargs -0 grep -wl 'BBC CBBC Pick n Mix Development' | sed 's/.*/\"&\"/' | xargs rm"

                        sh 'xcodebuild -workspace "HTML App Wrapper.xcworkspace" -scheme "Get Creative Development" build archive -sdk iphoneos -archivePath "./build/getCreative.xcarchive"'
                        sh 'xcodebuild -exportArchive -archivePath "./build/getCreative.xcarchive" -exportOptionsPlist "./dev_jenkins_export.plist" -exportPath "./build/"'
                    }
                }
            }
        }

        stage ('Zip DSYM Archive') {
            steps {
                dir('ios/build/getCreative.xcarchive') {
                            sh 'ls -al'
                            // // sh 'echo test > archive/test.txt'
                            zip zipFile: 'getCreative-dSYMs.zip', archive: true, dir: 'dSYMs', glob: '**/**' 
                            archiveArtifacts artifacts: 'getCreative-dSYMs.zip', fingerprint: true
                            
                }

            }
        }


        stage('Publish Get Creative IOS to Artifactory') {
            steps {
                dir('ios/build') {
                    echo "Publishing to Artifactory"
                    script {
                        utils.publish("ios", branchNameGlobal, fullSemanticVersionGlobal, appNameGlobal)
                    }
                }
            }
        } 
    }

    post {
        cleanup {
            echo 'Resetting ios & android directories'
            // this will remove any untracked file/folder, even if included in gitignore
            dir('ios') {
                sh 'git clean -xdf'
                sh 'git submodule foreach --recursive git clean -xdf'
                sh 'git reset --hard'
                sh 'git submodule foreach --recursive git reset --hard'
            }
             dir('android') {
                sh 'git clean -xdf'
                sh 'git submodule foreach --recursive git clean -xdf'
                sh 'git reset --hard'
                sh 'git submodule foreach --recursive git reset --hard'
            }
        }
    }
}

