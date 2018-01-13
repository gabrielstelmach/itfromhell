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
package net.itfromhell.howit.dummy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Representation of Product to buy or sold, depending on context.
 * 
 * @author Gabriel Stelmach <gabriel_stelmach@hotmail.com>
 */
public class Product
{
	/**
	 * Product name.
	 */
	private final String name;
	/**
	 * Product price.
	 */
	private double price;
	/**
	 * Product due date.
	 */
	private final Date dueDate;
	
	public Product(String name, double price, String dueDate) throws ParseException
	{
		this.name = name;
		this.price = price;
		this.dueDate = (new SimpleDateFormat("dd/MM/yyyy")).parse(dueDate);
	}
	
	public Product(String name, double price, Date dueDate)
	{
		this.name = name;
		this.price = price;
		this.dueDate = dueDate;
	}
	
	/**
	 * Apply the discount on product.
	 * 
	 * @param percent Percent to apply.
	 */
	public void discount(double percent)
	{
		price = (price - ((percent * price) / 100));
	}
	
	@Override
	public String toString()
	{
		StringBuilder me =  new StringBuilder("[" + this.getClass().getName());
		me.append(" | name = ");
		me.append(name);
		me.append(" | price = ");
		me.append(price);
		me.append(" | dueDate = ");
		me.append(((dueDate == null) ? null : (new SimpleDateFormat("dd/MM/yyyy")).format(dueDate)));
		me.append("]");
		
		return me.toString();
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	public Date getDueDate()
	{
		return dueDate;
	}	
}