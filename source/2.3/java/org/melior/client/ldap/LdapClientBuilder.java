/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.ldap;
import javax.net.ssl.SSLContext;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class LdapClientBuilder{
    private boolean ssl = false;

    private SSLContext sslContext;

  /**
   * Constructor.
   */
  private LdapClientBuilder(){
        super();
  }

  /**
   * Create LDAP client builder.
   * @return The LDAP client builder
   */
  public static LdapClientBuilder create(){
        return new LdapClientBuilder();
  }

  /**
   * Build LDAP client.
   * @return The LDAP client
   */
  public LdapClient build(){
        return new LdapClient(ssl, sslContext);
  }

  /**
   * Enable SSL.
   * @return The LDAP client builder
   */
  public LdapClientBuilder ssl(){
        this.ssl = true;

    return this;
  }

  /**
   * Set SSL context.
   * @param sslContext The SSL context
   * @return The LDAP client builder
   */
  public LdapClientBuilder sslContext(
    final SSLContext sslContext){
        this.sslContext = sslContext;

    return this;
  }

}
