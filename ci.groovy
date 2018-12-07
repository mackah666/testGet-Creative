@Library("devops-groovy-libs-dev") _

buildPipeline {
    artifactory = "getCreative"
    android = [
        bundled   : "getCreative",
        store     : "normal",
        buildType : "debug"
    ]
    ios = [
        bundled   : "Get Creative",
        archive   : "getCreative",
        scheme    : "Get Creative Development"
    ]
}
