package com.nassoft.qbyp.solr.create.hw.login;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nassoft.qbyp.solr.create.util.ConfigFilePathUtil;

import java.io.File;
import java.io.IOException;

/**
 * @program: BDLoginUtil
 * @description: bigdata login util
 * @author: pengxy
 * @create: 2018-06-21 19:10
 **/
public class BDLoginUtil {
    private static final Logger logger = LoggerFactory.getLogger(BDLoginUtil.class);
    private static final String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
    private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop.hadoop.com";
    private static Configuration loginedConf = null;

    public static Configuration getLoginedConf() {
        return loginedConf;
    }

    public synchronized static boolean loginDb(String userName) {
        return loginSolr(userName);
    }

    private static boolean loginSolr(String userName) {
        boolean loginStatus = true;
        String path = ConfigFilePathUtil.getJarDir()+ File.separator+ "config" + File.separator;
        //String path = System.getProperty("user.dir") + File.separator + "conf" + File.separator;
        path = path.replace("\\", "/");
        logger.info("==============================================================");
        logger.info(path);
        try {
            String principal = userName;
            HWLoginUtil.setJaasFile(principal, path + "user.keytab");
            HWLoginUtil.setKrb5Config(path + "krb5.conf");
            HWLoginUtil.setZookeeperServerPrincipal("zookeeper/hadoop.hadoop.com");
        } catch (IOException e) {
            loginStatus = false;
            logger.info("Failed to set security conf");
        }
        return loginStatus;
    }
    
    private static boolean loginHBase() {
        boolean loginStatus = true;
        Configuration conf = HBaseConfiguration.create();
        try {
            if (User.isHBaseSecurityEnabled(conf)) {
                logger.info("hbase need login");
                String userdir = ConfigFilePathUtil.getJarDir()+ File.separator+ "config" + File.separator;
                //String userdir = System.getProperty("user.dir") + File.separator + "conf" + File.separator;
                String userName = "nassoft_m";
                String userKeytabFile = userdir + "user.keytab";
                String krb5File = userdir + "krb5.conf";

                HWLoginUtil.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME, userName, userKeytabFile);
                HWLoginUtil.setZookeeperServerPrincipal(ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);
                HWLoginUtil.login(userName, userKeytabFile, krb5File, conf);
                loginedConf = conf;
            }
        } catch (IOException e) {
            loginedConf = null;
            loginStatus = false;
            logger.info("hbase login fail");
        }
        return loginStatus;
    }

    
}
