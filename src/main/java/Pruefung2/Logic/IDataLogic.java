package Pruefung2.Logic;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

public interface IDataLogic {

	void createData(List<String> words) throws InterruptedException;
	
	void getData() throws SolrServerException, IOException;

	void closeSolr() throws IOException;
}
