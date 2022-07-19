#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.seata;

import ${groupId}.utils.SpringUtil;
import io.seata.config.nacos.NacosConfiguration;
import io.seata.tm.api.FailureHandler;
import io.seata.tm.api.GlobalTransaction;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-09-09 9:54
 */
public class SeataRollBackFailHandle implements FailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(SeataRollBackFailHandle.class);

    StringEncryptor stringEncryptor = null;


    String dbUrl ="";
    String userName ="";
    String password ="";
    String driverClassName ="";

    public void initDB(){
        NacosConfiguration configuration = NacosConfiguration.getInstance();
        dbUrl = configuration.getConfig("store.db.url");
        userName = configuration.getConfig("store.db.user");
        password = configuration.getConfig("store.db.password");
        if(password.startsWith("ENC")){
            stringEncryptor = SpringUtil.getBean(StringEncryptor.class);
            password = stringEncryptor.decrypt(password.substring(4,password.length()-1));
        }
        driverClassName = configuration.getConfig("store.db.driverClassName");
        logger.info("init FailHandle success url:{} userName:{} password:{}",dbUrl,userName,password);
    }

    @Override
    public void onBeginFailure(GlobalTransaction tx, Throwable cause) {
        logger.error("onBeginFailure {}",tx.getXid());
    }

    @Override
    public void onCommitFailure(GlobalTransaction tx, Throwable cause) {
        logger.error("onCommitFailure {}",tx.getXid());
    }

    @Override
    public void onRollbackFailure(GlobalTransaction tx, Throwable originalException) {
        logger.error("onRollbackFailure {}",tx.getXid());
    }

    @Override
    public void onRollbackRetrying(GlobalTransaction tx, Throwable originalException) {
        logger.error("onRollbackRetrying {}",tx.getXid());
        try {
            initDB();
            Class.forName(driverClassName);
            Connection connection = DriverManager.getConnection(dbUrl, userName, password);
            Statement statement = connection.createStatement();
            //查询
            String globalTableSql ="select application_id,transaction_name from global_table where xid='"+tx.getXid()+"'";
            ResultSet resultSet = statement.executeQuery(globalTableSql);
            String applicationId = null;
            String transactionName = null;
            while (resultSet.next()){
                applicationId=resultSet.getString(1);
                transactionName=resultSet.getString(2);
            }
            String sql ="delete from branch_table where xid='"+tx.getXid()+"'";
            int i = statement.executeUpdate(sql);
            logger.error("删除数量branch_table:{}",i);
            String sql2 ="delete from lock_table where xid='"+tx.getXid()+"'";
            int i2 = statement.executeUpdate(sql2);
            logger.error("删除数量lock_table:{}",i2);
            SeataSendMail.send1("分布式事务出现脏数据","组件:"+applicationId+";   方法名:"+transactionName+";   全局事务Id:"+tx.getXid());
        } catch (ClassNotFoundException e) {
            logger.error("{}:{}",e.getMessage(),e.getCause());
            e.printStackTrace();
        } catch (SQLException throwables) {
            logger.error("{}:{}",throwables.getMessage(),throwables.getCause());
        } catch (Exception e) {
            logger.error("{}:{}",e.getMessage(),e.getCause());
        }
    }


    /**
     * 解密
     * @param ciphertext 密文
     * @return           明文
     */
    public static String decrypt(String ciphertext) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
      //  encryptor.setPassword(KEY);
        return encryptor.decrypt(ciphertext);
    }
}
