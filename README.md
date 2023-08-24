![slack](https://img.shields.io/badge/slack-Aserto%20Community-brightgreen)

# aserto-spring
Aserto authorization filter for Spring Security.
`aserto-spring` impements middleware for Spring Security that uses the [Aserto](https://aserto.com) authorizer.

The package adds a spring security filter that intercepts requests. Once a request is intercepted an authroization call
is made to the Aserto Authorizer. Is the request is authorized it is allowed to continue, otherwise a 401 is returned.

Built on top of the [Aserto Java SDK](https://github.com/aserto-dev/aserto-java)

## Prerequisites
- Java 8  or newer
- Spring Boot 2.7.x
- Spring Security 5.7.x

## Building

```mvn clean install```

Add the middleware to your project

```xml
<dependency>
    <groupId>com.aserto</groupId>
    <artifactId>aserto-spring</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Configuration
The following configuration settings are required for authorization:

### Topaz
- aserto.authorizer.host
- aserto.authorizer.port
- aserto.authorizer.insecure
- aserto.authorizer.grpc.caCertPath
- aserto.authorizer.policyName
- aserto.authorizer.policyLabel
- aserto.authorizer.token
- aserto.authorizer.decision


### Aserto
- aserto.authorizer.host
- aserto.authorizer.port
- #aserto.authorizer.apiKey
- aserto.authorizer.policyName
- aserto.authorizer.policyLabel
- aserto.authorizer.token
- aserto.authorizer.decision

## Usage
In order to use the middleware you just need to add the annotation for component scan to your main class.
```java
@ComponentScan("com.aserto")
```

### Middleware defaults
By default the middleware extracts 
- the identity from the Authorization header  e.g. `Authorization: Bearer <JWT>`
- the policy path is extracted from the rest controller mappings e.g. `@GetMapping("/users/{userID}")` will generate a the policy path `<policy_path>.GET.users.__userID`
- the resource context is not included by default in the authorization call. 

## Customizing the middleware

You are able to change the identity mapper, policy path mapper or resource mapper.
All you have to do is provide a bean that returns an instance that implement the IdentityMapper, PolicyMapper or ResourceMapper interface.

### IdentityMapper

```java
    @Bean
    public IdentityMapper identityMapper() {
        Extractor authzHeaderExtractor = new AuthzHeaderExtractor();
        return new SubjectIdentityMapper(hostNameExtractor);
    }
```

### PolicyMapper

```java
    @Bean
    public PolicyMapper policyMapper() {
        return new CustomPolicyMapper();
    }
```

### ResourceMapper

```java
    @Bean
    public ResourceMapper resourceMapper() {
        BodyExtractor bodyExtractor = new BodyExtractor();
        return new JsonResourceMapper(bodyExtractor, new String[]{"email", "name", "aud"});
    }
```

## Example

An example can be found in the [example](https://github.com/aserto-dev/aserto-spring/tree/main/examples/spring-authz-example) directory.