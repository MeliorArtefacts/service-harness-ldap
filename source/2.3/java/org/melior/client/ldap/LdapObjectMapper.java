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
import org.melior.util.exception.ExceptionUtil;
import org.springframework.ldap.UncategorizedLdapException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;

/**
 * For the LDAP search use case, takes the attributes from an LDAP {@code SearchResult} and
 * maps the attributes to the members of a new instance of the specified type.
 * <p>
 * For the LDAP modify use case, takes the members of an instance of the specified type and
 * maps the members to the attributes of a supplied LDAP context.
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
    private LdapObjectMapper(
        final Class<T> entityType) {

        super();

        this.entityType = entityType;
    }

    /**
     * Get instance of LDAP object mapper.
     * @param <T> The type
     * @param entityType The entity type
     * @return The LDAP object mapper
     */
    public static <T> LdapObjectMapper<T> of(
        final Class<T> entityType) {
        return new LdapObjectMapper<T>(entityType);
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

                        T i = instance; Attribute a = attribute;
                        ExceptionUtil.swallow(() -> {
                            field.setAccessible(true);
                            field.set(i, a.get());
                        });
                    }

                }

            }

            for (Method method : entityType.getDeclaredMethods()) {

                ldapProperty = method.getAnnotation(LdapProperty.class);

                if (ldapProperty != null) {

                    attribute = attributes.get(ldapProperty.value());

                    if (attribute != null) {

                        T i = instance; Attribute a = attribute;
                        ExceptionUtil.swallow(() -> {
                            method.invoke(i, a.get());
                        });
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

    /**
     * Map LDAP attributes from the supplied instance. The attributes in the instance
     * are used to update a single LDAP context.
     * @param <T> The type
     * @param context The LDAP context
     * @param instance The instance that provides the attributes
     */
    @SuppressWarnings("hiding")
    public <T> void mapFromInstance(
        final DirContextOperations context,
        final T instance) {

        LdapProperty ldapProperty;
        Object attribute;

        try {

            for (Field field : entityType.getDeclaredFields()) {

                ldapProperty = field.getAnnotation(LdapProperty.class);

                if (ldapProperty != null) {

                    attribute = ExceptionUtil.swallow(() -> {
                        field.setAccessible(true);
                        return field.get(instance);
                    }, null);

                    if (attribute != null) {

                        context.setAttributeValue(ldapProperty.value(), attribute);
                    }

                }

            }

            for (Method method : entityType.getDeclaredMethods()) {

                ldapProperty = method.getAnnotation(LdapProperty.class);

                if (ldapProperty != null) {

                    attribute = ExceptionUtil.swallow(() -> {
                        return method.invoke(instance, new Object[0]);
                    }, null);

                    if (attribute != null) {

                        context.setAttributeValue(ldapProperty.value(), attribute);
                    }

                }

            }

        }
        catch (Exception exception) {
            throw new UncategorizedLdapException(exception.getMessage());
        }

    }

}
