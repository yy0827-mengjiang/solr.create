package com.nassoft.qbyp.solr.create.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nassoft.qbyp.solr.create.mysql.IRdbService;
import com.nassoft.qbyp.solr.create.solr.ISolrService;

@Service
public class SolrCreateService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IRdbService rdbService;
	
	@Autowired
	private ISolrService solrService;

	public void createSolrCore() {
		String sql = " select COLLECTION_NAME,CONFIG_NAME,NUM_SHARDS,REPLICATION_FACTOR,IS_CREATED from T_SOLR_CREATE_CONFIG where IS_CREATED = 0 " ;
		List<Map<String, Object>> list = rdbService.getTransactionJdbcTemplate().queryForList(sql);
		if(list!=null) {
			list.forEach(v ->{
				String collection = (String) v.get("COLLECTION_NAME");
				Integer numShards = (Integer) v.get("NUM_SHARDS");
				String configName = (String) v.get("CONFIG_NAME");
				Integer replicationFactor = (Integer) v.get("REPLICATION_FACTOR");
				try {
					boolean isSuccess = solrService.createCore(collection, numShards, configName, replicationFactor);
					if(isSuccess) {
						String updateSql = "update T_SOLR_CREATE_CONFIG set IS_CREATED = 1 where COLLECTION_NAME='"+collection+"'";
						rdbService.getTransactionJdbcTemplate().update(updateSql);
						logger.info("创建collection"+collection+"成功！");
					}else {
						logger.info("创建collection"+collection+"失败！");
					}
					
				} catch (Exception e) {
					logger.error("创建collection"+collection+"出错！",e);
				}
				//暂停10秒
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	public void deleteSolrCore() {
		String sql = " select COLLECTION_NAME,CONFIG_NAME,NUM_SHARDS,REPLICATION_FACTOR,IS_CREATED from T_SOLR_CREATE_CONFIG where IS_CREATED = 1 " ;
		List<Map<String, Object>> list = rdbService.getTransactionJdbcTemplate().queryForList(sql);
		if(list!=null) {
			list.forEach(v ->{
				String collection = (String) v.get("COLLECTION_NAME");
				try {
					boolean isSuccess = solrService.deleteCore(collection);
					if(!isSuccess) {
						logger.info("删除collection"+collection+"失败！");
					}
				} catch (Exception e) {
					logger.error("删除collection"+collection+"出错！",e);
				}
				//暂停10秒
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	
	public void deleteSolrCoreByName(String name) {
		try {
			boolean isSuccess = solrService.deleteCore(name);
			if(!isSuccess) {
				logger.info("删除collection"+name+"失败！");
			}
		} catch (Exception e) {
			logger.error("删除collection"+name+"出错！",e);
		}
	}
	
	public void reloadyCoreByName(String name) {
		String sql = " select COLLECTION_NAME,CONFIG_NAME,NUM_SHARDS,REPLICATION_FACTOR,IS_CREATED from T_SOLR_CREATE_CONFIG where CONFIG_NAME ='"+name+"' " ;
		List<Map<String, Object>> list = rdbService.getTransactionJdbcTemplate().queryForList(sql);
		if(list!=null) {
			list.forEach(v ->{
				String collection = (String) v.get("COLLECTION_NAME");
				try {
					boolean isSuccess = solrService.reloadCore(collection);
					if(!isSuccess) {
						logger.info("更新collection"+collection+"配置文件失败！");
					}else {
						logger.info("更新collection"+collection+"配置文件成功！");
					}
				} catch (Exception e) {
					logger.error("更新collection"+collection+"配置文件出错！",e);
				}
				//暂停10秒
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	
	public void deleteSolrCoreByConfig(String name) {
		String sql = " select COLLECTION_NAME,CONFIG_NAME,NUM_SHARDS,REPLICATION_FACTOR,IS_CREATED from T_SOLR_CREATE_CONFIG where CONFIG_NAME ='"+name+"' " ;
		List<Map<String, Object>> list = rdbService.getTransactionJdbcTemplate().queryForList(sql);
		if(list!=null) {
			list.forEach(v ->{
				String collection = (String) v.get("COLLECTION_NAME");
				try {
					boolean isSuccess = solrService.deleteCore(collection);
					if(!isSuccess) {
						logger.info("删除collection"+collection+"失败！");
					}else {
						String updateSql = "update T_SOLR_CREATE_CONFIG set IS_CREATED = 0 where COLLECTION_NAME='"+collection+"'";
						rdbService.getTransactionJdbcTemplate().update(updateSql);
						logger.info("删除collection"+collection+"成功！");
					}
				} catch (Exception e) {
					logger.error("删除collection"+collection+"出错！",e);
				}
				//暂停10秒
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
}
