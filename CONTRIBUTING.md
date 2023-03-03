## Prerequisites

- Install JDK 17.
- Install Maven for local artifacts publishing.
- Install Gradle if you don't want to use wrapper.
- Install [GPG2](https://docs.releng.linuxfoundation.org/en/latest/gpg.html) for artifacts signing.
- Install IntelliJ IDEA or any similar IDE.
- Create a fork of [test-data-supplier](https://github.com/sskorol/test-data-supplier/fork).
- Clone the repository:

```shell
git clone https://github.com/[YOUR_GITHUB_ID]/test-data-supplier.git
git remote add upstream https://github.com/sskorol/test-data-supplier.git
git remote -v
```

- Make sure you have `origin` linked with your fork and `upstream` linked with the parent repository.
- Import project into IDE.

## Run / debug configurations

### Build

```shell
clean build publishToMavenLocal --stacktrace
```

Note that you must set `SIGNING_KEY_ID`, `SIGNING_KEY` and `SIGNING_PASSWORD` environment variables to publish artifacts
to Maven local for external testing. You can get these values when you set up `GPG2` key:

```shell
gpg2 --full-generate-key
```

Use 4096-bit RSA encoding. Ensure that email matches your GitHub profile. The passphrase == `SIGNING_PASSWORD`.

Use the following command to get your private `SIGNING_KEY`:

```shell
gpg2 --export-secret-keys --armor D028459F448C1F19 | cat
```

You should copy the main output block between newlines and paste it to your `SIGNING_KEY` env var.

`SIGNING_KEY_ID` can be obtained from the following output:

```shell
gpg2 --list-secret-keys --keyid-format LONG
```

You'll see something like:

```shell
sec   rsa4096/XXXXXXXXXXXXXXXXX 2023-01-01 [SC]
```

Copy the last 8 characters of the key and paste it to `SIGNING_KEY_ID` env var.

### Test

```shell
clean test --stacktrace
```

### Check available dependencies updates

```shell
dependencyUpdates
```

### Debug

To debug your tests, you have to set `DEBUG=true` environment variable and create `Remote JVM Debug` configuration. Note
that the default listening port should be changed to 5005.

When you start your [test](#test) configuration, you'll enter a "listening for remote connections" mode after
compilation.
Then you have to switch to a previously created `Remote JVM Debug` configuration and run it. It'll connect to a
specified port and let you drill into debug mode.

## Process

- Make sure you synchronized your fork with the parent repository before you start working on a new issue/feature.
- Create a [ticket](https://github.com/sskorol/test-data-supplier/issues) which describes the issue you want to fix or
  feature you want to implement. Follow one of the available templates GitHub suggests.
- Create a local Git branch based on the following pattern: `GH-[ISSUE_ID]-[ISSUE_SUMMARY]`
- Make your changes in the local branch.
- Write tests to verify your feature works as expected or bug is fixed.
- Depending on the underlying logic complexity, you may also want to test your changes externally: build a project
  to get a new artifact in your local Maven repository. Then add it as a dependency to the external project for further
  testing.
- Optionally, you can change the version in `gradle.properties` to differentiate your local build from officially
  built artifacts.
- Make sure you follow Java conventions and don't forget to reformat your code before pushing.
- When testing is finished, you can push your changes and create a pull request based on proposed template.
- Code review usually takes some time and require potential changes.
- When you get an approval, make sure you squash your commits before merging.
- To merge your changes to the upstream, use a rebase strategy to keep Git history clean.
