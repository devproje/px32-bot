package net.projecttl.p.x32;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.projecttl.p.x32.command.Ping;
import net.projecttl.p.x32.handler.CommandExecutor;
import net.projecttl.p.x32.handler.CommandHandler;
import net.projecttl.p.x32.handler.Ready;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Px32 {
	public static final Logger log = LoggerFactory.getLogger(Px32.class);
	private JDA jda;
	private final ArrayList<CommandExecutor> commands = new ArrayList<>();

	private void register() {
		commands.forEach(command -> {
			jda.upsertCommand(command.getData()).queue();
			jda.updateCommands().addCommands(
				Commands.context(Command.Type.USER, command.getData().getName())
			).queue();
			log.info("registered command: {}", command.getData().getName());
		});
	}

	public static void main(String[] args) {
		Px32 core = new Px32();
		JDABuilder builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"));

		core.commands.add(new Ping());
		CommandHandler handler = new CommandHandler(core.commands);

		builder.addEventListeners(
			handler,
			new Ready()
		);
		builder.setAutoReconnect(true);

		core.jda = builder.build();
		core.register();
	}
}
