package com.nassoft.qbyp.solr.create.solr;

import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

public interface ISolrService {
    QueryResponse query(String collection, SolrParams params) throws Exception;
    void update(String collection,List<SolrInputDocument> docs);
    boolean createCore(String collection,Integer numShards,String configName,Integer replicationFactor) throws Exception;
    boolean deleteCore(String collection);
    boolean reloadCore(String collection);
}
