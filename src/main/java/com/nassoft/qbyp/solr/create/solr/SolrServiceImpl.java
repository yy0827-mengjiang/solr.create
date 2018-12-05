package com.nassoft.qbyp.solr.create.solr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nassoft.qbyp.solr.create.util.ConfigFilePathUtil;

@Service
public class SolrServiceImpl implements ISolrService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private SolrClient solrClient;

	@Autowired
	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}

	@Override
	public QueryResponse query(String collection, SolrParams params) throws Exception {
		return solrClient.query(collection, params);
	}

	@Override
	public void update(String collection, List<SolrInputDocument> docs) {
		try {
			solrClient.add(collection, docs);
			solrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean createCore(String collection, Integer numShards, String configName, Integer replicationFactor) {
		CollectionAdminRequest.Create createCmd = CollectionAdminRequest.createCollection(collection, configName,
				numShards, replicationFactor);
		// int liveNodes =
		// solrClient.getZkStateReader().getClusterState().getLiveNodes().size();
		String configFilePathQbyp = ConfigFilePathUtil.getConfigPath("qbyp.properties");
		Properties properties = new Properties();
		try (InputStream in = new FileInputStream(new File(configFilePathQbyp))) {
			properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Integer liveNodes = Integer.parseInt(properties.getProperty("liveNodes"));
		int maxShardsPerNode = ((int) Math.max(Math.ceil((numShards * replicationFactor) / liveNodes), 1))+1;
		logger.info("liveNodes=====>"+liveNodes);
		logger.info("maxShardsPerNode=====>"+maxShardsPerNode);
		// Integer maxShardsPerNode = numShards * replicationFactor;
		createCmd.setMaxShardsPerNode(maxShardsPerNode);
		logger.info("Creating new collection " + collection + " with " + numShards + " shards and " + replicationFactor
				+ " replicas per shard");
		try {
			solrClient.request(createCmd);
		} catch (Exception exc) {
			logger.error("Failed to create collection " + collection + " due to: " + exc, exc);
			return false;
		}
		return true;
	}

	public boolean deleteCore(String collection) {
		CollectionAdminRequest.Delete deleteCmd = CollectionAdminRequest.deleteCollection(collection);
		try {
			solrClient.request(deleteCmd);
		} catch (Exception exc) {
			String excStr = exc.toString();
			if(excStr.contains("Could not find collection")) {
				logger.info("collection:"+collection+"不存在");
			}else {
				logger.error("Failed to delete collection " + collection + " due to: " + exc, exc);
			}
			return false;
		}
		return true;
	}
	
	public boolean reloadCore(String collection) {
		CollectionAdminRequest.Reload reloadCmd = CollectionAdminRequest.reloadCollection(collection);
		try {
			solrClient.request(reloadCmd);
		} catch (Exception exc) {
			String excStr = exc.toString();
			if(excStr.contains("Could not find collection")) {
				logger.info("collection:"+collection+"不存在");
			}else {
				logger.error("Failed to reload collection " + collection + " due to: " + exc, exc);
			}
			return false;
		}
		return true;
	}
}
