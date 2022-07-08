/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.ldap;
import java.lang.reflect.Method;
import javax.naming.directory.DirContext;
import org.melior.client.exception.RemotingException;
import org.melior.client.pool.ConnectionPool;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class Connection extends org.melior.client.core.Connection<LdapClientConfig, Connection, DirContext>{
    private LdapContextSource ldapContextSource;

  /**
   * Constructor.
   * @param configuration The client configuration
   * @param connectionPool The connection pool
   * @param ldapContextSource The LDAP context source
   * @throws RemotingException if an error occurs during the construction
   */
  public Connection(
    final LdapClientConfig configuration,
    final ConnectionPool<LdapClientConfig, Connection, DirContext> connectionPool,
    final LdapContextSource ldapContextSource) throws RemotingException{
        super(configuration, connectionPool);

        this.ldapContextSource = ldapContextSource;
  }

  /**
   * Open raw connection.
   * @return The raw connection
   * @throws Exception if unable to open the raw connection
   */
  protected DirContext openConnection() throws Exception{
        return ldapContextSource.getContext(configuration.getUsername(), configuration.getPassword());
  }

  /**
   * Close raw connection.
   * @param connection The raw connection
   * @throws Exception if unable to close the raw connection
   */
  protected void closeConnection(
    final DirContext connection) throws Exception{
        connection.close();
  }

  /**
   * Handle proxy invocation.
   * @param object The object on which the method was invoked
   * @param method The method to invoke
   * @param args The arguments to invoke with
   * @return The result of the invocation
   * @throws Throwable when the invocation fails
   */
  public Object invoke(
    final Object object,
    final Method method,
    final Object[] args) throws Throwable{
        String methodName;
    Object invocationResult;

        methodName = method.getName();

        if (methodName.equals("close") == true){
            releaseConnection(this);

            invocationResult = null;
    }
    else{
            invocationResult = invoke(method, args);
    }

    return invocationResult;
  }

}
