package de.mustnotbenamed.quickstart.undertowserver;

public class WeldUndertowBootstrapTest extends UndertowBootstrapTest {

    @Override
    protected UndertowBootstrap createSUT() {
        UndertowBootstrap.UndertowOptions options = new UndertowBootstrap.UndertowOptions();
        return new WeldUndertowBootstrap(options);
    }
}