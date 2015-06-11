package de.mustnotbenamed.quickstart.undertowserver.example.resources;

/**
 * @author schrader
 */
public class Pong {

    private String ping = null;

    public Pong(String ping) {
        this.ping = ping;
    }

    public String getPing() {
        return ping;
    }
}
