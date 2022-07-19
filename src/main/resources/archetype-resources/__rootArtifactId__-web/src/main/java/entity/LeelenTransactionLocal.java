#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LeelenTransactionLocal {
    private Logger logger = LoggerFactory.getLogger(LeelenTransactionLocal.class);
    private static final ThreadLocal<LeelenTransactionLocal> currentLocal = new ThreadLocal();
    public static Map<String, Transaction> connectionMap = new ConcurrentHashMap();
    private String groupId;
    private boolean isStart = false;
    private boolean isTransaction = false;
    private long maxTransactionTime;
    public LeelenTransactionLocal() {
    }

    public static LeelenTransactionLocal current() {
        return (LeelenTransactionLocal)currentLocal.get();
    }
    public static void revmoeCurrent() {
        currentLocal.remove();
    }
    public static void setCurrent(LeelenTransactionLocal current) {
        currentLocal.set(current);
    }

    public boolean isStart() {
        return this.isStart;
    }

    public void setStart(boolean start) {
        this.isStart = start;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isTransaction() {
        return this.isTransaction;
    }

    public void setTransaction(boolean transaction) {
        this.isTransaction = transaction;
    }

    public long getMaxTransactionTime() {
        return maxTransactionTime;
    }

    public void setMaxTransactionTime(long maxTransactionTime) {
        this.maxTransactionTime = maxTransactionTime;
    }
}
