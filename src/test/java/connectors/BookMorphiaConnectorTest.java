package connectors;

import om.Book;
import om.Publisher;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BookMorphiaConnectorTest
{
	@Test
	@Ignore
	public void initTest()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		assertNotNull(connector);
	}
	
	@Test
	@Ignore
	public void saveBookTest()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		
		Book book1 = new Book();
		book1.setIsbn("9781565927186");
		book1.setTitle("Learning Java");
		book1.setAuthor("Tom Kirkman");
		book1.setCost(3.95);
		
		Publisher pub1 = new Publisher();
		pub1.setName("Rizzoli Editore");
		book1.setPublisher(pub1);
		
		Book book2 = new Book();
		book2.setIsbn("9781565927623");
		book2.setTitle("Learning C");
		book2.setAuthor("Tom Kirkman");
		book2.setCost(4.20);
		
		Publisher pub2 = new Publisher();
		pub2.setName("Mondadori");
		book2.setPublisher(pub2);
		
		Book book3 = new Book();
		book3.setIsbn("9781565927624");
		book3.setTitle("Learning Python");
		book3.setAuthor("Thomas Jefferson");
		book3.setCost(2.20);
		
		book3.setPublisher(pub2);
		
		assertEquals("9781565927186", connector.saveBook(book1));
		assertEquals("9781565927623", connector.saveBook(book2));
		assertEquals("9781565927624", connector.saveBook(book3));
	}
	
	@Test
	@Ignore
	public void getBookByTitleTest()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		
		// caso libro trovato
		assertNotNull(connector.getBookByTitle("Learning Java"));
		
		// caso libro non trovato
		assertNull(connector.getBookByTitle("Bibbia"));
	}
	
	@Test
	@Ignore
	public void getBooksByAuthorTest()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		
		// caso autore trovato
		List<Book> positiveList = connector.getBooksByAuthor("Tom Kirkman");
		assertNotNull(positiveList);
		assertFalse(positiveList.isEmpty());
		assertEquals(2, positiveList.size());
		
		// caso autore non trovato
		List<Book> negativeList = connector.getBooksByAuthor("Paolo Rossi");
		assertNotNull(negativeList);
		assertTrue(negativeList.isEmpty());
	}
	
	@Test
	@Ignore
	public void getBooksByPrice()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		
		List<Book> positiveList = connector.getBooksByPrice(3.5, 4.5);
		assertNotNull(positiveList);
		assertFalse(positiveList.isEmpty());
		assertEquals(2, positiveList.size());
		
		List<Book> negativeList = connector.getBooksByPrice(10, 15);
		assertNotNull(negativeList);
		assertTrue(negativeList.isEmpty());
	}
	
	@Test
	@Ignore
	public void updateBookPriceTest()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		
		// caso libro trovato e update effettuato
		assertEquals(1, connector.updateBookPrice("Learning Java", 6.00));
		
		// caso libro non trovato e update non effettuato
		assertEquals(0, connector.updateBookPrice("Bibbia", 6.00));
	}
	
	@Test
	@Ignore
	public void removeBookByTitleTest()
	{
		BookMorphiaConnector connector = new BookMorphiaConnector();
		
		// caso libro trovato ed eliminato
		assertEquals(1, connector.removeBookByTitle("Learning Java"));
		
		// caso libro non trovato e non eliminato
		assertEquals(0, connector.removeBookByTitle("Bibbia"));
	}
}