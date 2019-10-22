package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity("Authors")
public class Author
{
	@Id
	private String name;
	private List<String> books;
	
	public Author()
	{
		books = new ArrayList<>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public List<String> getBooks()
	{
		return books;
	}
	
	public void setBooks(List<String> books)
	{
		this.books = books;
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
