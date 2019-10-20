package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Publisher. Classe che permette la rappresentazione di una casa editrice e di tutti i suoi attributi.
 *
 * La rappresentazione testuale delle istanze di Publisher viene realizzata tramite l'utilizzo delle API di Jackson, che permette
 * la conversione da oggetto Java a JSON e viceversa.
 *
 * @author Edoardo Baral
 */
public class Publisher
{
	private String name;
	
	/**
	 * Costruttore vuoto
	 */
	public Publisher()
	{ }
	
	/**
	 * Metodo che restituisce  il nome della casa editrice
	 * @return il nome della casa editrice
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Metodo che permette di indicare un nome per la casa editrice
	 * @param name: nome da assegnare alla casa editrice
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Metodo che restituisce una rappresentazione testuale dell'oggetto Publisher in formato JSON
	 * @return la rappresentazione testuale della casa editrice in JSON, null in caso di errore
	 */
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
