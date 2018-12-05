package com.nassoft.qbyp.solr.create.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nassoft.qbyp.solr.create.App;
import com.nassoft.qbyp.solr.create.service.SolrCreateService;

@Component
public class InitSolr implements CommandLineRunner{
	
	private static final Logger logger = LoggerFactory
			.getLogger(InitSolr.class);
	
	@Autowired
	SolrCreateService solrCreateService;
	
	@Override
	public void run(String... args) throws Exception {

		
		try {
			
			if(App.act!=null && App.act.length()>0) {
				if(App.act.equals("add")) {
					logger.info("创建solr core开始");
					solrCreateService.createSolrCore();
					logger.info("创建solr core结束");
				}else if(App.act.equals("del")) {
					logger.info("删除solr core开始");
					if(App.params!=null && App.params.equals("all")) {
						solrCreateService.deleteSolrCore();
					}else if(App.params!=null) {
						solrCreateService.deleteSolrCoreByName(App.params);
					}
					logger.info("删除solr core结束");
				}else if(App.act.equals("updateConfig")) {
					logger.info("更新solr core开始");
					solrCreateService.reloadyCoreByName(App.params);
					logger.info("更新solr core结束");
				}else if(App.act.equals("delByConfig")) {
					logger.info("根据配置文件删除solr core开始");
					solrCreateService.deleteSolrCoreByConfig(App.params);
					logger.info("根据配置文件删除solr core结束");
				}
			}
			
		} catch (Exception e) {
			logger.error("创建solr core异常",e);
		}
		System.exit(0);
	}

	
	
}
