/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org>
 */
package de.mustnotbenamed.quickstart.undertowserver;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.weld.environment.se.Weld;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.core.Application;
import java.util.logging.Logger;

import static de.mustnotbenamed.quickstart.undertowserver.WeldUndertowBootstrap.findApplication;
import static de.mustnotbenamed.quickstart.undertowserver.WeldUndertowBootstrap.getApplicationPath;
import static de.mustnotbenamed.quickstart.undertowserver.WeldUndertowBootstrap.getTargetClass;

/**
 * @author schrader
 */
public class CdiUndertowBootstrap implements UndertowBootstrap {

    private static final Logger LOG = Logger.getLogger(CdiUndertowBootstrap.class.getName());

    private UndertowOptions options;

    private UndertowJaxrsServer server;

    private boolean serverRunning = false;

    public CdiUndertowBootstrap(UndertowOptions undertowOptions) {
        this.options = undertowOptions;
    }

    @Override
    public void startup() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });

        Weld weld = new Weld();
        weld.addExtension(new org.jboss.resteasy.cdi.ResteasyCdiExtension());
        weld.initialize();

        Undertow.Builder builder = Undertow.builder()
                .addHttpListener(options.getPort(), options.getHost());

        server = new UndertowJaxrsServer().start(builder);
        serverRunning = true;
        LOG.info(String.format("Webserver started: http://%s:%s%s", options.getHost(), options.getPort(), options.getContextPath()));

        CDI<Object> cdi = CDI.current();
        DeploymentInfo di = deployJaxrsApplication(cdi, server)
                .setContextPath(options.getContextPath())
                .setDeploymentName("Undertow - JAX-RS - CDI");
        server.deploy(di);
    }

    @Override
    public synchronized void shutdown() {
        if (!serverRunning) {
            return;
        }

        if (server != null) {
            server.stop();
        }

        serverRunning = false;
    }

    @Override
    public UndertowOptions getUndertowOptions() {
        return options;
    }

    private DeploymentInfo deployJaxrsApplication(CDI cdi, UndertowJaxrsServer jaxrsServer) {
        Instance<Application> applicationInstance = cdi.select(Application.class);
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

}
