package net.projecttl.plugin.sample

import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.plugin.sample.command.Greeting

class CorePlugin extends Plugin {
	@Override
	void onLoad() {
		logger.info "Hello, World!"
		CommandHandler handler = new CommandHandler()
		handler.addCommand new Greeting()

		addHandler handler
	}

	@Override
	void destroy() {
	}
}
