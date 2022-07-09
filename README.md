# Hanabi
# Setup

JVM-Args:
`-Dfml.coreMods.load=cn.hanabi.injection.MixinLoader`
Please replace `cn.hanabi.injection.MixinLoader` with the **full** class name of `MixinLoader` (if you moved the class)

## IntelliJ IDEA
Gradle setup command:
`gradlew setupDecompWorkspace idea genIntelliJRuns build`

## Eclipse
Gradle setup command:
`gradlew setupDecompWorkspace eclipse build`

# Export
Gradle build command:
`gradlew clean build`