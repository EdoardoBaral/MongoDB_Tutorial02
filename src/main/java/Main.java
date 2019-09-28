import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		final String MONGO_CONNECTION = "mongodb+srv://EdoardoBaral:r5_m6_v7_e9@cluster0-h2qqj.gcp.mongodb.net/test?retryWrites=true&w=majority";
		final String DATABASE = "test01";
		
		/*
		 * SI PARTE DAL PRESUPPOSTO CHE LA COLLECTION NON ESISTA SUL DATABASE
		 */
		
		CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
		CodecRegistry fromProvider = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
		
		MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).applyConnectionString(new ConnectionString(MONGO_CONNECTION)).build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoCursor<String> cursorDb = mongoClient.listDatabaseNames().iterator();
		
		// Recupero della lista dei nomi dei database disponibili
		int i = 1;
		System.out.println("Lista dei database disponibili");
		while(cursorDb.hasNext())
		{
			String nomeDb = cursorDb.next();
			System.out.println(i++ +") "+ nomeDb);
		}
		System.out.println();
		
		// Creazione di una collection nel database
		MongoDatabase database = mongoClient.getDatabase(DATABASE);
		MongoCursor<String> cursorCollections = database.listCollectionNames().iterator();
		i = 1;
		System.out.println("Lista delle collection disponibili in test01");
		while(cursorCollections.hasNext())
		{
			String nomeCollection = cursorCollections.next();
			System.out.println(i++ +") "+ nomeCollection);
		}
		cursorCollections.close();
		System.out.println();
		
		MongoCollection<Document> customers = database.getCollection("customers");
		System.out.println("Collection customers creata");
		i = 1;
		System.out.println("Lista delle collection disponibili (aggiornata)");
		cursorCollections = database.listCollectionNames().iterator();
		while(cursorCollections.hasNext())
		{
			String nomeCollection = cursorCollections.next();
			System.out.println(i++ +") "+ nomeCollection);
		}
		cursorCollections.close();
		System.out.println();
		//TODO Quando si crea una collection con il metodo getCollection(), questa non viene committata fino a quando non vi viene inserito almeno un documento. Se rimane vuota, ne viene fatto il rollback
		
		// Inserimento di un nuovo elemento in una collection (la insertOne() e la insertMany() non verificano l'eventuale presenza nella collection degli elementi da inserire)
		System.out.println("Inserimento di un nuovo elemento nella collection customers");
		ObjectMapper mapper = new ObjectMapper();
		
		Customer customer1 = new Customer();
		customer1.setName("Pinco Pallo");
		customer1.setCompany("Fake Company");
		String jsonCustomer1 = mapper.writeValueAsString(customer1);
		customers.insertOne(Document.parse(jsonCustomer1));
		System.out.println(jsonCustomer1);
		
		Customer customer2 = new Customer();
		customer2.setName("Stanis LaRochelle");
		customer2.setCompany("Gli Occhi del Cuore");
		String jsonCustomer2 = mapper.writeValueAsString(customer2);
		customers.insertOne(Document.parse(jsonCustomer2));
		System.out.println(jsonCustomer2);
		
		System.out.println("Inserimento completato");
		System.out.println();
		
		// Lettura di tutti gli elementi presenti nella collection customers
		System.out.println("Lettura di tutti gli elementi presenti nella collection customers");
		FindIterable<Document> allCustomers = customers.find();
		MongoCursor<Document> cursorCustomers = allCustomers.iterator();
		if(!cursorCustomers.hasNext())
			System.out.println("Collection vuota");
		else
		{
			i = 1;
			while(cursorCustomers.hasNext())
			{
				Document doc = cursorCustomers.next();
				System.out.println(i++ +") "+ doc.toJson());
//				Customer c = mapper.readValue(doc.toJson(), Customer.class);
			}
		}
		
		System.out.println("Lettura terminata");
	}
}
