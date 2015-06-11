package de.mustnotbenamed.quickstart.undertowserver;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.inject.Instance;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author schrader
 */
public class WeldUndertowBootstrap implements UndertowBootstrap {

    private static final Logger LOG = Logger.getLogger(WeldUndertowBootstrap.class.getName());

    private UndertowOptions options;

    private UndertowJaxrsServer server;

    private Weld weld;

    private boolean serverRunning = false;

    public WeldUndertowBootstrap(UndertowOptions options) {
        this.options = options;
    }

    @Override
    public UndertowOptions getUndertowOptions() {
        return options;
    }

    @Override
    public synchronized void shutdown() {
        if (!serverRunning) {
            return;
        }

        if (server != null) {
            LOG.info("Stopping webserver");
            server.stop();
        }

        if (weld != null) {
            LOG.info("Stopping CDI (Weld)");
            weld.shutdown();
        }

        serverRunning = false;
    }

    @Override
    public void startup() {
        // start CDI
        weld = new Weld();
        weld.addExtension(new org.jboss.resteasy.cdi.ResteasyCdiExtension());
        WeldContainer container = weld.initialize();

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        Undertow.Builder builder = Undertow.builder()
                .addHttpListener(options.getPort(), options.getHost());

        server = new UndertowJaxrsServer().start(builder);
        serverRunning = true;
        LOG.info(String.format("Webserver started: http://%s:%s%s", options.getHost(), options.getPort(), options.getContextPath()));

        DeploymentInfo di = deployJaxrsApplication(container, server)
                .setContextPath(options.getContextPath())
                .setDeploymentName("Undertow - JAX-RS - CDI");
        server.deploy(di);
    }

    private DeploymentInfo deployJaxrsApplication(WeldContainer container, UndertowJaxrsServer jaxrsServer) {
        Instance<Application> applicationInstance = container.instance().select(Application.class);
        Application application = findApplication(applicationInstance);
        String applicationPath = getApplicationPath(application.getClass());

        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");
        deployment.setApplication(application);

        DeploymentInfo deploymentInfo = jaxrsServer.undertowDeployment(deployment, applicationPath)
                .setClassLoader(application.getClass().getClassLoader())
                .addListeners(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));

        LOG.info(String.format("JAX-RS application registered, class: %s, path: %s", getTargetClass(application.getClass()).getName(), applicationPath));

        return deploymentInfo;
    }

    public static Application findApplication(Instance<Application> applicationInstance) {
        Instance<Application> defaultApplication = applicationInstance.select(DefaultLiteral.INSTANCE);
        if (!defaultApplication.isUnsatisfied()) {
            return defaultApplication.get();
        }

        return applicationInstance.select(BuiltInLiteral.INSTANCE).get();
    }

    public static <T> Class<T> getTargetClass(Class<T> aPossibleProxy) {
        if (aPossibleProxy.getSimpleName().contains("Proxy")) {
            return (Class<T>) aPossibleProxy.getSuperclass();
        }
        return aPossibleProxy;
    }

    public static String getApplicationPath(Class<? extends Application> applicationClass) {
        ApplicationPath appPath = getTargetClass(Objects.requireNonNull(applicationClass, "Argument 'applicationClass' must not be null"))
                .getAnnotation(ApplicationPath.class);
        return appPath != null ? appPath.value() : "/";
    }

    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            shutdown();
        }

    }

}
