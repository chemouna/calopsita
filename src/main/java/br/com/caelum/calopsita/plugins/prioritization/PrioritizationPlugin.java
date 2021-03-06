package br.com.caelum.calopsita.plugins.prioritization;

import br.com.caelum.calopsita.model.Menu;
import br.com.caelum.calopsita.model.Parameters;
import br.com.caelum.calopsita.model.PluginConfig;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.SubmenuItem;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class PrioritizationPlugin implements PluginConfig {

	public String getDescription() {
		return null;
	}

	public String getName() {
		return "Prioritization";
	}

	public void includeMenus(Menu menu, Parameters parameters) {
		if (parameters.contains("project")) {
			Project project = parameters.get("project");
			menu.getOrCreate("cards")
				.add(new SubmenuItem("prioritize", "/projects/" + project.getId() +	"/prioritization/"));
		}
	}
}
