@Library("devops-groovy-libs-dev")_

deploymentPipeline {
   // These values are variable
   // This is the github app name
   githubAndroidAppName = "cbeebies-get-creative-app"
   picknmixVersionAndroid = "develop"
   picknmixVersionIOS = "develop"
   appVersionName = "1.3.0"
   appVersionCode=40
   releaseCandidateInt = "2"

   // These values are fixed
   androidAppName = "getCreative"
   iosAppName = "Get Creative"
   appArtifactoryRepo = "getCreative"
   androidBuildTypes = [ ["NormalUat", "normal", "1fe5b0e5aa6144dfbe05dfdfe46d5d6d"],
                               ["FreetimeUat", "freetime", "34859f316951458db421598bc6bd2b46"]]

   ios = [
       bundled   : "ios/Games/Get Creative/bundled",
       archive   : "getCreative",
       scheme    : "Get Creative UAT",
       hockeyAppId: "36cbc7c220844881bbe42d7a21fb1609"
   ]
}
