package de.mustnotbenamed.quickstart.undertowserver;

import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;

/**
 * @author schrader
 */
public interface UndertowBootstrap {

    void startup();

    void shutdown();

    UndertowOptions getUndertowOptions();

    class DefaultLiteral extends AnnotationLiteral<Default> implements Default {

        private static final long serialVersionUID = 5464062523108931731L;

        public static final Default INSTANCE = new DefaultLiteral();

        private DefaultLiteral() {
        }

    }

    class BuiltInLiteral extends AnnotationLiteral<BuiltIn> implements BuiltIn {

        private static final long serialVersionUID = 5464062523108931731L;

        public static final BuiltIn INSTANCE = new BuiltInLiteral();

        private BuiltInLiteral() {
        }

    }

    class UndertowOptions {

        private int port = 8080;

        private String host = "localhost";

        private String contextPath = "/";

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public UndertowOptions port(int port) {
            this.port = port;
            return this;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public UndertowOptions host(String host) {
            this.host = host;
            return this;
        }

        public String getContextPath() {
            return contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        public UndertowOptions contextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("UndertowOptions[");
            sb.append("port='").append(port).append('\'');
            sb.append(", host='").append(host).append('\'');
            sb.append(", contextPath='").append(contextPath).append('\'');
            sb.append(']');
            return sb.toString();
        }
    }
}
