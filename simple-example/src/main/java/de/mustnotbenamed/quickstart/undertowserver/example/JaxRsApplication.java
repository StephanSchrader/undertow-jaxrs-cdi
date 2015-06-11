package de.mustnotbenamed.quickstart.undertowserver.example;

import de.mustnotbenamed.quickstart.undertowserver.JaxRsResourceFinder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author schrader
 */
@ApplicationScoped
@ApplicationPath("/")
public class JaxRsApplication extends Application {

    @Inject
    private JaxRsResourceFinder resourceFinder;

    @Override
    public Set<Class<?>> getClasses() {
        return resourceFinder.getClasses();
    }
}
