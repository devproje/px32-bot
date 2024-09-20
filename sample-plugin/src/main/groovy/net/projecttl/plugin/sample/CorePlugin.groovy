package net.projecttl.plugin.sample

import net.projecttl.p.x32.api.Plugin
import net.projecttl.plugin.sample.command.Greeting

class CorePlugin extends Plugin {
	@Override
	void onLoad() {
		logger.info "Hello, World!"
		commandContainer.addCommand new Greeting()
	}

	@Override
	void destroy() {
	}
}
