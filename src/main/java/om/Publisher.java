package om;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity("Publishers")
public class Publisher
{
	@Id
	private String name;
	@Reference
	@JsonIgnore
	private List<Book> companionBooks;
	
	public Publisher()
	{
		companionBooks = new ArrayList<>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public List<Book> getCompanionBooks()
	{
		return companionBooks;
	}
	
	public void setCompanionBooks(List<Book> companionBooks)
	{
		this.companionBooks = companionBooks;
	}
	
	public void addCompanionBook(Book book)
	{
		this.companionBooks.add(book);
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
