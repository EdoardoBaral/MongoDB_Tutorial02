import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import om.Book;
import om.Publisher;

import java.util.List;

public class MorphiaMain
{
	public static void main(String[] args) throws Exception
	{
		final String URI = "mongodb+srv://EdoardoBaral:r5_m6_v7_e9@cluster0-h2qqj.gcp.mongodb.net/test?retryWrites=true&w=majority";
		final String DATABASE = "test01";
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("om");
		MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
		Datastore datastore = morphia.createDatastore(mongoClient, DATABASE);
		datastore.ensureIndexes();
		
		/* ---------- Scrittura di un'entità ---------- */
		Publisher publisher = new Publisher();
		publisher.setName("Awsome Publisher");
		
		Book book = new Book();
		book.setIsbn("9781565927186");
		book.setTitle("Learning Java");
		book.setAuthor("Tom Kirkman");
		book.setCost(3.95);
		book.setPublisher(publisher);
		
		publisher.addCompanionBook(book);
		
		datastore.save(book);
		datastore.save(publisher);
		// L'inserimento di un elemento in una collection la cui PK coincide con quella di un altro elemento, non causa un errore
		
		/* ---------- Lettura di un'entità ---------- */
		List<Book> books = datastore.createQuery(Book.class).field("title").contains("Learning Java").find().toList();
		if(books.isEmpty())
		{
			System.out.println("Nessun risultato trovato");
		}
		else
		{
			for(Book b : books)
			{
				System.out.println("Risultato: "+ b.toString());
			}
		}
		
		/* ---------- Update di un'entità ---------- */
		Query<Book> query = datastore.createQuery(Book.class).field("title").contains("Learning Java");
		UpdateOperations<Book> updates = datastore.createUpdateOperations(Book.class).inc("price", 1);
		datastore.update(query, updates);
		
		books = datastore.createQuery(Book.class).field("title").contains("Learning Java").find().toList();
		if(books.isEmpty())
		{
			System.out.println("Nessun risultato trovato");
		}
		else
		{
			for(Book b : books)
			{
				System.out.println("Risultato: "+ b.toString());
			}
		}
		
		/* ---------- Cancellazione di un'entità ---------- */
		query = datastore.createQuery(Book.class).field("title").contains("Learning");
		datastore.delete(query);
		
		books = datastore.createQuery(Book.class).field("title").contains("Learning Java").find().toList();
		if(books.isEmpty())
		{
			System.out.println("Nessun risultato trovato");
		}
		else
		{
			for(Book b : books)
			{
				System.out.println("Risultato: "+ b.toString());
			}
		}
		
		System.out.println(">>> FINE PROGRAMMA <<<");
	}
}
