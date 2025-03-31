import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    vcsRoot(DTmpSettings105)

    buildType(BuildSomething)
    buildType(BuildAll)
}

object BuildAll : BuildType({
    name = "Build All"
    description = "builds everything 2345678"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        root(DTmpSettings105)

        showDependenciesChanges = true
    }

    triggers {
        vcs {
            branchFilter = "+:pull/*"
        }
    }

    features {
        pullRequests {
            vcsRootExtId = "${DTmpSettings105.id}"
            provider = github {
                authType = token {
                    token = "credentialsJSON:a4fc33cf-11f9-4f53-b379-0e7bad097c07"
                }
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
    }

    dependencies {
        snapshot(BuildSomething) {
        }
    }
})

object BuildSomething : BuildType({
    name = "Build Something"
    description = "aaa"

    vcs {
        root(DTmpSettings105, "+:.", "-:.teamcity", "+:.teamcity/scripts")
    }
})

object DTmpSettings105 : GitVcsRoot({
    name = "Main"
    url = "git@github.com:pavelsher/app_test.git"
    branch = "main"
    branchSpec = "+:refs/heads/*"
    authMethod = uploadedKey {
        uploadedKey = "repo_key"
    }
})
