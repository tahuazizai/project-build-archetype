#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @version: 1.00.00
 * @description: 拷贝对象
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-03-29 9:01
 */
@Slf4j
public class CloneUtils {

    /**
     * 拷贝对象
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T cloneObject(T obj) {
        T cloneObj = null;
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut);) {
            out.writeObject(obj);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            cloneObj = (T) in.readObject();
        } catch (IOException e) {
            log.error("io流异常", e);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException", e);
        }
        return cloneObj;
    }

    /**
     * 复制list对象
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends Serializable> List<T> cloneList(List<T> list) {
        List<T> cloneList = null;
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut);) {
            out.writeObject(list);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            cloneList = (List<T>) in.readObject();
        } catch (IOException e) {
            log.error("io流异常", e);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException", e);
        }
        return cloneList;
    }
}
