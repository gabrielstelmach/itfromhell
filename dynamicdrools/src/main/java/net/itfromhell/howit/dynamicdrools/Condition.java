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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Representation of condition to apply a business rule.
 * 
 * @author Gabriel Stelmach <gabriels@fourway.com.br>
 * @see Rule
 */
public class Condition
{
  /**
   * Object property to be evaluated.
   */
  private String property;
  /**
   * Value to be evaluated.
   */
  private Object value;
  /**
   * Operator used to compare the data.
   */
  private Operator operator;

  /**
   * Type of available operator.
   */
  public static enum Operator
  {
    /**
     * The value is not equal to
     */
    NOT_EQUAL_TO("Not equal to", "!=", (new ArrayList<Class>()
    {
      {
        add(String.class);
				add(Double.class);
				add(Float.class);
				add(Integer.class);
				add(Short.class);
				add(Long.class);
				add(Date.class);
      }
    })),
    /**
     * The value is equal to
     */
    EQUAL_TO("Equal to", "==", (new ArrayList<Class>()
    {
      {
        add(String.class);
				add(Double.class);
				add(Float.class);
				add(Integer.class);
				add(Short.class);
				add(Long.class);
				add(Date.class);
      }
    })),
    /**
     * The value contains
     */
    CONTAINS("Contains this", "?", (new ArrayList<Class>()
    {
      {
        add(String.class);
      }
    })),
    /**
     * The value is greater than
     */
    GREATER_THAN("Greater than", ">", (new ArrayList<Class>()
    {
      {
				add(Double.class);
				add(Float.class);
				add(Integer.class);
				add(Short.class);
				add(Long.class);
				add(Date.class);
      }
    })),
    /**
     * The value is less than
     */
    LESS_THAN("Less than", "<", (new ArrayList<Class>()
    {
      {
				add(Double.class);
				add(Float.class);
				add(Integer.class);
				add(Short.class);
				add(Long.class);
				add(Date.class);
      }
    })),
    /**
     * The value is greater or equal to
     */
    GREATER_THAN_OR_EQUAL_TO("Greater or equal to", ">=", (new ArrayList<Class>()
    {
      {
				add(Double.class);
				add(Float.class);
				add(Integer.class);
				add(Short.class);
				add(Long.class);
				add(Date.class);
      }
    })),
    /**
     * The value is less or equal to
     */
    LESS_THAN_OR_EQUAL_TO("Less or equal to", "<=", (new ArrayList<Class>()
    {
      {
				add(Double.class);
				add(Float.class);
				add(Integer.class);
				add(Short.class);
				add(Long.class);
				add(Date.class);
      }
    }));
		
    /**
     * Description for operator
     */
    private final String description;
    /**
     * Language operation
     */
    private final String operation;
		/**
		 * List of applicable classes.
		 */
		private final List<Class> acceptables;
    
    private Operator(String description, String operation, List<Class> acceptables)
    {
      this.description = description;
      this.operation = operation;
			this.acceptables = acceptables;
    }

    @Override
    public String toString()
    {
      StringBuilder me = new StringBuilder("[" + this.getClass().getName());
      me.append(" | name = ");
      me.append(name());
      me.append(" | description = ");
      me.append(description);
      me.append(" | operation = ");
      me.append(operation);
      me.append(" | acceptables = ");
      me.append(Arrays.toString(acceptables.toArray()));
      me.append("]");

      return me.toString();
    }
    
    public String getDescription()
    {
      return description;
    }
    
    public String getOperation()
    {
      return operation;
    }
    
		/**
		 * Indicates when the specified Class is comparable using this operator.
		 * 
		 * @param clazz Class to verify.
		 * @return True when this operator can be used.
		 */
		public boolean isComparable(Class clazz)
		{
			for (Class accept : acceptables)
			{
				if (accept.equals(clazz))
				{
					return true;
				}
			}
			
			return false;
		}
		
