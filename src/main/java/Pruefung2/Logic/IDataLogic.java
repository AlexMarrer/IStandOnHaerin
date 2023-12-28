package Pruefung2.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import Pruefung2.Data;

public interface IDataLogic {

	void createData(List<String> words) throws InterruptedException;
	
	void getData() throws SolrServerException, IOException;

	ArrayList<Data> getSolrData() throws SolrServerException, IOException;
	
	void closeSolr() throws IOException;
}
