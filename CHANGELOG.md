# Test Data Supplier Changelog

## 1.8.5
**Added missing sources classifier for Maven distribution.**

[4ff246f69c1690c](https://github.com/sskorol/test-data-supplier/commit/4ff246f69c1690c) Sergey Korol *2019-04-15 at 22:40*

## 1.8.4
**Added SPI-off jar support to allow users manually handling DataProviderTransformer via spi-off classifier.**

[083395fd52326c9](https://github.com/sskorol/test-data-supplier/commit/083395fd52326c9) Sergey Korol *2019-04-15 at 21:23*

## 1.8.3
**Updated dependencies which potentially caused issues on latest Java 11.**

[633b652a11c342b](https://github.com/sskorol/test-data-supplier/commit/633b652a11c342b) Sergey Korol *2019-02-17 at 18:26*

## 1.8.2
**Added Java 11 support.
AspectJ is still in RC state, so may produce illegal access warnings.**

[79802dcb6fd494a](https://github.com/sskorol/test-data-supplier/commit/79802dcb6fd494a) Sergey Korol *2018-10-03 15:44:00*

## 1.8.1
**Added ITestNGListener SPI back to support non-modular apps without explicit forcing users to put implementation to their projects.**

[986e1e2cb3bac67](https://github.com/sskorol/test-data-supplier/commit/986e1e2cb3bac67) Sergey Korol *2018-09-29 21:33:00*

## 1.8.0
**Update README.md**

 * Fixed README typo.

[f72bad6f7fe3f05](https://github.com/sskorol/test-data-supplier/commit/f72bad6f7fe3f05) Sergey Korol *2018-09-29 18:43:58*

**Fixed broken test which referred to trevis config.**

 * Updated README with Java 10 usage examples.
 * Added more generic copy task to support both common test task and jacoco instrumentation.

[5c7bf526d3fc31b](https://github.com/sskorol/test-data-supplier/commit/5c7bf526d3fc31b) Sergey Korol *2018-09-29 18:30:29*

**Refactored project to support Java 10.**

 * Java 11 is not supported by Lombok and AspectJ yet, so we have to wait for new releases.
 * Lombok was removed at all for now, as it doesn&#39;t work well with modular projects.
 * New SPI updates affected coverage, as it&#39;s not possible to test implementation classes within test sources root. The only option is moving implementations outside to a separate module. But I don&#39;t believe we want to split tests from main sources.

[1c759014ca2e3af](https://github.com/sskorol/test-data-supplier/commit/1c759014ca2e3af) Sergey Korol *2018-09-29 17:01:40*

**Bumped release version**


[5e30a0a529d27cd](https://github.com/sskorol/test-data-supplier/commit/5e30a0a529d27cd) Sergey Korol *2018-06-15 11:39:00*

## 1.7.0
**Fixed README typos**


[2a6ac8a36c17b48](https://github.com/sskorol/test-data-supplier/commit/2a6ac8a36c17b48) Sergey Korol *2018-06-15 11:33:31*

**Replaced DataSourceUtils with a generic DataReader implementations for Json, Yaml and Csv types. See TestDataReader javadocs for details.**


[65ab82239b78475](https://github.com/sskorol/test-data-supplier/commit/65ab82239b78475) Sergey Korol *2018-06-15 11:25:12*

**Fixed broken README layout**


[0e5323fee95a09a](https://github.com/sskorol/test-data-supplier/commit/0e5323fee95a09a) Sergey Korol *2018-06-12 21:08:19*

**Version bumping**


[bb451762ad50341](https://github.com/sskorol/test-data-supplier/commit/bb451762ad50341) Sergey Korol *2018-06-12 21:05:21*

## 1.6.5
**Minor refactoring.**


[d321e37a35183a3](https://github.com/sskorol/test-data-supplier/commit/d321e37a35183a3) Sergey Korol *2018-06-12 20:53:14*

**Added YAML file processing support via the same technique as was used for JSON and CSV.**


[dec4be682a1e4ce](https://github.com/sskorol/test-data-supplier/commit/dec4be682a1e4ce) Sergey Korol *2018-06-12 20:46:11*

**Fixed Sonar configuration issues.**

 * Revised naming and ordering of some fields and entities.

[9e00a7c7d8feaab](https://github.com/sskorol/test-data-supplier/commit/9e00a7c7d8feaab) Sergey Korol *2018-06-10 08:50:20*

**Refactoring:**

 * Replaced gradle quality plugin with Sonar Cloud.
 * Decomposed some heavy code pieces.
 * Minor cleanup.

[272276ce3e7cc20](https://github.com/sskorol/test-data-supplier/commit/272276ce3e7cc20) Sergey Korol *2018-06-08 15:39:37*

**Version bumping**


[203642cc922c69c](https://github.com/sskorol/test-data-supplier/commit/203642cc922c69c) Sergey Korol *2018-06-06 12:29:44*

## 1.6.0
**Refactoring:**

 * Moved data annotations to model package.
 * Updated README with new features.

[18a12e826113f54](https://github.com/sskorol/test-data-supplier/commit/18a12e826113f54) Sergey Korol *2018-06-06 10:43:36*

**Core updates:**

 * Added a new utility class for data retrieval from JSON / CSV files and urls.
 * Added new annotations for Java entities to specify source and field names.
 * Refactored meta data (moved some generic staff into reflection utils).
 * Quality:
 * Updated gradle plugin to the latest version.
 * Gradle / libs:
 * Updated wrapper and key libraries&#39; versions.

[9d5272aa9a3993c](https://github.com/sskorol/test-data-supplier/commit/9d5272aa9a3993c) Sergey Korol *2018-06-06 10:06:55*

**Update CHANGELOG.md**

 * Bumped release version.

[8b2daca277bca5a](https://github.com/sskorol/test-data-supplier/commit/8b2daca277bca5a) Sergey Korol *2018-04-04 14:15:20*

## 1.5.5
**Downgrading due to 4.5.1 due to caching issue.**


[8d2ce2c787ec765](https://github.com/sskorol/test-data-supplier/commit/8d2ce2c787ec765) Sergey Korol *2018-04-04 13:35:14*

**Increased coverage.**


[b6b652b8f4ed9d1](https://github.com/sskorol/test-data-supplier/commit/b6b652b8f4ed9d1) Sergey Korol *2018-04-04 11:48:29*

**Added SPI for custom IAnnotationTransformers processing.**

 * Minor refactoring.

[b67c7846a4a7ae2](https://github.com/sskorol/test-data-supplier/commit/b67c7846a4a7ae2) Sergey Korol *2018-04-03 14:43:41*

**Update README.md**

 * 6.14.3 version is not released to Maven yet. Fixed.

[01654618531b850](https://github.com/sskorol/test-data-supplier/commit/01654618531b850) Sergey Korol *2018-02-28 13:03:38*

**Update CHANGELOG.md**

 * Added new release tag.

[b83ad3ffed8ec06](https://github.com/sskorol/test-data-supplier/commit/b83ad3ffed8ec06) Sergey Korol *2018-02-28 12:48:38*

## 1.5.0
**Replaced common lib loading mechanism with SPI to simplify installation process.**


[af75e0adf3227cf](https://github.com/sskorol/test-data-supplier/commit/af75e0adf3227cf) Sergey Korol *2018-02-28 12:36:02*

**Update CHANGELOG.md**

 * Added new release tag.

[6b98278a7fc9998](https://github.com/sskorol/test-data-supplier/commit/6b98278a7fc9998) Sergey Korol *2018-02-27 22:15:20*

## 1.4.5
**Added subtypes analysis support [TestNG-1691](https://github.com/cbeust/testng/issues/1691).**

 * Updated project dependencies, issue template and README.
 * Minor refactoring.

[c8b24e8df1c1768](https://github.com/sskorol/test-data-supplier/commit/c8b24e8df1c1768) Sergey Korol *2018-02-27 22:03:08*

**Update README.md**

 * Added listeners limitation.

[d9f518785c5fc26](https://github.com/sskorol/test-data-supplier/commit/d9f518785c5fc26) Sergey Korol *2018-02-07 13:16:48*

**Update README.md**

 * Updated TestNG and DS versions.

[d87b1f8fee8867b](https://github.com/sskorol/test-data-supplier/commit/d87b1f8fee8867b) Sergey Korol *2018-02-05 09:17:07*

**Update CHANGELOG.md**

 * Updated latest released tag.

[d2dbbec94b3d154](https://github.com/sskorol/test-data-supplier/commit/d2dbbec94b3d154) Sergey Korol *2018-02-05 09:14:35*

## 1.4.1
**Updated required dependencies for better TestNG DP on Factory level support.**


[83010a220d4dce4](https://github.com/sskorol/test-data-supplier/commit/83010a220d4dce4) Sergey Korol *2018-02-05 08:59:26*

**Updated outdated deps and gradle version.**


[baa4f1c9de24730](https://github.com/sskorol/test-data-supplier/commit/baa4f1c9de24730) Sergey Korol *2018-01-06 16:41:32*

**Updated README with actual details regarding Factory.**


[e1173678654ebb7](https://github.com/sskorol/test-data-supplier/commit/e1173678654ebb7) Sergey Korol *2017-12-19 22:10:25*

**Factory updates:**

 * Added support for missing data provider class injections into Factory meta-data.
 * Updated outdated dependencies.

[f4572604bbccb22](https://github.com/sskorol/test-data-supplier/commit/f4572604bbccb22) Sergey Korol *2017-12-19 22:07:24*

**Create PULL_REQUEST_TEMPLATE.md**

 * To simplify PRs creation process.

[e70729c706a78c0](https://github.com/sskorol/test-data-supplier/commit/e70729c706a78c0) Sergey Korol *2017-12-19 16:22:34*

**Added ISSUE_TEMPLATE**

 * To simplify issues creation process.

[3fbc9109c5a8ed1](https://github.com/sskorol/test-data-supplier/commit/3fbc9109c5a8ed1) Sergey Korol *2017-12-19 14:53:17*

**Update CHANGELOG.md**

 * Fixed version tag.

[e6f66e6f538ca5c](https://github.com/sskorol/test-data-supplier/commit/e6f66e6f538ca5c) Sergey Korol *2017-11-30 09:14:16*

## 1.4.0
**Factory support (fixes #48):**

 * Added Factory annotation processing support; note that it currently works only with explicit dataProviderClass specification (see README for details).
 * Revised transformer structure due to TestNG internals complexity.
 * ITestNGMethod injections:
 * DataSupplier now supports ITestNGMethod injection into signature (that was required for correct Factory events processing).
 * DataSupplierInterceptor now supplies ITestNGMethod instead of a Method.
 * Dependencies:
 * Updated Gradle and internal libraries&#39; versions.

[2328a41f6f7d173](https://github.com/sskorol/test-data-supplier/commit/2328a41f6f7d173) Sergey Korol *2017-11-30 08:35:00*

**Minor refactoring and improvements on core level.**


[8af19c7a4482c1f](https://github.com/sskorol/test-data-supplier/commit/8af19c7a4482c1f) Sergey Korol *2017-11-30 08:59:55*

**Core refactoring:**

 * Methods, variables renaming and splitting to make code more obvious and readable.

[b813c8e1c6810b5](https://github.com/sskorol/test-data-supplier/commit/b813c8e1c6810b5) Sergey Korol *2017-11-04 19:35:28*

**Update CHANGELOG.md**

 * New release version update.

[e7d8820614e0bc3](https://github.com/sskorol/test-data-supplier/commit/e7d8820614e0bc3) Sergey Korol *2017-11-03 20:47:37*

## 1.3.0
**Indices support:**

 * Added indices[] arg on annotation level to make DataSupplier fully similar to DataProvider.

[8580acbee18e42e](https://github.com/sskorol/test-data-supplier/commit/8580acbee18e42e) Sergey Korol *2017-11-03 20:31:45*

**Update CHANGELOG.md**

 * Increased released version.

[b4cf26874961e03](https://github.com/sskorol/test-data-supplier/commit/b4cf26874961e03) Sergey Korol *2017-11-02 16:17:08*

## 1.2.5
**Class level annotations support:**

 * Fixed NPEs with class level Test annotation usage. TestNG sends nulls to transformer in this case.
 * Added a possibility to specify DataSupplier on class level Test annotation. In this case it&#39;ll be applied to all public methods, which don&#39;t have Test annotations.
 * Minor improvements:
 * Updated Gradle and TestNG versions.
 * Added new details regarding running from IDE via TestNG run config into README.

[c658bef37e6283c](https://github.com/sskorol/test-data-supplier/commit/c658bef37e6283c) Sergey Korol *2017-11-02 15:57:50*

**Migrated to new AspectJ version to avoid version eye warnings.**


[e35144cede6be21](https://github.com/sskorol/test-data-supplier/commit/e35144cede6be21) Sergey Korol *2017-10-13 06:54:51*

**Dependencies update:**

 * - vavr version had an outdated version which caused warnings on versioneye.
 * - revised tuple transformation test;

[81e086661723bba](https://github.com/sskorol/test-data-supplier/commit/81e086661723bba) Sergey Korol *2017-10-02 07:46:29*

**Update CHANGELOG.md**


[12540d978710d0b](https://github.com/sskorol/test-data-supplier/commit/12540d978710d0b) Sergey Korol *2017-09-30 20:51:39*

## 1.2.0
**New features:**

 * added Map / Entry return types support;
 * Map should be transposed into list of Entry;
 * added new flatMap operation support for collections / tuple / streams / entries / map;

[ad93692c88f02da](https://github.com/sskorol/test-data-supplier/commit/ad93692c88f02da) Sergey Korol *2017-09-30 20:27:56*

**Gradle update:**

 * wrapper migration to 4.1 version;

[11d7a2a6a046cbb](https://github.com/sskorol/test-data-supplier/commit/11d7a2a6a046cbb) Sergey Korol *2017-08-11 07:55:23*

**Fixed release version**


[47df2a94dfa2266](https://github.com/sskorol/test-data-supplier/commit/47df2a94dfa2266) Sergey Korol *2017-08-03 18:52:50*

## 1.1.0
**Jacoco xml support:**

 * added xml report generation for online checks;

[362cdaa068239ee](https://github.com/sskorol/test-data-supplier/commit/362cdaa068239ee) Sergey Korol *2017-08-03 18:38:38*

**fixed travis config due to jacoco refactoring;**


[4698cdf5bcebaf1](https://github.com/sskorol/test-data-supplier/commit/4698cdf5bcebaf1) Sergey Korol *2017-08-03 18:10:22*

**DataSupplier interceptor:**

 * added SPI loading helper;
 * added DataSupplier interceptor for tacking useful meta-data;
 * revised key structures for better support in future;
 * updated gradle version, revised jacoco configuration to live with aspectj without instrumentation conflicts;

[0fcd4f35cbe1672](https://github.com/sskorol/test-data-supplier/commit/0fcd4f35cbe1672) Sergey Korol *2017-08-03 18:06:21*

**Added IDEA plugin support documentation.**


[95a48ddf0fb5a29](https://github.com/sskorol/test-data-supplier/commit/95a48ddf0fb5a29) Sergey Korol *2017-07-28 16:04:52*

**Update CHANGELOG.md**

 * Updated release version

[aeeebd6dc5b9e21](https://github.com/sskorol/test-data-supplier/commit/aeeebd6dc5b9e21) Sergey Korol *2017-07-12 19:51:12*

## 1.0.0
**New features:**

 * added Tuple return type support from vavr library to extend flexibility;
 * added runInParallel feature as an alternative to common parallel flag;

[30c94e775d5078f](https://github.com/sskorol/test-data-supplier/commit/30c94e775d5078f) Sergey Korol *2017-07-12 19:37:32*

**Update CHANGELOG.md**

 * Replaced confusing Unversioned tag with actual 0.9.0

[fab62c5a35c3f9d](https://github.com/sskorol/test-data-supplier/commit/fab62c5a35c3f9d) Sergey Korol *2017-07-08 23:19:32*

## 0.9.0
**Dependencies update:**

 * assertj core was outdated and caused version eye warnings;

[7de84ff77c8075a](https://github.com/sskorol/test-data-supplier/commit/7de84ff77c8075a) Sergey Korol *2017-07-08 23:00:41*

**Core updates:**

 * added custom DataSupplier name specification support;
 * increased coverage;
 * revised reflection utils to support custom names;
 * minor refactoring;

[103998744c9ef66](https://github.com/sskorol/test-data-supplier/commit/103998744c9ef66) Sergey Korol *2017-07-08 22:46:51*

**Changelog updates:**

 * added release dependency;
 * moved template to gradle folder;

[a158dffeb4d8d7b](https://github.com/sskorol/test-data-supplier/commit/a158dffeb4d8d7b) Sergey Korol *2017-07-07 11:22:25*

**removed redundant items from template**


[3242e0d3ccf2622](https://github.com/sskorol/test-data-supplier/commit/3242e0d3ccf2622) Sergey Korol *2017-07-07 11:10:45*

**- fixed template markdown**


[6ee781245f79986](https://github.com/sskorol/test-data-supplier/commit/6ee781245f79986) Sergey Korol *2017-07-07 11:03:25*

**- added changelog template and generator plugin;**


[98c07f9300ebbb4](https://github.com/sskorol/test-data-supplier/commit/98c07f9300ebbb4) Sergey Korol *2017-07-07 10:54:02*

## 0.8.0
**- added missing badges;**

 * - prepared new version;

[fb1b463b2ebe0b6](https://github.com/sskorol/test-data-supplier/commit/fb1b463b2ebe0b6) Sergey Korol *2017-07-07 09:55:13*

**- added Travis CI support;**

 * - added jacoco support;
 * - extended test coverage;

[0c65fc8385acda4](https://github.com/sskorol/test-data-supplier/commit/0c65fc8385acda4) Sergey Korol *2017-07-07 09:52:31*

**Update README.md**

 * Replaced flaky bintray badge

[dde3083e802f164](https://github.com/sskorol/test-data-supplier/commit/dde3083e802f164) Sergey Korol *2017-07-06 12:35:29*

**Wrong env for twitter access token**


[989b1b99dacca57](https://github.com/sskorol/test-data-supplier/commit/989b1b99dacca57) Sergey Korol *2017-07-05 21:27:41*

**Update README.md**

 * Fixed version to the latest one.

[4ee32ea25ccb679](https://github.com/sskorol/test-data-supplier/commit/4ee32ea25ccb679) Sergey Korol *2017-07-05 21:14:29*

## 0.7.2
**- fixed tweet dependency**


[1204f8a8b8b9f93](https://github.com/sskorol/test-data-supplier/commit/1204f8a8b8b9f93) Sergey Korol *2017-07-05 21:11:06*

## 0.7.1
**- updated lib versions**


[3efbeb5556cdd98](https://github.com/sskorol/test-data-supplier/commit/3efbeb5556cdd98) Sergey Korol *2017-07-05 20:55:30*

**- added twitter api support for automatic notifications on release**


[d80ad4f28003a3a](https://github.com/sskorol/test-data-supplier/commit/d80ad4f28003a3a) Sergey Korol *2017-07-05 20:54:05*

**Update README.md**

 * Fixed twitter link

[9ad949c8dea0814](https://github.com/sskorol/test-data-supplier/commit/9ad949c8dea0814) Sergey Korol *2017-07-05 19:51:39*

**Update README.md**

 * Revised naming

[25526b1c658d51b](https://github.com/sskorol/test-data-supplier/commit/25526b1c658d51b) Sergey Korol *2017-07-05 19:46:09*

**Update README.md**

 * Badges made more generic

[36d2ec20011b697](https://github.com/sskorol/test-data-supplier/commit/36d2ec20011b697) Sergey Korol *2017-07-05 19:42:14*

**Update README.md**

 * Added more flexible maven search url

[1c6cb37c6bb7d32](https://github.com/sskorol/test-data-supplier/commit/1c6cb37c6bb7d32) Sergey Korol *2017-07-05 12:44:59*

**Update README.md**

 * Updated badges

[dd93887fecd85a0](https://github.com/sskorol/test-data-supplier/commit/dd93887fecd85a0) Sergey Korol *2017-07-05 10:38:18*

## 0.7.0
**- moved transformation logic into separate entity to increase readability;**

 * - added reflection utils to split corresponding staff from listener logic;
 * - added tests for missing DS;
 * - prepared README for new release;

[70f6768b518c38d](https://github.com/sskorol/test-data-supplier/commit/70f6768b518c38d) Sergey Korol *2017-07-05 10:17:27*

**Update README.md**

 * Added social badge

[dcc5a63a87e8353](https://github.com/sskorol/test-data-supplier/commit/dcc5a63a87e8353) Sergey Korol *2017-07-04 13:06:03*

**Update README.md**

 * Added badges

[cb1274b77aed249](https://github.com/sskorol/test-data-supplier/commit/cb1274b77aed249) Sergey Korol *2017-07-04 09:16:49*

**- added examples links**


[afa70db53879683](https://github.com/sskorol/test-data-supplier/commit/afa70db53879683) Sergey Korol *2017-07-04 08:10:47*

**- optimized args mess while passing test meta data across helper methods**


[084aaafdea16805](https://github.com/sskorol/test-data-supplier/commit/084aaafdea16805) Sergey Korol *2017-07-03 15:06:08*

**- updated README with maven / gradle integration details**


[c3aeb0453410918](https://github.com/sskorol/test-data-supplier/commit/c3aeb0453410918) Sergey Korol *2017-07-03 14:21:51*

## 0.6.0
**- fixed checkstyle errors**


[b7a947650771323](https://github.com/sskorol/test-data-supplier/commit/b7a947650771323) Sergey Korol *2017-07-03 13:35:44*

**- added ITestContext / Method injections support (similar to common DP);**

 * - added tests for new features coverage;
 * - minor improvements;

[b4ff2f775cddba0](https://github.com/sskorol/test-data-supplier/commit/b4ff2f775cddba0) Sergey Korol *2017-07-03 13:27:49*

## 0.5.6
**- updated README with latest release version**


[e8652626aed9bf3](https://github.com/sskorol/test-data-supplier/commit/e8652626aed9bf3) Sergey Korol *2017-07-03 09:49:42*

**- updated README with latest release version**


[c0169e93c98f67f](https://github.com/sskorol/test-data-supplier/commit/c0169e93c98f67f) Sergey Korol *2017-07-03 09:43:57*

## 0.5.5
**- added GPG signing and simple Javadocs**


[8ba2b8942e6a856](https://github.com/sskorol/test-data-supplier/commit/8ba2b8942e6a856) Sergey Korol *2017-07-03 09:20:38*

**Create CODE_OF_CONDUCT.md**


[7fbc385a901f05f](https://github.com/sskorol/test-data-supplier/commit/7fbc385a901f05f) Sergey Korol *2017-07-01 20:00:34*

**- updated collection tests with more interesting example of injecting array into test signature**


[b905f24dcc7f35a](https://github.com/sskorol/test-data-supplier/commit/b905f24dcc7f35a) Sergey Korol *2017-07-01 17:16:41*

**Update README.md**


[c7502a7d26300e1](https://github.com/sskorol/test-data-supplier/commit/c7502a7d26300e1) Sergey Korol *2017-07-01 16:44:01*

## 0.5.2
**- added maven publisher**


[e8b5758e0428c28](https://github.com/sskorol/test-data-supplier/commit/e8b5758e0428c28) Sergey Korol *2017-07-01 16:35:08*

**- replaced common version with a snapshot**


[900849664705d69](https://github.com/sskorol/test-data-supplier/commit/900849664705d69) Sergey Korol *2017-07-01 16:26:47*

## 0.5.1
**- minor fixes**


[f22f3b2961436ac](https://github.com/sskorol/test-data-supplier/commit/f22f3b2961436ac) Sergey Korol *2017-07-01 16:20:46*

**- updated bintray repo and readme with actual info**


[e58b5517862ffd0](https://github.com/sskorol/test-data-supplier/commit/e58b5517862ffd0) Sergey Korol *2017-07-01 16:00:20*

**- initial commit of extended TestNG DataProvider**


[40278b2738393a4](https://github.com/sskorol/test-data-supplier/commit/40278b2738393a4) Sergey Korol *2017-07-01 15:49:50*

