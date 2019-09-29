package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;

@Entity("Books")
public class Book
{
	@Id
	private String isbn;
	private String title;
	private String author;
	@Property("price")
	private double cost;
	@Embedded
	private Publisher publisher;
	
	public Book()
	{
		cost = 0;
	}
	
	public String getIsbn()
	{
		return isbn;
	}
	
	public void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	public double getCost()
	{
		return cost;
	}
	
	public void setCost(double cost)
	{
		this.cost = cost;
	}
	
	public Publisher getPublisher()
	{
		return publisher;
	}
	
	public void setPublisher(Publisher publisher)
	{
		this.publisher = publisher;
	}
	
	public String toString()
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch(JsonProcessingException e)
		{
			return null;
		}
	}
}
