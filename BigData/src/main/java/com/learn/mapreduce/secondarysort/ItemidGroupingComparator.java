package com.learn.mapreduce.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by hechao on 2017/8/8.
 */
public class ItemidGroupingComparator extends WritableComparator {

    //传入作为key的bean的class类型，以及制定需要让框架做反射获取实例对象
    protected ItemidGroupingComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean abean = (OrderBean) a;
        OrderBean bbean = (OrderBean) b;

        //比较两个bean时，指定只比较bean中的orderid
        return abean.getItemid().compareTo(bbean.getItemid());
    }
}
