package Pruefung2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrServerException;

import Pruefung2.Logic.DataLogic;
import Pruefung2.Logic.IDataLogic;

public class Main {

	private final static Path path = Path.of(
			"C:\\Users\\alexu\\OneDrive\\eclipse-workspace\\pruefung2\\ichStandUfHaerin\\src\\main\\java\\Pruefung2\\words.txt");

	public static void main(String[] args) throws SolrServerException, IOException, InterruptedException {
		long startTime = System.nanoTime();
		IDataLogic dataLogic = new DataLogic();
	    int oldInt = 10000; // Miau
	    int totalDocs = 100000;
		
		dataLogic.deleteAllDocuments();
		dataLogic.createData(getWords(), oldInt, totalDocs);
		dataLogic.getData();

		dataLogic.timeEnd(startTime);

		dataLogic.closeSolr();
	}
	
	private static List<String> getWords() throws IOException {
		return Files.lines(path)
				.flatMap(line -> Arrays.stream(line.split("\\s+")))
				.collect(Collectors.toList());
	}
}
