/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.ldap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;
import org.melior.client.ssl.ClientSSLContext;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class LdapSocketFactory extends SSLSocketFactory{
    private static SSLSocketFactory delegate = ClientSSLContext.ofLenient("TLS").getSocketFactory();

  /**
   * Constructor.
   * @throws RuntimeException if unable to initialize the socket factory
   */
  private LdapSocketFactory(){
        super();
  }

  /**
   * Get default socket factory.
   * @return The socket factory
   */
  public static SSLSocketFactory getDefault(){
    return new LdapSocketFactory();
  }

  /**
   * Get default cipher suites.
   * @return The default cipher suites
   */
  public String[] getDefaultCipherSuites(){
    return delegate.getDefaultCipherSuites();
  }

  /**
   * Get supported cipher suites.
   * @return The supported cipher suites
   */
  public String[] getSupportedCipherSuites(){
    return delegate.getSupportedCipherSuites();
  }

  /**
   * Create socket.
   * @return The socket
   */
  public Socket createSocket() throws IOException{
    return delegate.createSocket();
  }

  /**
   * Create socket.
   * @return The socket
   */
  public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException{
    return delegate.createSocket(arg0, arg1, arg2, arg3);
  }

  /**
   * Create socket.
   * @return The socket
   */
  public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException{
    return delegate.createSocket(arg0, arg1);
  }

  /**
   * Create socket.
   * @return The socket
   */
  public Socket createSocket(InetAddress arg0, int arg1) throws IOException{
    return delegate.createSocket(arg0, arg1);
  }

  /**
   * Create socket.
   * @return The socket
   */
  public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException{
    return delegate.createSocket(arg0, arg1, arg2, arg3);
  }

  /**
   * Create socket.
   * @return The socket
   */
  public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException{
    return delegate.createSocket(arg0, arg1, arg2, arg3);
  }

}
