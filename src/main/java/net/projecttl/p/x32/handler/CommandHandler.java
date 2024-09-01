package net.projecttl.p.x32.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.projecttl.p.x32.Px32;

import java.util.List;

public class CommandHandler extends ListenerAdapter {
    private final List<Command> commands;

    public CommandHandler(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent ev) {
        if (ev.getUser().isBot()) {
            return;
        }

        for (Command command : commands) {
            if (!command.getData().getName().equals(ev.getName())) {
                continue;
            }

            command.execute(ev);

            Px32.log.info("user {} execute command: {}", ev.getUser().getId(), ev.getName());
            break;
        }
    }
}
