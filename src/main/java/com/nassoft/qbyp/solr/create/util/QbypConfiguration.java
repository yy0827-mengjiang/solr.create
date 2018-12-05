package com.nassoft.qbyp.solr.create.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value={"file:${as.path}/config/qbyp.properties"})
public class QbypConfiguration {

//	@Value("${mappingId}")
//	private String mappingId;
	
	

}
