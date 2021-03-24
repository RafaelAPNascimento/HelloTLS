package br.com.app.conf;

import br.com.app.rest.impl.ServicesImpl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(ServicesImpl.class);

        return resources;
    }
}
