package pruefung2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

import Pruefung2.Data;

@SuppressWarnings("deprecation")
public class DataEncapsulation {

    private static String file = "words.txt";
    
    private static ArrayList<String> words;

    
    public static void main(String[] args) throws IOException, SolrServerException {
    	
        int amountObject = 10000;
        String urlString = "http://localhost:8983/solr/SolrTestCore";
        HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
        solr.setParser(new XMLResponseParser());
	    try {
	        solr.deleteByQuery("*:*");
	        solr.commit(); 
	    } catch (SolrServerException | IOException e) {
	        e.printStackTrace();
	    }
	    long startTime = System.nanoTime();
	    words = getWordsFromFile();
        List<SolrInputDocument> documents = generateAndIndexData(amountObject, solr);

        indexData(solr, documents);
        
		long endTime = System.nanoTime();
		long durationInNano = (endTime - startTime);
		long durationInMillis = durationInNano / 1_000_000;
		long durationInSek = durationInNano / 1_000_000_000;
		System.out.println("Gesamtdauer: " + durationInSek + "s" + "\nGesamtdauer: " + durationInMillis + "milli");
    }

    private static List<SolrInputDocument> generateAndIndexData(int amountObject, HttpSolrClient solr)
            throws IOException, SolrServerException {
        ArrayList<Data> data = new ArrayList<>(amountObject);
        List<SolrInputDocument> documents = new ArrayList<>(amountObject);

        IntStream.range(0, amountObject).parallel().forEach(i -> {
            String text = buildText(words);
			String title = buildTitle(text);

			String normalizedText = normalizeText(text);
			String normalizedTitle = normalizeText(title);

			Data newData = new Data(i + 1, normalizedTitle, normalizedText);
			data.add(newData);

			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", newData.getId());
			document.addField("titel", newData.getTitle());
			document.addField("text", newData.getText());
			documents.add(document);
        });

        return documents;
    }

    private static void indexData(HttpSolrClient solr, List<SolrInputDocument> documents)
            throws SolrServerException, IOException {
        int batchSize = 1000;
        for (int i = 0; i < documents.size(); i += batchSize) {
            List<SolrInputDocument> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
            solr.add(batch);
            solr.commit();
        }
    }

    private static String normalizeText(String text) {
        return text.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss").replace("Ä", "Ae")
                .replace("Ü", "ue").replace("Ö", "Oe");
    }

    private static String buildText(ArrayList<String> words) {
        int targetWordCount = ThreadLocalRandom.current().nextInt(501) + 1000;
        StringBuilder text = new StringBuilder(targetWordCount * 5);

        int wordCount = 0;
        while (wordCount < targetWordCount) {
            String word = words.get(ThreadLocalRandom.current().nextInt(words.size()));
            text.append(word);

            if (wordCount < targetWordCount - 1) {
                if (ThreadLocalRandom.current().nextDouble() < 0.05) {
                    text.append(", ");
                } else if (ThreadLocalRandom.current().nextDouble() < 0.02) {
                    text.append(".");
                    if (wordCount < targetWordCount - 1) {
                        text.append(" ");
                    }
                    capitalizeNextWord(text);
                } else {
                    text.append(" ");
                }
            }
            wordCount++;
        }

        return text.toString();
    }

    private static void capitalizeNextWord(StringBuilder text) {
        if (text.length() == 1 || (Character.isWhitespace(text.charAt(text.length() - 2)))) {
            text.setCharAt(text.length() - 1, Character.toUpperCase(text.charAt(text.length() - 1)));
        }
    }

    private static String buildTitle(String fullText) {
        String[] words = fullText.split("\\s+");
        int maxLength = Math.min(5, words.length);

        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            String word = words[i].replaceAll("[,\\.]", "");
            titleBuilder.append(word).append(" ");
        }

        return titleBuilder.toString().trim();
    }

    private static ArrayList<String> getWordsFromFile() throws IOException {
        ArrayList<String> words = new ArrayList<>();
        try (InputStream is = DataEncapsulation.class.getResourceAsStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
        }
        return words;
    }
}