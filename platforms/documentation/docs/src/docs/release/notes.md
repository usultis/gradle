<meta property="og:image" content="https://gradle.org/images/releases/gradle-default.png" />
<meta property="og:type"  content="article" />
<meta property="og:title" content="Gradle @version@ Release Notes" />
<meta property="og:site_name" content="Gradle Release Notes">
<meta property="og:description" content="We are excited to announce Gradle @version@.">
<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:site" content="@gradle">
<meta name="twitter:creator" content="@gradle">
<meta name="twitter:title" content="Gradle @version@ Release Notes">
<meta name="twitter:description" content="We are excited to announce Gradle @version@.">
<meta name="twitter:image" content="https://gradle.org/images/releases/gradle-default.png">

We are excited to announce Gradle @version@ (released [@releaseDate@](https://gradle.org/releases/)).

This release features [1](), [2](), ... [n](), and more.

Gradle 9.0 has many bug fixes and other general improvements. As a major version, this release also has changes to deprecated APIs and behavior. Consult the [Gradle 8.x upgrade guide](userguide/upgrading_version_8.html) for guidance on removed APIs and behavior.

<!-- 
Include only their name, impactful features should be called out separately below.
 [Some person](https://github.com/some-person)

 THIS LIST SHOULD BE ALPHABETIZED BY [PERSON NAME] - the docs:updateContributorsInReleaseNotes task will enforce this ordering, which is case-insensitive.
-->

We would like to thank the following community members for their contributions to this release of Gradle:

[Victor Merkulov](https://github.com/urdak)

Be sure to check out the [public roadmap](https://blog.gradle.org/roadmap-announcement) for insight into what's planned for future releases.

## Upgrade instructions

Switch your build to use Gradle @version@ by updating the [wrapper](userguide/gradle_wrapper.html) in your project:

```
./gradlew wrapper --gradle-version=@version@ && ./gradlew wrapper
```

See the [Gradle 8.x upgrade guide](userguide/upgrading_version_8.html#changes_@baseVersion@) to learn about deprecations, breaking changes, and other considerations when upgrading to Gradle @version@.

For Java, Groovy, Kotlin, and Android compatibility, see the [full compatibility notes](userguide/compatibility.html).   

## New features and usability improvements

### Kotlin 2 TO DO

### Groovy 4

Gradle embeds the latest stable release of Groovy 4.0. This is a major version jump from Gradle 7 and 8 that used Groovy 3.0.

This brings a range of new features and improvements.
See the [Groovy 4.0 release notes](https://groovy-lang.org/releasenotes/groovy-4.0.html) for full details.

Gradle uses Groovy for Groovy DSL build scripts (`.gradle`) and Ant integration.

Be aware that some behavior between Groovy 3.0 and 4.0 has changed in build scripts.
See the [Gradle 8.x upgrade guide](userguide/upgrading_version_8.html#groovy-4) for details.

<a name="semver"></a>
### SemVer release versioning

Starting with Gradle 9.0.0, all Gradle releases follow the [Semantic Versioning (SemVer)](https://semver.org/spec/v2.0.0.html) specification.
Version numbers are now expressed as `MAJOR.MINOR.PATCH`,
whereas previous minor releases omitted the patch segment (e.g., `8.5` instead of `8.5.0`).
Note that this change does not retroactively apply to older releases or any future backports targeting them.
Also, `@Incubating` features can still [be changed](/userguide/feature_lifecycle.html#sec:incubating_state) in minor releases as they are not considered part of the public API.

Gradle now allows specifying only a major or minor version when setting the wrapper version.
For example:
```sh
./gradlew wrapper --gradle-version=9
```
resolves to the latest normal release of Gradle `9.x.y`, while:
```sh
./gradlew wrapper --gradle-version=9.1
```
resolves to the latest `9.1.x` version.
Note that this feature only works with Gradle `9.0.0` and above.
Older Gradle version do not follow the SemVer specification and, for example, `8.12` would be ambiguous between `8.12` (because it's an exact version) and `8.12.1` (semantically the latest version for `8.12`).

To support the feature, [version information endpoints](https://services.gradle.org/versions/) were extended to include endpoints for major versions.
For example, [https://services.gradle.org/versions/9](https://services.gradle.org/versions/9) returns all versions of Gradle with major version 9.

<a name="config-cache"></a>
### Configuration Cache improvements

Gradle's [Configuration Cache](userguide/configuration_cache.html) improves build performance by caching the result of the configuration phase.
Gradle uses the Configuration Cache to reload a saved configuration when nothing that affects the build configuration has changed.

#### CC TO DO

Check out the link:https://blog.gradle.org/road-to-configuration-cache[blog post] to learn more.

<a name="build-authoring"></a>
### Build authoring improvements

Gradle provides rich APIs for plugin authors and build engineers to develop custom build logic.

### Kotlin build script compilation avoidance

With this release, the mechanism for detecting ABI (Application Binary Interface) changes in [Kotlin DSL](userguide/kotlin_dsl.html) (`.kts`) build scripts has been significantly improved.
Gradle now relies on the Kotlin distribution’s own ABI fingerprinting instead of its previous internal mechanism.

The biggest advantage of the new mechanism is the ability to work in the presence of inline functions, something that Gradle wasn't handling efficiently until now.
This results in substantial performance gains depending on your build and the changes made to the build logic.

For example, in the `gradle/gradle` project, non-ABI changes to build logic now result in up to a 60% reduction in configuration time by avoiding unnecessary script recompilation.

![Reduction in unnecessary script recompilation](release-notes-assets/help_after_nonABI_change_in_buildSrc.png)

#### Gradle API now uses JSpecify Nullability Annotations

Since Gradle 5.0 we've been using annotations from the dormant and unfinished JSR-305 to make the nullness of type usages explicit for the Gradle API.
Starting with Gradle 9.0, the Gradle API is annotated using JSpecify instead.

For more details and potential breakages, see the dedicated [upgrading guide section](userguide/upgrading_version_8.html).

#### New `RootComponentIdentifier`

Gradle introduces a new subtype of `ComponentIdentifier` called `RootComponentIdentifier`, which represents the root of a resolved dependency graph.
When a configuration is resolved, it is transformed into a synthetic variant that serves as the root of a dependency graph.
The root component exists solely to own the root variant.

Dependency graphs resolved from `buildscript` configurations and detached configurations will have a component identified by a `RootComponentIdentifier` at the root of their graph.
Resolved project configurations will continue to have their root component live within the project's component, identified with a `ProjectComponentIdentifier`.
With this change, detached configurations can now resolve dependencies on their own project.

In future Gradle versions, all configurations, including those declared inside projects, will also be owned by a synthetic root component identified by a `RootComponentIdentifier`.

## Promoted features

Promoted features are features that were incubating in previous versions of Gradle but are now supported and subject to backward compatibility.
See the User Manual section on the "[Feature Lifecycle](userguide/feature_lifecycle.html)" for more information.

The following are the features that have been promoted in this Gradle release.

### Promoted features in the Kotlin DSL

The following operator functions in `DependencyHandlerScope` are now considered stable:
* `NamedDomainObjectProvider<Configuration>.invoke(dependencyNotation: Any): Dependency?`
* `NamedDomainObjectProvider<Configuration>.invoke(dependencyNotation: String, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency`
* `NamedDomainObjectProvider<Configuration>.invoke(group: String, name: String, version: String?, configuration: String?, classifier: String?, ext: String?): ExternalModuleDependency`
* `NamedDomainObjectProvider<Configuration>.invoke(group: String, name: String, version: String?, configuration: String?, classifier: String?, ext: String?, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency`
* `NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependencyNotation: Any): Dependency?`
* `NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependencyNotation: String, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency`
* `NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(group: String, name: String, version: String?, configuration: String?, classifier: String?, ext: String?): ExternalModuleDependency`
* `NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(group: String, name: String, version: String?, configuration: String?, classifier: String?, ext: String?, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency`
* `<T : Any> Configuration.invoke(dependency: Provider<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> Configuration.invoke(dependency: ProviderConvertible<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> NamedDomainObjectProvider<Configuration>.invoke(dependency: Provider<T>)`
* `<T : Any> NamedDomainObjectProvider<Configuration>.invoke(dependency: ProviderConvertible<T>)`
* `<T : Any> NamedDomainObjectProvider<Configuration>.invoke(dependency: Provider<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> NamedDomainObjectProvider<Configuration>.invoke(dependency: ProviderConvertible<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependency: Provider<T>)`
* `<T : Any> NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependency: ProviderConvertible<T>)`
* `<T : Any> NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependency: Provider<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependency: ProviderConvertible<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> String.invoke(dependency: Provider<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : Any> String.invoke(dependency: ProviderConvertible<T>, dependencyConfiguration: ExternalModuleDependency.() -> Unit)`
* `<T : ModuleDependency> NamedDomainObjectProvider<Configuration>.invoke(dependency: T, dependencyConfiguration: T.() -> Unit): T`
* `<T : ModuleDependency> NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependency: T, dependencyConfiguration: T.() -> Unit): T`

The following operator functions in `DependencyConstraintHandlerScope` are now considered stable:
* `NamedDomainObjectProvider<Configuration>.invoke(dependencyConstraintNotation: Any): DependencyConstraint`
* `NamedDomainObjectProvider<Configuration>.invoke(dependencyConstraintNotation: String, configuration: DependencyConstraint.() -> Unit): DependencyConstraint`
* `NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependencyConstraintNotation: Any): DependencyConstraint`
* `NamedDomainObjectProvider<DependencyScopeConfiguration>.invoke(dependencyConstraintNotation: String, configuration: DependencyConstraint.() -> Unit): DependencyConstraint`

The following top-level functions in `DependencyHandlerExtensions` are now considered stable:
* `DependencyHandler.create(dependencyNotation: String, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency`

The following top-level functions in `KotlinDependencyExtensions` are now considered stable:
* `PluginDependenciesSpec.embeddedKotlin(module: String): PluginDependencySpec`

The following functions are now considered stable:
* `GroovyBuilderScope.hasProperty(name: String): Boolean`

<!--
### Example promoted
-->

## Fixed issues

<!--
This section will be populated automatically
-->

## Known issues

Known issues are problems that were discovered post-release that are directly related to changes made in this release.

<!--
This section will be populated automatically
-->

## External contributions

We love getting contributions from the Gradle community. For information on contributing, please see [gradle.org/contribute](https://gradle.org/contribute).

## Reporting problems

If you find a problem with this release, please file a bug on [GitHub Issues](https://github.com/gradle/gradle/issues) adhering to our issue guidelines.
If you're not sure if you're encountering a bug, please use the [forum](https://discuss.gradle.org/c/help-discuss).

We hope you will build happiness with Gradle, and we look forward to your feedback via [Twitter](https://twitter.com/gradle) or on [GitHub](https://github.com/gradle).
