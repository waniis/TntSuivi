package com.mycompany.myapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String MEDECIN = "ROLE_MEDECIN";

    public static final String PATIENT = "ROLE_PATIENT";

    public static final String ADMIN_DE_CENTRE = "ROLE_ADMIN_DE_CENTRE";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
