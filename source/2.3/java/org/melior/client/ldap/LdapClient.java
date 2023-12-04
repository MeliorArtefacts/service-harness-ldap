/* __  __      _ _            
  |  \/  |    | (_)           
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
        Service Harness
*/
package org.melior.client.ldap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import org.melior.client.exception.RemotingException;
import org.melior.logging.core.Logger;
import org.melior.logging.core.LoggerFactory;
import org.melior.service.exception.ExceptionType;
import org.melior.util.time.Timer;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.util.StringUtils;

/**
 * Implements an easy to use, auto-configuring LDAP client with connection
 * pooling, configurable backoff strategy and automatic object mapping.
 * <p>
 * The client writes timing details to the logs while dispatching LDAP requests
 * to the LDAP server.  The client automatically converts any exception that
 * occurs during communication with the LDAP server into a standard
 * {@code RemotingException}.
 * @author Melior
 * @since 2.3
 */
public class LdapClient extends LdapClientConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean ssl;

    private SSLContext sslContext;

    private LdapTemplate ldapTemplate;

    /**
     * Constructor.
     * @param ssl The SSL indicator
     * @param sslContext The SSL context
     */
    LdapClient(
        final boolean ssl,
        final SSLContext sslContext) {

        super();

        this.ssl = ssl;

        this.sslContext = sslContext;
    }

    /**
     * Configure client.
     * @param clientConfig The new client configuration parameters
     * @return The LDAP client
     */
    public LdapClient configure(
        final LdapClientConfig clientConfig) {
        super.configure(clientConfig);

        return this;
    }

    /**
     * Initialize client.
     * @throws RemotingException if unable to initialize the client
     */
    private void initialize() throws RemotingException {

        ConnectionManager connectionManager;

        if (ldapTemplate != null) {
            return;
        }

        if (StringUtils.hasLength(getUrl()) == false) {
            throw new RemotingException(ExceptionType.LOCAL_APPLICATION, "URL must be configured.");
        }

        if (StringUtils.hasLength(getUsername()) == false) {
            throw new RemotingException(ExceptionType.LOCAL_APPLICATION, "User name must be configured.");
        }

        if (StringUtils.hasLength(getPassword()) == false) {
            throw new RemotingException(ExceptionType.LOCAL_APPLICATION, "Password must be configured.");
        }

        connectionManager = new ConnectionManager(this, new ConnectionFactory(ssl, sslContext, this));

        ldapTemplate = new LdapTemplate();
        ldapTemplate.setContextSource(connectionManager);
        ldapTemplate.setDefaultTimeLimit(getRequestTimeout());
    }

    /**
     * Perform search.
     * @param <T> The type
     * @param query The LDAP query
     * @param entityType The response entity type
     * @return The search result
     * @throws RemotingException if unable to perform the search
     */
    public <T> List<T> search(
        final LdapQuery query,
        final Class<T> entityType) throws RemotingException {

        String methodName = "search";
        Timer timer;
        List<T> result;
        long duration;

        initialize();

        logger.debug(methodName, "Perform search in LDAP repository.  base = ", query.base(), ", filter = ", query.filter());

        timer = Timer.ofNanos().start();

        try {

            result = ldapTemplate.search(query, LdapObjectMapper.of(entityType));

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Search completed successfully.  Duration = ", duration, " ms.");
        }
        catch (RuntimeException exception) {

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Search failed.  Duration = ", duration, " ms.");

            throw new RemotingException(ExceptionType.REMOTING_COMMUNICATION, exception.getMessage(), exception);
        }
        catch (Exception exception) {

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Search failed.  Duration = ", duration, " ms.");

            throw new RemotingException(ExceptionType.REMOTING_COMMUNICATION, "Failed to perform search: " + exception.getMessage(), exception);
        }

        return result;
    }

    /**
     * Perform modify.
     * @param <T> The type
     * @param query The LDAP query
     * @param instance The instance that provides the new attributes
     * @throws RemotingException if unable to perform the modify
     */
    public <T> void modify(
        final LdapQuery query,
        final T instance) throws RemotingException {

        String methodName = "modify";
        Timer timer;
        DirContextOperations context;
        long duration;

        initialize();

        logger.debug(methodName, "Perform modify in LDAP repository.  base = ", query.base(), ", filter = ", query.filter());

        timer = Timer.ofNanos().start();

        try {

            context = ldapTemplate.searchForContext(query);

            LdapObjectMapper.of(instance.getClass()).mapFromInstance(context, instance);

            ldapTemplate.modifyAttributes(context);

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Modify completed successfully.  Duration = ", duration, " ms.");
        }
        catch (RuntimeException exception) {

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Modify failed.  Duration = ", duration, " ms.");

            throw new RemotingException(ExceptionType.REMOTING_COMMUNICATION, exception.getMessage(), exception);
        }
        catch (Exception exception) {

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Modify failed.  Duration = ", duration, " ms.");

            throw new RemotingException(ExceptionType.REMOTING_COMMUNICATION, "Failed to perform modify: " + exception.getMessage(), exception);
        }

    }

}
