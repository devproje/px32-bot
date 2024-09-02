package net.projecttl.p.x32.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.projecttl.p.x32.handler.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

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
		AtomicReference<MessageEmbed> embed = new AtomicReference<>(new EmbedBuilder()
			.setDescription(":hourglass: Just wait a seconds...")
			.build());

		ev.replyEmbeds(embed.get()).queue(hook -> {
			Random r = new Random();
			embed.set(new EmbedBuilder()
				.setTitle(":ping_pong: Pong!")
				.addField("\uD83E\uDD16", format("**%d**ms", System.currentTimeMillis() - current), true)
				.addField("\uD83D\uDD0C", format("**%d**ms", ev.getJDA().getGatewayPing()), true)
				.setColor(r.nextInt(0x000001, 0xffffff))
				.build());

			hook.editOriginalEmbeds(embed.get()).queue();
		});
	}
}
