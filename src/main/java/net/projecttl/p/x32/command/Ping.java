package net.projecttl.p.x32.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.projecttl.p.x32.handler.Command;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

public class Ping implements Command {
    @NotNull
    @Override
    public CommandData getData() {
        return CommandData.fromData(new CommandDataImpl(
            "ping",
            "Discord API 레이턴시를 확인 합니다."
        ).toData());
    }

    @Override
    public void execute(SlashCommandInteractionEvent ev) {
        long current = System.currentTimeMillis();
        ev.reply(":hourglass: Just wait a seconds...").queue(hook -> {
            String content = format("**BOT**: %d**ms**\n", System.currentTimeMillis() - current) +
                format("**API**: %d**ms**", ev.getJDA().getGatewayPing());

            hook.editOriginal(content).queue();
        });
    }
}
