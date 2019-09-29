package om;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("customers") //Inutile se il nome della collection e della classe coincidono
public class Customer implements Serializable {
	private static final long serialVersionUID = 3990790906982099175L;
	
	@Id //Il campo name agisce come primary key della collection
	private String name;
	private String company;
	
	public Customer()
	{ }
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getCompany()
	{
		return company;
	}
	
	public void setCompany(String company)
	{
		this.company = company;
	}
	
	@Override
	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			return null;
		}
	}
}
