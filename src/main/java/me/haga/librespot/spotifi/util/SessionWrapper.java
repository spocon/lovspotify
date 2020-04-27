package me.haga.librespot.spotifi.util;


import xyz.gianlu.librespot.core.Session;
import xyz.gianlu.librespot.core.ZeroconfServer;

import java.util.concurrent.atomic.AtomicReference;

public final class SessionWrapper {

    private final AtomicReference<Session> ref = new AtomicReference<>(null);
    private Listener listener = null;

    private SessionWrapper() {
    }

    /**
     * Convenience method to create an instance of {@link SessionWrapper} that is updated by {@link ZeroconfServer}
     *
     * @param server The {@link ZeroconfServer}
     * @return A wrapper that holds a changing session
     */
    public static SessionWrapper fromZeroconf( ZeroconfServer server) {
        SessionWrapper wrapper = new SessionWrapper();
        server.addSessionListener(wrapper::set);
        return wrapper;
    }

    /**
     * Convenience method to create an instance of {@link SessionWrapper} that holds a static session
     *
     * @param session The static session
     * @return A wrapper that holds a never-changing session
     */
    public static SessionWrapper fromSession( Session session) {
        SessionWrapper wrapper = new SessionWrapper();
        wrapper.ref.set(session);
        return wrapper;
    }

    public void setListener( Listener listener) {
        this.listener = listener;

        Session s;
        if ((s = ref.get()) != null) listener.onNewSession(s);
    }

    private void set( Session session) {
        ref.set(session);
        session.addCloseListener(this::clear);
        if (listener != null) listener.onNewSession(session);
    }

    private void clear() {
        ref.set(null);
        if (listener != null) listener.onSessionCleared();
    }

    public Session get() {
        Session s = ref.get();
        if (s != null) {
            if (s.isValid()) return s;
            else clear();
        }

        return null;
    }

    public interface Listener {
        void onSessionCleared();

        void onNewSession( Session session);
    }
}

