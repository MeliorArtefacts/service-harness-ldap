# Melior Service Harness :: LDAP
<div style="display: inline-block;">
<img src="https://img.shields.io/badge/version-2.3-green?style=for-the-badge"/>
<img src="https://img.shields.io/badge/production-ready-green?style=for-the-badge"/>
<img src="https://img.shields.io/badge/compatibility-spring_boot_2.4.5-green?style=for-the-badge"/>
</div>

## Artefact
Get the artefact and the POM file in the *artefact* folder.
```
<dependency>
    <groupId>org.melior</groupId>
    <artifactId>melior-harness-ldap</artifactId>
    <version>2.3</version>
</dependency>
```

## Client
Create a bean to instantiate the LDAP client.  The LDAP client uses connection pooling to improve performance.
```
@Bean("myclient")
@ConfigurationProperties("myclient")
public LdapClient client() {
    return LdapClientBuilder.create().ssl().build();
}
```

The LDAP client is auto-configured from the application properties.
```
myclient.url=ldaps://some.service:636
myclient.username=user
myclient.password=password
myclient.request-timeout=30
myclient.inactivity-timeout=300
```

Map the attributes in the LDAP repository to the fields in the JAVA class.
```
public class Person {
    @LdapProperty("name")
    private String name;

    @LdapProperty("postaladdress")
    private String postalAddress;

    @LdapProperty("age")
    private int age;

    ...
}

```

Wire in and use the LDAP client.  Use the LDAP object mapper to request only those attributes that are required by the mapping.
```
@Autowired
private LdapClient client;

public List<Person> foo(String name) throws RemotingException {
    return curClient.search(LdapQueryBuilder.query().base("ou=person,o=company")
        .attributes(LdapObjectMapper.getAttributes(Person.class))
        .where("name").is(name), Person.class);
}
```

The LDAP client may be configured using these application properties.

|Name|Default|Description|
|:---|:---|:---|
|`url`||The URL of the LDAP repository|
|`username`||The user name required by the LDAP repository|
|`password`||The password required by the LDAP repository|
|`minimum-connections`|0|The minimum number of connections to open to the LDAP repository|
|`maximum-connections`|1000|The maximum number of connections to open to the LDAP repository|
|`connection-timeout`|30 s|The amount of time to allow for a new connection to open to the LDAP repository|
|`validate-on-borrow`|false|Indicates if a connection must be validated when it is borrowed from the JDBC connection pool|
|`validation-timeout`|5 s|The amount of time to allow for a connection to be validated|
|`request-timeout`|60 s|The amount of time to allow for a request to the LDAP repository to complete|
|`backoff-period`|1 s|The amount of time to back off when the circuit breaker trips|
|`backoff-multiplier`|1|The factor with which to increase the backoff period when the circuit breaker trips repeatedly|
|`backoff-limit`||The maximum amount of time to back off when the circuit breaker trips repeatedly|
|`inactivity-timeout`|300 s|The amount of time to allow before surplus connections to the LDAP repository are pruned|
|`maximum-lifetime`|unlimited|The maximum lifetime of a connection to the LDAP repository|
|`prune-interval`|60 s|The interval at which surplus connections to the LDAP repository are pruned|

&nbsp;  
## References
Refer to the [**Melior Service Harness :: Core**](https://github.com/MeliorArtefacts/service-harness-core) module for detail on the Melior logging system and available utilities.
