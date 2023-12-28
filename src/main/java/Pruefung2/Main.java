package Pruefung2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class Main {

	private final static File wordList = new File(
			"C:\\Users\\alexu\\OneDrive\\eclipse-workspace\\pruefung2\\ichStandUfHaerin\\src\\main\\java\\Pruefung2\\words.txt");

	private final static String solrURL = "http://localhost:8983/solr/solrtest";

	private final static SolrClient solrClient = new HttpSolrClient.Builder(solrURL).build();

	public static void main(String[] args) throws SolrServerException, IOException {
	
		createData(getWords());
		getData();
		
		solrClient.close();
	}

	private static ArrayList<String> getWords() throws FileNotFoundException {
		
		ArrayList<String> words = new ArrayList<>();
		
		try (Scanner myScanner = new Scanner(wordList)) {
			while (myScanner.hasNext()) {
				words.add(myScanner.next());
			}
		}
		
		return words;
	}
	
	private static void getData() throws SolrServerException, IOException {

		SolrQuery query = new SolrQuery();
	    query.setQuery("*:*");
	    query.setRows(5);
	    
	    QueryResponse response = solrClient.query(query);
	    for (SolrDocument doc : response.getResults()) {
	        System.out.println(doc);
	    }
	}

	private static void createData(ArrayList<String> words) throws SolrServerException, IOException {
		Random randomWord = new Random();
		ArrayList<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
		for (int i = 0; i < 10; i++) {
	        StringBuilder text = new StringBuilder();
	        StringBuilder title = new StringBuilder();
	        int randomTextLength = randomWord.nextInt(501) + 1000;
			for (int j = 0; j < randomTextLength; j++) {
				String word = words.get(randomWord.nextInt(words.size()));
	            text.append(word).append(" ");
				if (j < 5) {
	                title.append(word).append(" ");
				}
				Data data = new Data(i, title.toString(), text.toString());
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", data.getId());
				document.addField("title", data.getTitle());
				document.addField("text", data.getText());

				documents.add(document);
			}
		}
		
		solrClient.add(documents);
		solrClient.commit();
	}

}
