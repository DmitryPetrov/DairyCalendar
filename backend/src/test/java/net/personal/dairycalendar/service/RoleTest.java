package net.personal.dairycalendar.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void getGrantedAuthority_admin() {
        Assertions.assertEquals("ROLE_ADMIN", Role.ADMIN.getGrantedAuthority().getAuthority());
    }

    @Test
    void getGrantedAuthority_user() {
        Assertions.assertEquals("ROLE_USER", Role.USER.getGrantedAuthority().getAuthority());
    }
}