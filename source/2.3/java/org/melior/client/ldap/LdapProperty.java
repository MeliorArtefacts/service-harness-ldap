/* __  __      _ _            
  |  \/  |    | (_)           
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
        Service Harness
*/
package org.melior.client.ldap;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target represents an LDAP attribute within LDAP search results.
 * @author Melior
 * @since 2.3
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LdapProperty {

    /**
     * Returns the name of the LDAP attribute as it appears in the search results.
     * @return The LDAP attribute name
     */
    String value() default "";

}
