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