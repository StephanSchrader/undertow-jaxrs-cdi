package de.mustnotbenamed.quickstart.undertowserver;

import org.jboss.resteasy.cdi.ResteasyCdiExtension;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author schrader
 */
@ApplicationScoped
public class JaxRsResourceFinder {

    @Inject
    private ResteasyCdiExtension resteasyCdiExtension;

    public List<Class<?>> getResources() {
        return toGeneric(resteasyCdiExtension.getResources());
    }

    public List<Class<?>> getProviders() {
        return toGeneric(resteasyCdiExtension.getProviders());
    }

    public Set<Class<?>> getClasses() {
        Set<Class<?>> result = new HashSet<>();
        result.addAll(getResources());
        result.addAll(getProviders());
        return result;
    }

    private List<Class<?>> toGeneric(List<Class> resources) {
        List<Class<?>> result = new LinkedList<>();
        for (Class resource : resources) {
            result.add(resource);
        }

        return result;
    }

}
