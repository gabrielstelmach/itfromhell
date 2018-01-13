/**
 * Copyright 2018 Gabriel Stelmach
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the 
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Licensed under the MIT license: https://opensource.org/licenses/MIT
 */
package net.itfromhell.howit.dynamicdrools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business rule representation<br>
 * This class encapsulates all necessary elements to define a specific rule using <b>Drools</b>.
 * 
 * @author Gabriel Stelmach <gabriels@fourway.com.br>
 */
public class Rule
{
  /**
   * Rule's friendly name.
   */
  private final String name;
  /**
   * Class qualified name for which the rule will be applied.
   */
  private String object;
  /**
	 * Conditionals of this rule.
   */
  private List<Condition> conditions = new ArrayList<Condition>();
  /**
   * Action to be executed.
   */
  private String action;
  
  /**
   * Creates a new rule.
   * 
   * @param name Friendly name.
   */
  public Rule(String name)
  {
    this.name = name;
  }
	
	/**
	 * List of attributes available to use in template.<br>
	 * These names must be the same used to write the .drl file template, which is compiled in runtime.
	 */
	public enum Attribute
	{
		/**
		 * Name of the rule.
		 */
		RULE_NAME("name"),
		/**
		 * Object with data to be processed.
		 */
		DATA_OBJECT("object"),
		/**
		 * Conditional expression.
		 */
		CONDITIONAL("conditional"),
		/**
		 * Action to take.
		 */
		ACTION("action");
		
		/**
		 * Name used in template to assign each attirbute.
		 */
		private final String name;
		
		private Attribute(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
  
  @Override
  public String toString()
  {
    StringBuilder me = new StringBuilder("[" + this.getClass().getName());
    me.append(" | name = ");
    me.append(name);
    me.append(" | object = ");
    me.append(object);
    me.append(" | conditions = ");
    me.append(((conditions == null) ? "null" : conditions.size()));
    me.append(" | action = ");
    me.append(action);
    me.append("]");
    
    return me.toString();
  }
	
	/**
	 * Converts these conditionals to Drools Rule Language (DRL) format.<br>
	 * The formatted conditional is in dialect Java (<i>dialect "java"</i>).
   * 
   * @return Rule's conditional expression.
   * @throws IllegalStateException Indicates none conditional declared.
	 * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
   */
  public String conditionAsDRL() throws IllegalStateException, IllegalArgumentException
  {
    if ((conditions == null) || (conditions.isEmpty()))
    {
      throw new IllegalStateException("You must declare at least one condition to be evaluated.");
    }
    
    StringBuilder drl = new StringBuilder();
		//For each condition of this rule, we create its textual representation
    for (int i = 0; i < conditions.size(); i++)
    {
      Condition condition = conditions.get(i);
			drl.append("(");
			drl.append(condition.buildExpression());
			drl.append(")");
      if ((i + 1) < conditions.size())
      {
        drl.append(" && ");
      }
    }
		
    return drl.toString();
  }
	
	/**
	 * Returns the created rule as a map of its properties to be compiled with template.
	 * 
	 * @return Map of rule's properties.
	 * @throws IllegalStateException Indicate a non valid rule.
	 */
	public Map<String, Object> asMap() throws IllegalStateException
	{
		if ((name == null) || (object == null) || (action == null))
		{
			throw new IllegalArgumentException("The rule has no name, object to be evaluated or action to be accomplished.");
		}
		
		Map<String, Object> attributes = new HashMap<String, Object>();
    attributes.put(Rule.Attribute.RULE_NAME.toString(), name);
    attributes.put(Rule.Attribute.DATA_OBJECT.toString(), object);
    attributes.put(Rule.Attribute.CONDITIONAL.toString(), conditionAsDRL());
    attributes.put(Rule.Attribute.ACTION.toString(), action);
		
		return attributes;
	}
  
	/**
	 * Create new condition and add it to this rule.
	 * 
	 * @param property Object property to be evaluated.
	 * @param operator Operator used to compare the data.
	 * @param value Value to be evaluated.
	 * @return Condition created.
	 */
	public Condition addCondition(String property, Condition.Operator operator, Object value)
	{
		Condition condition = new Condition(property, operator, value);
		conditions.add(condition);
		
		return condition;
	}
	
  public String getName()
  {
    return name;
  }

  public String getDataObject()
  {
    return object;
  }
  
  public List<Condition> getConditions()
  {
    return conditions;
  }
	
	public Condition getCondition()
	{
		if ((conditions == null) || (conditions.isEmpty()))
		{
			return null;
		}
		else
		{
			return conditions.get(0);
		}
	}
  
  public void setConditions(List<Condition> conditions)
  {
    this.conditions = conditions;
  }
  
	public void setCondition(Condition condition)
  {
    conditions = new ArrayList<Condition>();
    conditions.add(condition);
  }

  public String getAction()
  {
    return action;
  }
  
  public void setDataObject(String dataObject)
  {
    this.object = dataObject;
	}
	
  public void setAction(String action)
  {
    this.action = action;
  }
}