/* __  __      _ _            
  |  \/  |    | (_)           
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
        Service Harness
*/
package org.melior.client.ldap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import org.springframework.ldap.UncategorizedLdapException;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Takes the attributes from an LDAP {@code SearchResult} and maps the attributes
 * to the members of a new instance of an object of the specified type.
 * <p>
 * The target members of the instance are identified with the {@code LdapProperty} annotation.
 * @author Melior
 * @since 2.3
 * @see LdapProperty
 */
public class LdapObjectMapper<T> implements AttributesMapper<T> {

    private Class<T> entityType;

    /**
     * Constructor.
     * @param entityType The entity type
     */
    public LdapObjectMapper(
        final Class<T> entityType) {

        super();

        this.entityType = entityType;
    }

    /**
     * Map LDAP attributes to an instance. The supplied attributes are the attributes
     * from a single LDAP {@code SearchResult}.
     * @param attributes The LDAP attributes
     * @return The instance populated with the attributes
     * @throws NamingException if unable to create the instance
     */
    public T mapFromAttributes(
        final Attributes attributes) throws NamingException {

        T instance = null;
        LdapProperty ldapProperty;
        Attribute attribute;

        try {

            instance = (T) entityType.getDeclaredConstructor().newInstance();

            for (Field field : entityType.getDeclaredFields()) {

                ldapProperty = field.getAnnotation(LdapProperty.class);

                if (ldapProperty != null) {

                    attribute = attributes.get(ldapProperty.value());

                    if (attribute != null) {

                        try {

                            field.setAccessible(true);
                            field.set(instance, attribute.get());
                        }
                        catch (Exception exception) {
                        }

                    }

                }

            }

            for (Method method : entityType.getDeclaredMethods()) {

                ldapProperty = method.getAnnotation(LdapProperty.class);

                if (ldapProperty != null) {

                    attribute = attributes.get(ldapProperty.value());

                    if (attribute != null) {

                        try {

                            method.invoke(instance, attribute.get());
                        }
                        catch (Exception exception) {
                        }

                    }

                }

            }

        }
        catch (Exception exception) {
            throw new UncategorizedLdapException(exception.getMessage());
        }

        return instance;
    }

    /**
     * Extract LDAP attributes from LDAP annotations on fields and
     * setter methods of entity type.
     * @param <T> The type
     * @param entityType The entity type
     * @return The LDAP attributes
     */
    public static <T> String[] getAttributes(
        final Class<T> entityType) {

        Set<String> attributes;
        LdapProperty ldapProperty;

        attributes = new HashSet<String>();

        for (Field field : entityType.getDeclaredFields()) {

            ldapProperty = field.getAnnotation(LdapProperty.class);

            if (ldapProperty != null) {

                attributes.add(ldapProperty.value());
            }

        }

        for (Method method : entityType.getDeclaredMethods()) {

            ldapProperty = method.getAnnotation(LdapProperty.class);

            if (ldapProperty != null) {

                attributes.add(ldapProperty.value());
            }

        }

        return attributes.toArray(new String[attributes.size()]);
    }

}
