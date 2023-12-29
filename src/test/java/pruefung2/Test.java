package pruefung2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import Pruefung2.Data;

@SuppressWarnings("deprecation")

public class Test {

	public static void main(String[] args) throws SolrServerException, IOException {
		String urlString = "http://localhost:8983/solr/SolrTestCore";
		HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
		solr.setParser(new XMLResponseParser());

		ArrayList<Data> data = new ArrayList<>();

		// Test 1
		long startTime1 = System.nanoTime();
		SolrQuery query1 = new SolrQuery();
		query1.set("q", "text:Kokain");

		QueryResponse response1 = solr.query(query1);

		SolrDocumentList docList1 = response1.getResults();

		for (SolrDocument doc1 : docList1) {
			System.out.println(doc1.getFieldValue("id") + " -> " + doc1.getFieldValue("text"));
			Data data1 = new Data(Integer.parseInt(doc1.getFieldValue("id").toString()),
					doc1.getFieldValue("title").toString(), doc1.getFieldValue("text").toString());
			data.add(data1);
		}

		long endTime1 = System.nanoTime();
		long durationInNano1 = (endTime1 - startTime1);
		long durationInMillis1 = durationInNano1 / 1_000_000;
		long durationInSek1 = durationInNano1 / 1_000_000_000;
		System.out.println("Gesamtdauer: " + durationInSek1 + "s" + "\nGesamtdauer: " + durationInMillis1 + "milli");

		// Test 2
		long startTime2 = System.nanoTime();
		SolrQuery query2 = new SolrQuery();
		query2.set("q", "text:Heroin");

		QueryResponse response2 = solr.query(query2);

		SolrDocumentList docList2 = response2.getResults();

		for (SolrDocument doc2 : docList2) {
			System.out.println(doc2.getFieldValue("id") + " -> " + doc2.getFieldValue("text"));
			Data data2 = new Data(Integer.parseInt(doc2.getFieldValue("id").toString()),
					doc2.getFieldValue("title").toString(), doc2.getFieldValue("text").toString());
			data.add(data2);
		}

		long endTime2 = System.nanoTime();
		long durationInNano2 = (endTime2 - startTime2);
		long durationInMillis2 = durationInNano2 / 1_000_000;
		long durationInSek2 = durationInNano2 / 1_000_000_000;
		System.out.println(
				"Gesamtdauer Test 2: " + durationInSek2 + "s" + "\nGesamtdauer Test 2: " + durationInMillis2 + "milli");

		// Test 3
		long startTime3 = System.nanoTime();
		SolrQuery query3 = new SolrQuery();
		query3.set("q", "text:Hilfe");

		QueryResponse response3 = solr.query(query3);

		SolrDocumentList docList3 = response3.getResults();

		for (SolrDocument doc3 : docList3) {
			System.out.println(doc3.getFieldValue("id") + " -> " + doc3.getFieldValue("text"));
			Data data3 = new Data(Integer.parseInt(doc3.getFieldValue("id").toString()),
					doc3.getFieldValue("title").toString(), doc3.getFieldValue("text").toString());
			data.add(data3);
		}

		long endTime3 = System.nanoTime();
		long durationInNano3 = (endTime3 - startTime3);
		long durationInMillis3 = durationInNano3 / 1_000_000;
		long durationInSek3 = durationInNano3 / 1_000_000_000;
		System.out.println(
				"Gesamtdauer Test 2: " + durationInSek3 + "s" + "\nGesamtdauer Test 2: " + durationInMillis3 + "milli");

		// Test 4
		long startTime4 = System.nanoTime();
		SolrQuery query4 = new SolrQuery();
		query4.set("q", "text:Kevin");

		QueryResponse response4 = solr.query(query4);

		SolrDocumentList docList4 = response4.getResults();

		for (SolrDocument doc4 : docList4) {
			System.out.println(doc4.getFieldValue("id") + " -> " + doc4.getFieldValue("text"));
			Data data4 = new Data(Integer.parseInt(doc4.getFieldValue("id").toString()),
					doc4.getFieldValue("title").toString(), doc4.getFieldValue("text").toString());
			data.add(data4);
		}

		long endTime4 = System.nanoTime();
		long durationInNano4 = (endTime4 - startTime4);
		long durationInMillis4 = durationInNano4 / 1_000_000;
		long durationInSek4 = durationInNano4 / 1_000_000_000;
		System.out.println(
				"Gesamtdauer Test 4: " + durationInSek4 + "s" + "\nGesamtdauer Test 4: " + durationInMillis4 + "milli");

		// Test 5
		long startTime5 = System.nanoTime();
		SolrQuery query5 = new SolrQuery();
		query5.set("q", "text:Drogen");

		QueryResponse response5 = solr.query(query5);

		SolrDocumentList docList5 = response5.getResults();

		for (SolrDocument doc5 : docList5) {
			System.out.println(doc5.getFieldValue("id") + " -> " + doc5.getFieldValue("text"));
			Data data5 = new Data(Integer.parseInt(doc5.getFieldValue("id").toString()),
					doc5.getFieldValue("title").toString(), doc5.getFieldValue("text").toString());
			data.add(data5);
		}

		long endTime5 = System.nanoTime();
		long durationInNano5 = (endTime5 - startTime5);
		long durationInMillis5 = durationInNano5 / 1_000_000;
		long durationInSek5 = durationInNano5 / 1_000_000_000;
		System.out.println(
				"Gesamtdauer Test 5: " + durationInSek5 + "s" + "\nGesamtdauer Test 5: " + durationInMillis5 + "milli");

		// getINfo

		for (Data get : data) {
			get.getInfo();
		}
	}

}