@Library("devops-groovy-libs-dev") _

buildPipeline {
    artifactory = "getCreative"
    android = [
        bundled   : "android/app/src/getCreative/assets/bundled",
        game      : "getCreative",
        store     : "normal",
        buildType : "debug"
    ]
    ios = [
        bundled   : "ios/Games/Get Creative/bundled",
        archive   : "getCreative",
        scheme    : "Get Creative Development"
    ]
}
