package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;

/**
 * Book. Classe che permette di rappresentare un libro e tutti i suoi attributi.
 *
 * La classe è marcata con la Java Annotation @Entity("Books") in modo da mappare le istanze di questa classe su documenti
 * JSON archiviati nella collection Books.
 * L'elemento isbn viene utilizzato come identificativo dell'oggetto e, pertanto, viene marcato con la Java Annotation @Id.
 * Tutti gli attributi della classe che vanno mappati su attributi omonimi nel JSON non presentano alcuna annotazione mentre
 * l'attributo cost, che va mappato sul campo price del JSON, presenta la Java Annotation @Property("price") prima della
 * dichiarazione dell'attributo nella classe Java.
 * L'attibuto publisher viene marcato con la Java Annotation @Embedded in quanto si è scelto di rappresentare l'entità Publisher
 * come incapsulata all'interno del JSON di Book e non in una collection separata e poi referenziata.
 *
 * La rappresentazione testuale delle istanze di Book viene realizzata tramite l'utilizzo delle API di Jackson, che permette
 * la conversione da oggetto Java a JSON e viceversa.
 *
 * @author Edoardo Baral
 */

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
	
	/**
	 * Costruttore vuoto
	 */
	public Book()
	{ }
	
	/**
	 * Metodo che restituisce il codice ISBN del libro (che funge anche da identificativo nella collection Books)
	 * @return il codice ISBN del libro
	 */
	public String getIsbn()
	{
		return isbn;
	}
	
	/**
	 * Metodo che permette di impostare un codice ISBN per il libro
	 * @param isbn: codice ISBN da assegnare al libro
	 */
	public void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}
	
	/**
	 * Metodo che restituisce il titolo del libro
	 * @return il titolo del libro
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Metodo che permette di impostare un nuovo titolo per il libro
	 * @param title: titolo da assegnare al libro
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Metodo che restituisce l'autore del libro
	 * @return l'autore del libro
	 */
	public String getAuthor()
	{
		return author;
	}
	
	/**
	 * Metodo che permette di indicare un autore per il libro
	 * @param author:
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	/**
	 * Metodo che restituisce il prezzo del libro
	 * @return il prezzo del libro
	 */
	public double getCost()
	{
		return cost;
	}
	
	/**
	 * Metodo che permette di indicare il prezzo del libro
	 * @param cost: prezzo da assegnare al libro
	 */
	public void setCost(double cost)
	{
		this.cost = cost;
	}
	
	/**
	 * Metodo che restituisce la casa editrice del libro
	 * @return restituisce la casa editrice del libro
	 */
	public Publisher getPublisher()
	{
		return publisher;
	}
	
	/**
	 * Metodo che permette di assegnare una casa editrice al libro
	 * @param publisher: casa editrice da assegnare al libro
	 */
	public void setPublisher(Publisher publisher)
	{
		this.publisher = publisher;
	}
	
	/**
	 * Metodo che restituisce una rappresentazione testuale dell'oggetto Book in formato JSON
	 * @return la rappresentazione testuale del libro in JSON, null in caso d'errore
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
