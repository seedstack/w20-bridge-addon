# Version 3.2.8 (2018-12-14)

* [nfo] w20-business-theme: [1.3.5](https://github.com/w20-framework/w20-business-theme/releases/tag/v1.3.5)

# Version 3.2.7 (2018-12-14)

* [fix] Fix missing binding of `SeedRestPath` occurring with seed 3.8.0+ (SeedStack 18.11+).
* [chg] Built and tested with OpenJDK 11 (minimum Java version still being 8).

# Version 3.2.6 (2018-10-18)

* [fix] Make fragment variables from explicit `w20.app.json` configuration take precedence over default ones.

# Version 3.2.5 (2018-08-06)

* [nfo] w20: [2.5.1](https://github.com/w20-framework/w20/releases/tag/v2.5.1)
* [chg] In HTML5 mode, avoid redirecting requests with an extension (avoid redirecting static file requests). 

# Version 3.2.4 (2018-07-18)

* [nfo] w20-business-theme: [1.3.1](https://github.com/w20-framework/w20-business-theme/releases/tag/v1.3.1)
* [nfo] w20-simple-theme: [3.3.1](https://github.com/w20-framework/w20-simple-theme/releases/v3.3.1)

# Version 3.2.3 (2018-07-16)

* [chg] Switch to `org.seedstack.seed.Nullable` to avoid dependency to jsr305 or similar libs.
* [nfo] w20: [2.4.6](https://github.com/w20-framework/w20/releases/tag/v2.4.6)
* [nfo] w20-components: [2.3.2](https://github.com/w20-framework/w20-components/releases/tag/v2.3.2)

# Version 3.2.2 (2018-06-04)

* [nfo] w20: [2.4.1](https://github.com/w20-framework/w20/releases/tag/v2.4.1)

# Version 3.2.1 (2018-05-07)

* [fix] Fix wrong path to `w20.js` in fallback master page template.
* [fix] Provide a proper master page template for simple and material themes.

# Version 3.2.0 (2018-05-07)

* [new] Allow populating fragment variables directly from SeedStack configuration using the `w20.variables` configuration property.
* [brk] Switch from Bower to NPM package manager.
* [nfo] w20: [2.4.0](https://github.com/w20-framework/w20/releases/tag/v2.4.0)
* [nfo] w20-bootstrap-2: [2.2.0](https://github.com/w20-framework/w20-bootstrap-2/releases/tag/v2.2.0)
* [nfo] w20-bootstrap-3: [2.3.0](https://github.com/w20-framework/w20-bootstrap-3/releases/tag/v2.3.0)
* [nfo] w20-business-theme: [1.3.0](https://github.com/w20-framework/w20-business-theme/releases/tag/v1.3.0)
* [nfo] w20-components: [2.3.0](https://github.com/w20-framework/w20-components/releases/tag/v2.3.0)
* [nfo] w20-dataviz: [2.3.0](https://github.com/w20-framework/w20-dataviz/releases/tag/v2.3.0)
* [nfo] w20-extras: [2.3.0](https://github.com/w20-framework/w20-extras/releases/tag/v2.3.0)
* [nfo] w20-material: [2.2.0](https://github.com/w20-framework/w20-material/releases/tag/v2.2.0)
* [nfo] w20-material-theme: [1.1.0](https://github.com/w20-framework/w20-material-theme/releases/tag/v1.1.0)
* [nfo] w20-simple-theme: [3.3.0](https://github.com/w20-framework/w20-simple-theme/releases/v3.3.0)

# Version 3.1.0 (2017-07-31)

* [chg] Delegates security to the [web-bridge add-on](https://github.com/seedstack/web-bridge) API.

# Version 3.0.1 (2017-02-26)

* [fix] Add missing configuration and error description metadata.
* [fix] Fix transitive dependency to poms SNAPSHOT.

# Version 3.0.0 (2016-12-13)

* [brk] Update to SeedStack 16.11 new configuration system.
* [brk] W20 configuration is no longer searched in `META-INF/configuration` but at the classpath root.
* [chg] Upgrade AngularJs to 1.4.14
* [nfo] w20: [2.3.3](https://github.com/seedstack/w20/releases/tag/v2.3.3)

# Version 2.3.5 (2016-12-14)

* [fix] Allow to override routes in `w20.app.json`.
* [nfo] w20: [2.3.3](https://github.com/seedstack/w20/releases/tag/v2.3.3)
* [nfo] w20-business-theme: [1.2.3](https://github.com/seedstack/w20/releases/tag/v1.2.3)

# Version 2.3.4 (2016-09-18)

* [chg] Upgrade business theme to [1.2.2](https://github.com/seedstack/w20-business-theme/releases/tag/v1.2.2)
* [chg] Upgrade simple theme to [3.2.2](https://github.com/seedstack/w20-simple-theme/releases/tag/v3.2.2)

# Version 2.3.3 (2016-09-16)

* [chg] Upgrade business theme to [1.2.1](https://github.com/seedstack/w20-business-theme/releases/tag/v1.2.1)
* [chg] Upgrade simple theme to [3.2.1](https://github.com/seedstack/w20-simple-theme/releases/tag/v3.2.1)

# Version 2.3.2 (2016-07-28)

* [fix] Html5 redirection filter now ignores WebSocket upgrade requests and REST request if configured on a specific prefix path
* [chg] Upgrade AngularJs to 1.4.12

# Version 2.3.1 (2016-05-25)

* [nfo] w20: [2.3.1](https://github.com/seedstack/w20/releases/tag/v2.3.1)

# Version 2.3.0 (2016-04-26)

* [new] Add support for HTML5 mode (pretty urls) and enable it by default.
* [new] Add support for the `optional` and `ignore` attributes on fragment configuration.
* [new] Theme-specific masterpages with generic fallback when no theme available
* [nfo] w20: [2.3.0](https://github.com/seedstack/w20/releases/tag/v2.3.0)
* [nfo] w20-bootstrap-2: 2.1.2
* [nfo] w20-bootstrap-3: [2.2.0](https://github.com/seedstack/w20-bootstrap-3/releases/tag/v2.2.0)
* [nfo] w20-business-theme: [1.2.0](https://github.com/seedstack/w20-business-theme/releases/tag/v1.2.0)
* [nfo] w20-components: [2.2.1](https://github.com/seedstack/w20-components/releases/tag/v2.2.1)
* [nfo] w20-dataviz: [2.2.0](https://github.com/seedstack/w20-dataviz/releases/tag/v2.2.0)
* [nfo] w20-extras: [2.2.0](https://github.com/seedstack/w20-extras/releases/tag/v2.2.0)
* [nfo] w20-material: [2.1.5](https://github.com/seedstack/w20-material/releases/tag/v2.1.5)
* [nfo] w20-simple-theme: [3.2.0](https://github.com/seedstack/w20-simple-theme/releases/v3.2.0)
* [nfo] w20-material-theme: [1.0.0](https://github.com/seedstack/w20-material-theme/releases/tag/v1.0.0)

# Version 2.2.2 (2016-02-15)

* [chg] Update `w20` to [2.2.2](https://github.com/seedstack/w20/releases/tag/v2.2.2)

# Version 2.2.1 (2016-02-08)

* [chg] Update `w20` to [2.2.1](https://github.com/seedstack/w20/releases/tag/v2.2.1)

# Version 2.2.0 (2016-01-21)

* [fix] Fixed Guice error when disabling masterpage.
* [new] For ease of use, `w20-bridge-web` jar also packages the following bower dependencies in addition to W20: `angular-aria`, `angular-animate`, `angular-cookies`, `angular-messages`, `angular-mocks`, `angular-touch`
* [nfo] w20: 2.2.0
* [nfo] w20-bootstrap-2: 2.1.2
* [nfo] w20-bootstrap-3: 2.1.2
* [nfo] w20-business-theme: 1.1.3
* [nfo] w20-components: 2.2.0
* [nfo] w20-dataviz: 2.1.1
* [nfo] w20-extras: 2.1.1
* [nfo] w20-material: 2.1.3
* [nfo] w20-simple-theme: 3.1.1

# Version 2.1.1 (2015-11-25)

* [chg] Update version of w20, w20-bootstrap-2, w20-bootstrap-3, w20-components and w20-material to 2.1.1.

# Version 2.1.0 (2015-11-17)

* [chg] Refactored as an add-on and updated to work with Seed 2.1.0+

# Version 2.0.0 (2015-07-30)

* [new] Initial Open-Source release.
