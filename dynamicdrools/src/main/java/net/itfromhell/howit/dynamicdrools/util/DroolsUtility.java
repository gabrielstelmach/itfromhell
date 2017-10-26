package net.itfromhell.howit.dynamicdrools.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.itfromhell.howit.dynamicdrools.Rule;
import org.drools.core.spi.KnowledgeHelper;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

/**
 * Utility to operate Drools on runtime.
 * 
 * @author Gabriel Stelmach <gabriel_stelmach@hotmail.com>
 * @since 1.0
 */
public class DroolsUtility
{
	/**
	 * Loads a session to execute rules in memory using a template file.
	 * 
	 * @param rules List of rules to be compiled.
	 * @param templatePath Relative path to template file describing the rule's pattern.
	 * @return Session for execution of rules.
	 * @throws Exception 
	 */
	public StatelessKieSession loadSession(List<Rule> rules, String templatePath) throws Exception
	{
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(rules.size());
		
		for (Rule rule : rules)
		{
			maps.add(rule.asMap());
		}
		
		return loadSession(templatePath, maps);
	}
	
	/**
	 * Loads a session to execute rules in memory using a template file.
	 * 
	 * @param templatePath Relative path to template file describing the rule's pattern.
	 * @param rulesAsParameters List of maps representing each rule as a set of parameters.
	 * @return Session for execution of rules.
	 * @throws Exception 
	 */
	private StatelessKieSession loadSession(String templatePath, List<Map<String, Object>> rulesAsParameters) throws Exception
  {
    ObjectDataCompiler compiler = new ObjectDataCompiler();
		//Compiles the list of rules using the template to create a readable Drools Rules Language
    String drl = compiler.compile(rulesAsParameters, Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath));
    
		System.out.println("drl:\n" + drl);
		
    KieServices services = KieServices.Factory.get();
    KieFileSystem system = services.newKieFileSystem();
    system.write("src/main/resources/drools/templates/rule.drl", drl);
    services.newKieBuilder(system).buildAll();
    
    KieContainer container = services.newKieContainer(services.getRepository().getDefaultReleaseId());
    StatelessKieSession session = container.getKieBase().newStatelessKieSession();
    
    return session;
  }
	
	/**
	 * Debug tool to show what is happening over each triggered execution.<br>
	 * Name of rule trigger as well the object inspected are printed.
	 * 
	 * @param helper Injected when a consequence is fired.
	 */
	public static void debug(final KnowledgeHelper helper)
	{
		System.out.println("Triggered rule: " + helper.getRule().getName());
		if (helper.getMatch() != null && helper.getMatch().getObjects() != null)
		{
			for (Object object : helper.getMatch().getObjects())
			{
				System.out.println("Data object: " + object);
			}
		}
	}
}