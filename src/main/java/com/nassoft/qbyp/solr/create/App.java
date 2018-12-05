package com.nassoft.qbyp.solr.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.nassoft.qbyp.solr.create.hw.login.BDLoginUtil;
import com.nassoft.qbyp.solr.create.util.ConfigFilePathUtil;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableCaching
@ComponentScan
public class App {
	public static ConfigurableApplicationContext context;
	public static String act;
	public static String params;
	
	public static void main(String[] args) throws Exception {
		
		if(args!=null && args.length>0) {
			if(act==null) {
				act = args[0];
				if(act.equals("del")) {
					if(args.length == 1) {
						System.out.println("请输入删除参数！"); 
						return;
					}else if(args.length == 2) {
						Scanner sc=new Scanner(System.in);
						
						if(args[1]!=null && args[1].equals("all")) {
							System.out.println("输入删除参数为all，是否删除T_SOLR_CREATE_CONFIG字段为1(已创建)的collection？(确定输入:Y)");
						}else {
							System.out.println("是否删除collection:"+args[1]+"？(确定输入:Y)");
						}
						
						String isDelete=sc.nextLine();
						System.out.println("输入 "+isDelete);
						if(isDelete!=null && isDelete.length()>0 && isDelete.toUpperCase().equals("Y")) {
								params = args[1];
								sc.close();
						}else {
							System.out.println("取消删除！"); 
						}
						
						if(params==null) {
							return;
						}
					}
				}else if(act.equals("updateConfig")) {
					if(args.length == 1) {
						System.out.println("请输入更新配置参数！"); 
						return;
					}else if(args.length == 2) {
                        Scanner sc=new Scanner(System.in);
						
						System.out.println("是否更新配置文件为:"+args[1]+"的collection？(确定输入:Y)");
						
						String isUpdate=sc.nextLine();
						System.out.println("输入 "+isUpdate);
						if(isUpdate!=null && isUpdate.length()>0 && isUpdate.toUpperCase().equals("Y")) {
								params = args[1];
								sc.close();
						}else {
							System.out.println("取消更新！"); 
						}
						
						if(params==null) {
							return;
						}
					}
				}else if(act.equals("delByConfig")) {
					if(args.length == 1) {
						System.out.println("请输入删除参数！"); 
						return;
					}else if(args.length == 2) {
						Scanner sc=new Scanner(System.in);
						System.out.println("是否删除配置文件为:"+args[1]+"的collection？(确定输入:Y)");
						String isDelete=sc.nextLine();
						System.out.println("输入 "+isDelete);
						if(isDelete!=null && isDelete.length()>0 && isDelete.toUpperCase().equals("Y")) {
								params = args[1];
								sc.close();
						}else {
							System.out.println("取消删除！"); 
						}
						if(params==null) {
							return;
						}
					}
				}
			}
			
			String configFilePathQbyp  = ConfigFilePathUtil.getConfigPath("qbyp.properties");
			if(!StringUtils.isEmpty(configFilePathQbyp)) {
				Properties properties = new Properties();
		    	InputStream in = new FileInputStream(new File(configFilePathQbyp));
		        properties.load(in);
		        String isUseHw = (String) properties.get("isUseHw");
		        String userName = (String) properties.get("userName");
		        if(isUseHw.equals("1")) {
		        	if (!BDLoginUtil.loginDb(userName)) {
		        		throw new Exception("大数据集群登陆失败，平台无法正常启动，请联系管理员！");
		            }
		        }
			}
			System.setProperty("as.path",ConfigFilePathUtil.getJarDir());
			Properties properties = new Properties();
			InputStream in = new FileInputStream(new File(ConfigFilePathUtil.getConfigPath("application.properties")));
			properties.load(in);
			SpringApplication app = new SpringApplication(App.class);
			app.setDefaultProperties(properties);
			context = app.run(args);
		}else {
			System.out.println("请输入执行命令的参数！"); 
		}
		
		
	}
}
