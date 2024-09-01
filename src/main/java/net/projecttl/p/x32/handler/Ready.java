package net.projecttl.p.x32.handler;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ready extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Ready.class);

    @Override
    public void onReady(ReadyEvent ev) {
        log.info("Logged in as {}", ev.getJDA().getSelfUser().getAsTag());
    }
}
