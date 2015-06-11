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
