package com.team33.gui;

import com.team33.model.statistics.BaseData;

import java.io.Serializable;
import java.util.HashSet;
import java.util.TreeSet;

class ChartData implements Serializable{

    private final HashSet<BaseData> data = new HashSet<>();

    ChartData(TreeSet<BaseData> data) {
        this.data.addAll(data);
    }

    TreeSet<BaseData> getData() {
        TreeSet<BaseData> baseData = new TreeSet<>();
        baseData.addAll(data);
        return baseData;
    }
}
