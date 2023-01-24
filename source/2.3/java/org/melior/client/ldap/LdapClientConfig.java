/* __  __      _ _            
  |  \/  |    | (_)           
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
        Service Harness
*/
package org.melior.client.ldap;
import org.melior.client.core.ClientConfig;

/**
 * Configuration parameters for a {@code LdapClient}, with defaults.
 * @author Melior
 * @since 2.3
 */
public class LdapClientConfig extends ClientConfig {

    /**
     * Constructor.
     */
    protected LdapClientConfig() {

        super();
    }

    /**
     * Configure client.
     * @param clientConfig The new client configuration parameters
     * @return The client configuration parameters
     */
    public LdapClientConfig configure(
        final LdapClientConfig clientConfig) {
        super.configure(clientConfig);

        return this;
    }

}
