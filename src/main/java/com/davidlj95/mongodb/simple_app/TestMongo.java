package com.davidlj95.mongodb.simple_app;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.result.UpdateResult;

public class TestMongo {

	public static void main(String[] args) {
		// Create connection and get database and collection
		System.out.println("Opening connection to the database");
		MongoClient mongoClient = new MongoClient("servidor.davidlj95.com", 27017);
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> collection = database.getCollection("mini");
		System.out.println("Connected to MongoDB server");
		// Exercici 1
		System.out.println("==============================");
		System.out.println("EX01: Obtenir documents");
		System.out.println("       -> location = Barcelona");
		System.out.println("       -> ordenats ascendentment per cognom");
		System.out.println("==============================");
		MongoIterable<Document> results = 
				collection.find(new Document("location", "Barcelona")).sort(
				Sorts.ascending("surname"));
		printResults(results);
		// Exercici 2
		System.out.println("==============================");
		System.out.println("EX02: Modificar documents");
		System.out.println("       -> seleccionar documents amb:");
		System.out.println("          \"Samsung TV 65 QLEV\" o \"TV LG OLED de 139cm Full HD\"");
		System.out.println("          dins de suggProd");
		System.out.println("       -> inserir \"Phillips OLED 4K\" a suggProducts");
		System.out.println("==============================");
		Bson ex2_filter = Filters.in("suggProd", new String[]{ "Samsung TV 65 QLEV", "TV LG OLED de 139cm Full HD" });
		Bson ex2_update = Updates.addToSet("suggProd", "Phillips OLED 4K");
		UpdateResult result_update = collection.updateMany(ex2_filter, ex2_update);
		System.out.println(String.format("Update result: %s", result_update.toString()));
		printResults(collection.find(ex2_filter));
		// Exercici 3
		System.out.println("==============================");
		System.out.println("EX03: Modificar documents");
		System.out.println("       -> Renombrar camp");
		System.out.println("          suggProd -> suggestedProducts");
		System.out.println("==============================");
		Bson ex3_update = Updates.rename("suggProd", "suggestedProducts");
		result_update = collection.updateMany(new Document(), ex3_update);
		System.out.println(String.format("Update result: %s", result_update.toString()));
		printResults(collection.find(new Document()));
		// Close
		mongoClient.close();
		
	}
	
	public static void printResults(MongoIterable<Document> results) {
		int i = 0;
		for(Document result : results) {
			System.out.println(result.toString());
			i += 1;
		}
		System.out.println(String.format("S'han trobat %d resultats", i));
	}

}
