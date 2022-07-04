/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.ldap;
import javax.naming.directory.DirContext;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.UncategorizedLdapException;
import org.springframework.ldap.core.ContextSource;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class ConnectionManager extends org.melior.client.pool.ConnectionManager<LdapClientConfig, Connection, DirContext> implements ContextSource{

  /**
   * Constructor.
   * @param configuration The client configuration
   * @param connectionFactory The connection factory
   */
  public ConnectionManager(
    final LdapClientConfig configuration,
    final ConnectionFactory connectionFactory){
        super(configuration, connectionFactory);
  }

  /**
   * Get context.
   * @param principal The principal
   * @param credentials The credentials
   * @return The context
   * @throws NamingException if unable to create a context
   */
  public DirContext getContext(
    final String principal,
    final String credentials) throws NamingException{

    try{
            return getConnection();
    }
    catch (Exception exception){
      throw new UncategorizedLdapException(exception.getMessage());
    }

  }

  public DirContext getReadOnlyContext() throws NamingException{
    return getContext(null, null);
  }

  public DirContext getReadWriteContext() throws NamingException{
    return getContext(null, null);
  }

}
