![slack](https://img.shields.io/badge/slack-Aserto%20Community-brightgreen)

# aserto-spring
Aserto authorization filter for Spring Security.
`aserto-spring` impements middleware for Spring Security that uses the [Aserto](https://aserto.com) authorizer.

The package adds a spring security filter that intercepts requests. Once a request is intercepted an authroization call
is made to the Aserto Authorizer. Is the request is authorized it is allowed to continue, otherwise a 401 is returned.

Built on top of the [Aserto Java SDK](https://github.com/aserto-dev/aserto-java)

## Prerequisites
- Java 17  or newer
- Spring Boot 3.1.5 or newer
- Spring Security 6.1.5 or newer

## Building

```mvn clean install```

Add the middleware to your project

```xml
<dependency>
    <groupId>com.aserto</groupId>
    <artifactId>aserto-spring</artifactId>
</dependency>
```

## Configuration
The following configuration settings are required for authorization:

### Topaz
- aserto.authorizer.serviceUrl
- aserto.authorizer.insecure
- aserto.authorizer.policyRoot
- aserto.authorizer.grpc.caCertPath
- aserto.authorizer.policyName
- aserto.authorizer.policyLabel
- aserto.authorizer.token
- aserto.authorizer.decision


### Aserto
- aserto.authorizer.serviceUrl
- aserto.authorizer.policyRoot
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

and configure the security filter chain to use the middleware.

```java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().access(new AsertoAuthorizationManager(authzCfg))
        );
        return http.build();
    }
```
### Middleware defaults
By default, the middleware extracts
- the policy path is extracted from the rest controller mappings e.g. `@GetMapping("/users/{userID}")` will generate the policy path `<policy_path>.GET.users.__userID`
- the resource context is not included by default in the authorization call. 
The middleware does not extract the identity by default. You can easily configure an identity mapper by using one of the ones we provided or by creating your own.
e.g.
```java
    @Bean
    public IdentityMapper identityMapper() {
        Extractor hostNameExtractor = new HeaderExtractor("authorization");
        return new JwtIdentityMapper(hostNameExtractor);
    }
```

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

## Configuring the middleware for check calls
The check call is a specialized is call. It allows us to specify an object type, an object id and a relation.
e.g.

```java
    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, "/todos")
            .access(new CheckConfig(authzCfg, "group", "viewer", "member").getAuthManager())
        );
    return http.build();
}
```

### Method level authorization
The check call can be used at a method level as well.
e.g.
```java
    @GetMapping("/todos")
    @PreAuthorize("@aserto.check('group', 'viewer', 'member')")
    public String getTodo() {
        return "Hello from route GET /todos";
    }
```
The check call accept hard coded values or implementations of the [ObjectTypeMapper.java](src%2Fmain%2Fjava%2Fcom%2Faserto%2Fauthroizer%2Fmapper%2Fobject%2FObjectTypeMapper.java),
[ObjectTypeMapper.java](src%2Fmain%2Fjava%2Fcom%2Faserto%2Fauthroizer%2Fmapper%2Fobject%2FObjectTypeMapper.java) and [RelationMapper.java](src%2Fmain%2Fjava%2Fcom%2Faserto%2Fauthroizer%2Fmapper%2Frelation%2FRelationMapper.java)
interfaces

## Example

An example can be found in the [example](https://github.com/aserto-dev/aserto-spring/tree/main/examples) directory.