    /**
     * Gets the operator related to description.
     *
     * @param description Description for an operation.
     * @return Type of operator.
     * @throws EnumConstantNotPresentException When the description is not related to a valid operator.
     */
    public static Operator fromDescription(String description) throws EnumConstantNotPresentException
    {
      for (Operator operator : Operator.values())
      {
        if (operator.getDescription().equals(description))
        {
          return operator;
        }
      }
      
      throw new EnumConstantNotPresentException(Operator.class, "? (" + description + ")");
    }
  }
  
	/**
	 * Create a new empty condition.
	 */
	public Condition()
	{
	}
	
	/**
	 * Create a complete condition.
	 * 
	 * @param property Data property to be evaluated.
	 * @param operator Operator used to compare the data.
	 * @param value Value to be evaluated.
	 */
	public Condition(String property, Operator operator, Object value)
	{
		this.property = property;
		this.operator = operator;
		this.value = value;
	}
	
	/**
	 * Convert the condition to textual expression.
	 * 
	 * @return The expression of this condition in dialect.
	 * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
	 */
	public String buildExpression() throws IllegalArgumentException
	{
		StringBuilder drl = new StringBuilder();
		
		if (value instanceof String)
		{
			drl.append(expressionForStringValue());
		}
		else if (value instanceof Number)
		{
			drl.append(expressionForNumberValue());
		}
		else if (value instanceof Date)
		{
			drl.append(expressionForDateValue());
		}
		else
		{
			throw new IllegalArgumentException("The class " + value.getClass().getSimpleName() + " of value is not acceptable.");
		}
		
		return drl.toString();
	}
	
	/**
	 * Convert the condition for <b>String</b> value in expression.
	 * 
	 * @return Expression in dialect.
	 * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
	 */
	private String expressionForStringValue() throws IllegalArgumentException
	{
		StringBuilder drl = new StringBuilder();
		
		if (operator.isComparable(String.class))
		{
			if (operator.equals(Condition.Operator.CONTAINS))
			{
				drl.append(property).append(".toUpperCase().contains(\"").append(((String)value).toUpperCase()).append("\")");
			}
			else
			{
				drl.append(property).append(" ").append(operator.getOperation()).append(" ").append("\"").append(value).append("\"");
			}
		}
		else
		{
			throw new IllegalArgumentException("Is not possible to use the operator " + operator.getDescription() + " to a " + value.getClass().getSimpleName() + " object.");
		}
		
		return drl.toString();
	}
	
	/**
	 * Convert the condition for <b>Integer</b>, <b>Double</b> or <b>Float</b> value in expression.
	 * 
	 * @return Expression in dialect.
	 * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
	 */
	private String expressionForNumberValue() throws IllegalArgumentException
	{
		StringBuilder drl = new StringBuilder();
		
		if ((operator.isComparable(Short.class)) || (operator.isComparable(Integer.class)) || (operator.isComparable(Long.class)) 
						|| (operator.isComparable(Double.class)) || (operator.isComparable(Float.class)))
		{
			drl.append(property).append(" ").append(operator.getOperation()).append(" ").append(value);
		}
		else
		{
			throw new IllegalArgumentException("Is not possible to use the operator " + operator.getDescription() + " to a " + value.getClass().getSimpleName() + " object.");
		}
		
		return drl.toString();
	}
	
	/**
	 * Convert the condition for <b>Date</b> value in expression.
	 * 
	 * @return Expression in dialect.
	 * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
	 */
	private String expressionForDateValue() throws IllegalArgumentException
	{
		StringBuilder drl = new StringBuilder();
		
		if (operator.isComparable(Date.class))
		{
			drl.append(property).append(" ").append(operator.getOperation()).append(" (new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\")).parse(\"" + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format((Date)value) + "\")");
		}
		else
		{
			throw new IllegalArgumentException("Is not possible to use the operator " + operator.getDescription() + " to a " + value.getClass().getSimpleName() + " object.");
		}
		
		return drl.toString();
	}
	
  public String getProperty()
  {
    return property;
  }

  public Object getValue()
  {
    return value;
  }
  
  public Operator getOperator()
  {
    return operator;
  }
  
  public void setProperty(String property)
  {
    this.property = property;
  }
  
  public void setValue(Object value)
  {
    this.value = value;
  }

  public void setOperator(Condition.Operator operator)
  {
    this.operator = operator;
  }
}