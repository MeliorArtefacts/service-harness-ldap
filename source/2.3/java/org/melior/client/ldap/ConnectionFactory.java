/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.ldap;
import java.util.HashMap;
import java.util.Map;
import javax.naming.directory.DirContext;
import javax.net.ssl.SSLContext;
import org.melior.client.exception.RemotingException;
import org.melior.client.pool.ConnectionPool;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class ConnectionFactory implements org.melior.client.core.ConnectionFactory<LdapClientConfig, Connection, DirContext>{
    private LdapContextSource ldapContextSource;

  /**
   * Constructor.
   * @param ssl The SSL indicator
   * @param sslContext The SSL context
   * @param configuration The client configuration
   * @throws RemotingException if unable to initialize the connection factory
   */
  public ConnectionFactory(
    final boolean ssl,
    final SSLContext sslContext,
    final LdapClientConfig configuration) throws RemotingException{
        super();

        Map<String, Object> properties;

        if (ssl == true){
            properties = new HashMap<String, Object>();
      properties.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(configuration.getConnectionTimeout()));
      properties.put("java.naming.ldap.factory.socket", LdapSocketFactory.class.getName());

            ldapContextSource = new LdapContextSource();
      ldapContextSource.setUrl(configuration.getUrl());
      ldapContextSource.setUserDn(configuration.getUsername());
      ldapContextSource.setPassword(configuration.getPassword());
      ldapContextSource.setPooled(false);
      ldapContextSource.setBaseEnvironmentProperties(properties);
      ldapContextSource.setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy());
      ldapContextSource.afterPropertiesSet();
    }
    else{
            properties = new HashMap<String, Object>();
      properties.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(configuration.getConnectionTimeout()));

            ldapContextSource = new LdapContextSource();
      ldapContextSource.setUrl(configuration.getUrl());
      ldapContextSource.setUserDn(configuration.getUsername());
      ldapContextSource.setPassword(configuration.getPassword());
      ldapContextSource.setPooled(false);
      ldapContextSource.setBaseEnvironmentProperties(properties);
      ldapContextSource.afterPropertiesSet();
    }

  }

  /**
   * Create a new connection.
   * @param configuration The client configuration
   * @param connectionPool The connection pool
   * @return The new connection
   * @throws RemotingException if unable to create a new connection
   */
  public Connection createConnection(
    final LdapClientConfig configuration,
    final ConnectionPool<LdapClientConfig, Connection, DirContext> connectionPool) throws RemotingException{
        Connection connection;

        connection = new Connection(configuration, connectionPool, ldapContextSource);
    connection.open();

    return connection;
  }

  /**
   * Destroy the connection.
   * @param connection The connection
   */
  public void destroyConnection(
    final Connection connection){
        connection.close();
  }

}
