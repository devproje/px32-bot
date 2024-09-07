package net.wh64.p.x32;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class KernelCore {
	private final JDABuilder builder;

	public KernelCore(String token) {
		this.builder = JDABuilder.createDefault(token);
	}

	public JDA build() {
		JDA jda = builder.build();

		return jda;
	}
}
