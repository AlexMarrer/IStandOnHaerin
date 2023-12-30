package Pruefung2.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import Pruefung2.Data;


public class DataLogic implements IDataLogic {

	private final static String solrURL = "http://localhost:8983/solr/SolrTestCore";

	private final static SolrClient solrClient = new HttpSolrClient.Builder(solrURL).build();
	
	@Override
	public void deleteAllDocuments() {
	    try {
	        solrClient.deleteByQuery("*:*");
	        solrClient.commit(); 
	    } catch (SolrServerException | IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void createData(List<String> words, int oldInt, int totalDocs) throws InterruptedException {
	    Random randomWord = new Random();
	    ExecutorService threads = Executors.newFixedThreadPool(8);

	    for (int i = 0; i < totalDocs; i += oldInt) {
	        final int start = i;
	        final int end = Math.min(i + oldInt, totalDocs);

	        threads.submit(() -> {
	            ArrayList<SolrInputDocument> threadDocuments = new ArrayList<>();
	            for (int index = start; index < end; index++) {
	                StringBuilder text = new StringBuilder();
	                StringBuilder title = new StringBuilder();
	                int randomTextLength = randomWord.nextInt(501) + 1000;
	                for (int j = 0; j < randomTextLength; j++) {
	                    String word = words.get(randomWord.nextInt(words.size()));
	                    text.append(word);

	                    if (randomWord.nextInt(10) == 0) { 
	                        text.append(", ");
	                    } else if (randomWord.nextInt(15) == 0) { 
	                        text.append(". ");
	                        if (j + 1 < randomTextLength) {
	                            words.set(randomWord.nextInt(words.size()), 
	                                      words.get(randomWord.nextInt(words.size())).substring(0, 1).toUpperCase() + 
	                                      words.get(randomWord.nextInt(words.size())).substring(1));
	                        }
	                    } else {
	                        text.append(" ");
	                    }

	                    if (j < 5) {
	                        title.append(word).append(" ");
	                    }
	                }

	                Data data = new Data(index, title.toString(), text.toString());
	                SolrInputDocument document = new SolrInputDocument();
	                document.addField("id", data.getId());
	                document.addField("title", data.getTitle());
	                document.addField("text", data.getText());

	                threadDocuments.add(document);
	            }

	            try {
	                solrClient.add(threadDocuments);
	                solrClient.commit();
	            } catch (SolrServerException | IOException e) {
	                e.printStackTrace();
	            }
	        });
	    }

	    threads.shutdown();
	    threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	}

	@Override
	public void getData() throws SolrServerException, IOException {

		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRows(5);

		QueryResponse response = solrClient.query(query);
		for (SolrDocument doc : response.getResults()) {
			System.out.println(doc);
		}
	}
	
	@Override
	public ArrayList<Data> getSpecificAmountOfData(int amountOfData) throws SolrServerException, IOException {
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRows(amountOfData);
		QueryResponse response = solrClient.query(query);
		SolrDocumentList documents = response.getResults();
		ArrayList<Data> dataList = new ArrayList<Data>();
		
		for (SolrDocument doc : documents) {
            int id = Integer.parseInt((String) doc.getFieldValue("id"));
            String title = doc.getFieldValue("title").toString();
            String text = doc.getFieldValue("text").toString();

            dataList.add(new Data(id, title, text));
		}
		return dataList;
	}
	
	@Override 
	public ArrayList<Data> getSolrData() throws SolrServerException, IOException {
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRows(10000);
		QueryResponse response = solrClient.query(query);
		SolrDocumentList documents = response.getResults();
		ArrayList<Data> dataList = new ArrayList<Data>();
		
		for (SolrDocument doc : documents) {
            int id = Integer.parseInt((String) doc.getFieldValue("id"));
            String title = doc.getFieldValue("title").toString();
            String text = doc.getFieldValue("text").toString();

            dataList.add(new Data(id, title, text));
		}
		return dataList;
	}
	
	@Override 
	public void closeSolr() throws IOException {
		solrClient.close();		
	}
	
	@Override
	public void timeEnd(long startTime) {
		long endTime = System.nanoTime();
		long durationInNano = (endTime - startTime);
		long durationInMillis = durationInNano / 1_000_000;
		long durationInSek = durationInNano / 1_000_000_000;
		System.out.println("Gesamtdauer: " + durationInSek + "s" + "\nGesamtdauer: " + durationInMillis + "milli");
	}
}
