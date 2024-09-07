package net.projecttl.p.x32.exec;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.projecttl.p.x32.exec.config.DefaultConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Px32 {
	public static final Logger log = LoggerFactory.getLogger(Px32.class);
	private static JDA jda;

	public static void main(String[] args) {
		JDABuilder builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"));
		log.info("Px32 {}", DefaultConfig.INSTANCE.getVersion());

		builder.setAutoReconnect(true);
		jda = builder.build();
	}
}
