# Development Setup
This document provides information for a developer to get their local development environment configured so that they can do active development on this module. The instructions are specific to a macOS environment.

### Maven
The Gym Roster organization's `gym-common` package is used in this module. It is versioned and deployed as a GitHub package in its repo and not on a centralized Maven repository such as MVN Central.
As a result, you will be need to set up the Maven settings so that a `mvn install` can authenticate to the repo to download the `gym-common` dependency that is referenced in the `pom.xml`.

#### Generate your GITHUB token

1. Go to: https://github.com/settings/tokens
1. Click "Generate new token (classic)"
1. Note:
   * Package registry reader token
1. Expiration
   * No expiration.
1. Select scopes:
   * read:packages
1. Click `Generate token` and copy it.

#### Update your local ~/.m2/settings.xml:
```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

Replace `YOUR_GITHUB_USERNAME` with your GitHub user name and `YOUR_GITHUB_TOKEN` with the token that was generated in the previous step.
