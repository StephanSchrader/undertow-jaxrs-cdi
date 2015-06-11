package de.mustnotbenamed.quickstart.undertowserver;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author schrader
 */
@BuiltIn
@ApplicationScoped
@ApplicationPath("/")
public class DefaultJaxRsApplication extends Application {

    @Inject
    private JaxRsResourceFinder resourceFinder;

    @Override
    public Set<Class<?>> getClasses() {
        return resourceFinder.getClasses();
    }
}
