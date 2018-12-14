@Library("devops-groovy-libs")_

deploymentPipeline {
   // These values are variable
   // This is the github app name
   githubAndroidAppName = "cbeebies-get-creative-app"
   picknmixVersionAndroid = "develop"
   picknmixVersionIOS = "develop"
   appVersionName = "1.2.7"
   appVersionCode=40
   releaseCandidateInt = "1"

   // These values are fixed
   androidAppName = "getCreative"
   iosAppName = "Get Creative"
   appArtifactoryRepo = "getCreative"
   androidBuildTypes = [ ["NormalUat", "normal", "1fe5b0e5aa6144dfbe05dfdfe46d5d6d"],
                               ["FreetimeUat", "freetime", "34859f316951458db421598bc6bd2b46"]]

   ios = [
       bundled   : "ios/Games/Get Creative/bundled",
       archive   : "getCreative",
       scheme    : "Get Creative UAT"
   ]
}