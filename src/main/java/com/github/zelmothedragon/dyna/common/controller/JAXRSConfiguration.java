package com.github.zelmothedragon.dyna.common.controller;

import com.github.zelmothedragon.dyna.security.Roles;
import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({
    Roles.GUEST,
    Roles.ADMIN
})
@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {

    public JAXRSConfiguration() {
    }

}
