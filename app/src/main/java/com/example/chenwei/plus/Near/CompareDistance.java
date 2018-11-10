package com.example.chenwei.plus.Near;

import com.example.chenwei.plus.Tool.Near_resource_information;

import java.util.Comparator;

public class CompareDistance implements Comparator{
    public int compare(Object o1,Object o2) {
        Near_resource_information e1=(Near_resource_information)o1;
        Near_resource_information e2=(Near_resource_information)o2;
        if(e1.getDistance()<e2.getDistance())
            return 1;
        else
            return 0;
    }
}

