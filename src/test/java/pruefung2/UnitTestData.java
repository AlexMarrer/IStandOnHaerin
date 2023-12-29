package pruefung2;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Pruefung2.Logic.DataLogic;
import Pruefung2.Logic.IDataLogic;
import Pruefung2.Logic.IVerifyLogic;
import Pruefung2.Logic.VerifyLogic;

public class UnitTestData {

	private final static Path path = Path.of(
			"C:\\Users\\alexu\\OneDrive\\eclipse-workspace\\pruefung2\\ichStandUfHaerin\\src\\main\\java\\Pruefung2\\words.txt");

	static IDataLogic dataLogic;
	IVerifyLogic verifyLogic;

	@BeforeEach
	public void setUp() {
		dataLogic = new DataLogic();
		verifyLogic = new VerifyLogic();
	}

	@AfterAll
	public static void afterAll() throws IOException {
		dataLogic.closeSolr();
	}

	@Test
	public void getAllObjects() throws InterruptedException, IOException, SolrServerException {
		int oldInt = 1000; // Miau
		int totalDocs = 10000;

		dataLogic.deleteAllDocuments();
		dataLogic.createData(getWords(), oldInt, totalDocs);

		assertEquals(totalDocs, dataLogic.getSolrData().size());
	}

	@Test
	public void verifyIfTitleIsRight() throws InterruptedException, IOException, SolrServerException {
		int oldInt = 1000; // Miau
		int totalDocs = 10000;

		dataLogic.deleteAllDocuments();
		dataLogic.createData(getWords(), oldInt, totalDocs);

		assertTrue(verifyLogic.verifyTitle(dataLogic.getSolrData()));
	}
	
	@Test
	public void getSpecificNumbersOfDocs() throws InterruptedException, IOException, SolrServerException {
		int oldInt = 1000; // Miau
		int totalDocs = 10000;
		int specificAmount = 10;

		dataLogic.deleteAllDocuments();
		dataLogic.createData(getWords(), oldInt, totalDocs);
		
		assertTrue(verifyLogic.verifyAmountOfData(dataLogic.getSpecificAmountOfData(specificAmount), specificAmount));
	}

	private static List<String> getWords() throws IOException {
		return Files.lines(path).flatMap(line -> Arrays.stream(line.split("\\s+"))).collect(Collectors.toList());
	}
}
