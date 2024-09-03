package net.projecttl.p.x32.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.projecttl.p.x32.Px32;

import java.util.List;

public class CommandHandler extends ListenerAdapter {
    private final List<CommandExecutor> commands;

    public CommandHandler(List<CommandExecutor> commands) {
        this.commands = commands;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent ev) {
        if (ev.getUser().isBot()) {
            return;
        }

        for (CommandExecutor command : commands) {
            if (!command.getData().getName().equals(ev.getName())) {
                continue;
            }

            try {
                command.execute(ev);
                Px32.log.info("user {} execute command: {}", ev.getUser().getId(), ev.getName());
            } catch (Exception ex) {
                Px32.log.error("user {} execute command {} failed", ev.getUser().getId(), ev.getName(), ex);
            }
            break;
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent ev) {
        Px32.log.info("user {} execute context: {}", ev.getUser().getId(), ev.getName());
    }
}
