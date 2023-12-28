package Pruefung2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class Main {

	private final static Path path = Path.of(
			"C:\\Users\\alexu\\OneDrive\\eclipse-workspace\\pruefung2\\ichStandUfHaerin\\src\\main\\java\\Pruefung2\\words.txt");

	private final static String solrURL = "http://localhost:8983/solr/SolrTestCore";

	private final static SolrClient solrClient = new HttpSolrClient.Builder(solrURL).build();

	public static void main(String[] args) throws SolrServerException, IOException {
		long startTime = System.nanoTime();

		createData(getWords());
		getData();

		long endTime = System.nanoTime();
		long durationInNano = (endTime - startTime);
		long durationInMillis = durationInNano / 1_000_000;
		long durationInSek = durationInNano / 1_000_000_000;
		System.out.println("Gesamtdauer: " + durationInSek + "s" + "\nGesamtdauer: " + durationInMillis + "milli");

		solrClient.close();
	}

	private static List<String> getWords() throws IOException {
		return Files.lines(path).flatMap(line -> Arrays.stream(line.split("\\s+"))).collect(Collectors.toList());
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

	private static void createData(List<String> words) throws SolrServerException, IOException {
		Random randomWord = new Random();
		int oldInt = 10;
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
			}
			Data data = new Data(i, title.toString(), text.toString());
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", data.getId());
			document.addField("title", data.getTitle());
			document.addField("text", data.getText());

			documents.add(document);
			if ((i + 1) % oldInt == 0 || i == 99999) {
				solrClient.add(documents);
				solrClient.commit();
				documents.clear();
			}
		}
	}

}
