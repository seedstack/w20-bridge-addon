---
title: "W20 bridge"
repo: "https://github.com/seedstack/w20-bridge-addon"
author: Adrien LAUER
description: "Acts as an extensible integration bridge between SeedStack Java and Web frameworks."
tags:
    - interfaces
    - security
zones:
    - Addons
menu:
    W20BridgeAddon:
        weight: 10
---

The SeedStack W20 bridge acts as an integration bridge between the Seed Java framework and the W20 Web framework. 
<!--more-->

You can add it with the following dependency:

{{< dependency g="org.seedstack.addons.w20" a="w20-bridge-web" >}}

If you don't want to serve W20 static files from the Java application, use the following dependency instead:

{{< dependency g="org.seedstack.addons.w20" a="w20-bridge-rest" >}}

This will only provide the REST API required by the W20 frontend to bridge. Use the default masterpage template presented below to update your own index.html page on the frontend.

# How it works

The W20 bridge automatically generates and serves W20 resources that should normally be written manually:

* The W20 masterpage is automatically generated and served under `/`. There is no need to provide an `index.html` file,
except for advanced use cases (see below).
* The W20 configuration file (`w20.app.json`) is read from the `META-INF/configuration` classpath location and a managed,
enriched version is served under `/seed-w20/application/configuration`.
* A resource for basic authentication is served under `/seed-w20/security/basic-authentication`,
* A resource for retrieving the authorizations of the authenticated subject is served under `/seed-w20/security/authorizations`.

## Automatic activation of fragments

The W20 bridge automatically detects W20 fragments (manifests ending with the `.w20.json` extension) present in the classpath
under `META-INF/resources` and enables them in the generated W20 configuration.

{{% callout warning %}}
Note that the W20 bridge cannot detect fragments located outside the local classpath, like the ones in the document root
or external to the application. Those fragments must still be explicitly specified in the W20 configuration.
{{% /callout %}}

## Fragment variables

The W20 bridge provides several fragments variables containing path information about the application. These variables
can be used as `${variable-name[:default-value]}` placeholders in the fragment manifests:

* `seed-base-path`: the application base path without a trailing slash,
* `seed-base-path-slash`: the application base path with a trailing slash,
* `seed-rest-path`: the path under which the REST resources are served (without a trailing slash),
* `seed-rest-path-slash`: the path under which the REST resources are served (with a trailing slash),
* `components-path`: the path under which the Web components are served (without a trailing slash),
* `components-path-slash`: the path under which the Web components are served (with a trailing slash).

## Automatic configuration

Several aspects of the configuration are automatically managed:

* The application identifier (`w20-core` -> `application` -> `id`) is automatically set with the same value as the backend
application identifier,
* The environment type (`w20-core` -> `env` -> `type`) is automatically set to the `org.seedstack.w20.environment` property
if any.
* Several elements in the masterpage are automatically derived from the bridge configuration values. See the [configuration
section below](#configuration) for more details.

{{% callout info %}}
The {{< java "org.seedstack.w20.spi.FragmentConfigurationHandler" >}} interface can be implemented to further enrich or
override the generated configuration. As an example, it is used by the [i18n add-on]({{< ref "addons/i18n/index.md" >}}) for automatically managing
the frontend `culture` module if backend internationalization is active.
{{% /callout %}}

# Pretty URLS

By default AngularJS HTML5 mode is enabled when using the W20 bridge.  Its allows pretty URLs to be used instead of historic hashbang URLs (#!). To achieve this, a servlet filter is automatically placed at the root of the application: it redirects any URL that doesn't exist on the server to the masterpage, so the W20 frontend can load and AngularJS can then display the corresponding view. 

{{% callout warning %}}
The HTML5 redirect filter tries its best to avoid redirecting legitimate 404 or special cases like WebSocket upgrades. To ensure that your REST API calls returning legitimate 404 are not redirected by the filter, you must place the API on its own base path (like `/api`). See the [REST manual page]({{< ref "docs/seed/manual/rest.md#base-prefix" >}}) to learn how to do so.
{{% /callout %}}

Sometimes it is desirable to revert to hashbang URLs. You can do so with the following configuration:

```ini
[org.seedstack.w20]
pretty-urls = false
```

# Configuration

The behavior of W20 bridge can be altered with several backend configuration properties, described below.

## Application title

You can set the W20 application title with the following option:

```ini
[org.seedstack.w20]
application.title = My application
```

The default value is set to the Seed application name (coming from the `org.seedstack.seed.core.application-name`).

## Application subtitle

You can set the W20 application subtitle with the following option:

```ini
[org.seedstack.w20]
application.subtitle = A great application
```

There is no default value.

## Application version

You can set the W20 application version with the following option:

```ini
[org.seedstack.w20]
application.version = 1.2.3
```

The version is treated as a string so there is no restriction format. The default value is set to the Seed application
version (coming from the `org.seedstack.seed.core.application-version`). It is not recommended to change this default
value, other than for testing purposes or special cases. The version string is appended to all assets URLs by the
W20 loader to ensure that resources are refreshed when the version change.

## Loading timeout

The W20 loader has a predefined time limit to load all the application assets. Although the default value of 30 seconds
should be enough for all applications, tt is sometimes desirable to increase it with the following option:

```ini
[org.seedstack.w20]
timeout = 60
```

## CORS with credentials

To allow the application to access **secured** resources from other domains than its own, use the following option:

```ini
[org.seedstack.w20]
cors-with-credentials = true
```

This option is not necessary when accessing its own resources or publicly accessible cross-origin resources only.

# Masterpage customization

## Disable the masterpage

The generation of the masterpage can be completely disabled with the following configuration:

```ini
[org.seedstack.w20]
disable-masterpage = true
```

## Custom masterpage template

Each theme provides its own general-purpose masterpage template. If no theme is used or the theme doesn't contain a masterpage, the W20 bridge will fallback to a default masterpage with minimal body content. You can override the masterpage template by specifying its classpath path in the following configuration property:

```ini
[org.seedstack.w20]
masterpage-template = path/to/masterpage-template.html
```

{{% callout tips %}}
Masterpage templates can use `${}` placeholders for some configuration-dependant values. The following variables are available:

* `applicationTitle`,
* `applicationSubtitle`,
* `applicationVersion`,
* `timeout`,
* `corsWithCredentials`,
* `basePath`,
* `basePathSlash`,
* `restPath`,
* `restPathSlash`,
* `componentsPath,`,
* `componentsPathSlash`.
{{% /callout %}}

Below, you can find the fallback masterpage template that can be used as a starting point for your own custom templates:

```html
<!doctype html>
<html data-w20-app="${restPathSlash}seed-w20/application/configuration" data-w20-app-version="${applicationVersion}" data-w20-timeout="${timeout}" data-w20-cors-with-credentials="${corsWithCredentials}">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <title>${applicationTitle}</title>
    <script type="text/javascript" data-main="${componentsPathSlash}w20/modules/w20.js?__v=${applicationVersion}" src="${componentsPathSlash}requirejs/require.js?__v=${applicationVersion}"></script>
    <base href="${basePathSlash}">
</head>
<body>
<div id="w20-loading-cloak">
    <div class="w20-loading-indicator"></div>
</div>
<div id="w20-view" class="w20-content" data-ng-view></div>
<div data-w20-error-report></div>
</body>
</html>
```

