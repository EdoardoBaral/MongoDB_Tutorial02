package connectors;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.UpdateOperations;
import om.Author;
import om.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dev.morphia.aggregation.Group.grouping;
import static dev.morphia.aggregation.Group.push;
import static utils.ConnectionParams.DATABASE;
import static utils.ConnectionParams.URI;

/**
 * BookMorphiaConnector. Classe che permette di realizzare un connettore a MongoDB per la gestione degli elementi nella
 * collection Books.
 *
 * Il connettore si interfaccia a MongoDB per mezzo del framework Morphia, un Object Document Mapper (ODM) che permette
 * di mappare un oggetto Java su un documento JSON e viceversa, oltre a mettere a disposizione una serie di API per stabilire
 * una connessione al database, gestire le collection, effettuare le classiche operazioni CRUD sugli elementi delle collection
 * e molto altro.
 *
 * @author Edoardo Baral
 */
public class BookMorphiaConnector
{
	private Datastore datastore;
	
	/**
	 * Costruttore senza parametri, che crea un'istanza di BookMorphiaConnector e stabilisce una connessione con MongoDB
	 * utilizzando le API di Morphia
	 */
	public BookMorphiaConnector()
	{
		Morphia morphia = new Morphia();
		morphia.mapPackage("om");
		MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
		datastore = morphia.createDatastore(mongoClient, DATABASE);
		datastore.ensureIndexes();
	}
	
	/**
	 * Metodo che salva nella collection Books un nuovo libro. Il metodo può anche essere utilizzato per sovrascrivere completamente
	 * un libro già presente nella collection purché l'identificativo rimanga invariato (quindi il codice ISBN)
	 * @param book: libro da salvare sul database
	 * @return l'identificativo dell'elemento appena salvato
	 */
	public String saveBook(Book book)
	{
		Key<Book> key = datastore.save(book);   // restituisce la chiave dell'elemento appena inserito nella collection Books
		String isbnCode = (String) key.getId(); // valore della chiave (codice ISBN del libro)
		return isbnCode;
	}
	
	/**
	 * Metodo che cerca sul database il libro il cui titolo corrisponde con quello passato come argomento
	 * @param title: titolo del libro da cercare
	 * @return il libro recuperato dalla collection Books il cui titolo corrisponde con title, null se non viene trovato
	 */
	public Book getBookByTitle(String title)
	{
		List<Book> list = datastore.createQuery(Book.class).field("title").equal(title).find().toList();
		return list.isEmpty() ? null : list.get(0);
	}
	
	/**
	 * Metodo che restituisce la lista dei libri dell'autore il cui nome viene passato come argomento
	 * @param author: nome dell'autore
	 * @return la lista dei libri di author
	 */
	public List<Book> getBooksByAuthor(String author)
	{
		List<Book> list = datastore.createQuery(Book.class).field("author").equal(author).find().toList();
		return list;
	}
	
	/**
	 * Metodo che cerca e restituisce tutti i libri nella collection che presentano un prezzo compreso nell'intervallo tra
	 * min e max, passati come argomento
	 * @param min: estremo inferiore dell'intervallo di prezzo
	 * @param max: estremo superiore dell'intervallo di prezzo
	 * @return la lista di libri che presentano un prezzo compreso tra min e max inclusi
	 */
	public List<Book> getBooksByPrice(double min, double max)
	{
		Query<Book> query = datastore.find(Book.class);
		query.and(query.criteria("price").greaterThanOrEq(min), query.criteria("price").lessThanOrEq(max));
		List<Book> list = query.find().toList();
		return list;
	}
	
	/**
	 * Metodo che raggruppa tutti i libri della collection ognuno all'interno di un oggetto che rappresenta il rispettivo
	 * autore.
	 *
	 * A partire dalla collection Books, i libri vengono raggruppati per autore dopodiché vengono create delle nuove entità
	 * Author in cui viene aggiunto un nuovo campo books; questo viene poi gestito come una lista e vi vengono inseriti
	 * tutti i libri precedentemente suddivisi, a seconda dell'autore.
	 * La lista degli autori viene poi ordinata in base all'identificativo (ovvero il nome).
	 *
	 * @return la lista di tutti gli autori ad ognuno dei quali sono associati i propri libri
	 */
	public List<Author> getBooksGroupByAuthor()
	{
		Iterator<Author> iterator = datastore.createAggregation(Book.class)
											 .group("author", grouping("books", push("$ROOT")))
											 .sort(Sort.ascending("_id"))
											 .out(Author.class);

		List<Author> result = new ArrayList<>();
		while(iterator.hasNext())
		{
			result.add(iterator.next());
		}

		return result;
	}
	
	/**
	 * Metodo che restituisce una lista di tutti i libri della collection ordinati alfabeticamente prima per nome
	 * dell'autore e poi per titolo
	 * @return una lista di tutti i libri della collection ordinati alfabeticamente prima per nome dell'autore e poi per titolo
	 */
	public List<Book> getBooksSortedByAuthorAndTitle()
	{
		List<Book> sortedist = datastore.find(Book.class).order(Sort.ascending("author")).order(Sort.ascending("title")).find().toList();
		return sortedist;
	}
	
	/**
	 * Metodo che restituisce una prioezione dell'intera collezione di libri in cui, per ogni elemento, viene specificato
	 * solamente il campo del titolo (oltre all'identificativo della collection, il codice ISBN, che viene popolato di default)
	 * @return la lista di libri pordotta dalla proiezione per cui solo il titolo viene popolato
	 */
	public List<Book> getBooksProjection()
	{
		List<Book> list = datastore.find(Book.class).project("title", true).find().toList();
		return list;
	}
	
	/**
	 * Metodo che permette di aggiornare il prezzo di un libro nella collection Books
	 * @param title: titolo del libro da cercare
	 * @param price: nuovo prezzo da assegnare al libro
	 * @return il contatore degli elementi che sono stati aggiornati
	 */
	public int updateBookPrice(String title, double price)
	{
		Query<Book> selectBookQuery = datastore.createQuery(Book.class).field("title").contains(title);
		UpdateOperations<Book> updates = datastore.createUpdateOperations(Book.class).set("price", price);
		return datastore.update(selectBookQuery, updates).getUpdatedCount();
	}
	
	/**
	 * Metodo che rimuove un determinato libro dalla collection Books
	 * @param title: titolo del libro da eliminare
	 * @return il contatore dei libri eliminati dalla collection
	 */
	public int removeBookByTitle(String title)
	{
		Query<Book> selectBookQuery = datastore.createQuery(Book.class).field("title").contains(title);
		return datastore.delete(selectBookQuery).getN();
	}
}
