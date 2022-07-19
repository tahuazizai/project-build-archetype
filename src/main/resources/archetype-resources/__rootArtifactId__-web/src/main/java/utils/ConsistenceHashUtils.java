#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import ${package}.constant.ResultCodeConstant;
import ${groupId}.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @version: 1.00.00
 * @description: 一致性hash算法
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-04-28 10:36
 */
@Component
public class ConsistenceHashUtils {

    /**
     * 虚拟节点映射关系
     */
    private  SortedMap<Integer, String> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private  static final int VIRTUAL_NODE_NUM = 1000;

    /**
     * 计算Hash值, 使用FNV1_32_HASH算法
     *
     * @param str
     * @return
     */
    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    /**
     * 构造虚拟节点
     * @param realNodeList
     * @param virtualNum
     */
    public  void buildNodeNum(List<String> realNodeList, Integer virtualNum) {
        if (CollectionUtils.isEmpty(realNodeList)) {
            throw new BusinessException(String.valueOf(ResultCodeConstant.ILLEGAL));
        }
        virtualNodes.clear();
        virtualNum = Objects.isNull(virtualNum) ? VIRTUAL_NODE_NUM : virtualNum;
        for (String node : realNodeList) {
            for (int i = 0; i < virtualNum; i++) {
                String virtualNodeName = getVirtualNodeName(node, i);
                int hash = getHash(virtualNodeName);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }

    private  String getVirtualNodeName(String realName, int num) {
        return realName + "&&VN" + num;
    }

    private  String getRealNodeName(String virtualName) {
        return virtualName.split("&&")[0];
    }

    public  String getServer(String widgetKey) {
        int hash = getHash(widgetKey);
        // 只取出所有大于该hash值的部分而不必遍历整个Tree
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        String virtualNodeName;
        if (subMap.isEmpty()) {
            // hash值在最尾部，应该映射到第一个group上
            virtualNodeName = virtualNodes.get(virtualNodes.firstKey());
        } else {
            virtualNodeName = subMap.get(subMap.firstKey());
        }
        return getRealNodeName(virtualNodeName);
    }

}
