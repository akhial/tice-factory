package com.team33.model.statistics;

import java.io.Serializable;

/**
 * Created by dell on 24/04/2017.
 */
public class BaseData implements Comparable<BaseData>, Serializable {
    private String date;
    private int occurrence;

    public String getDate() {
        return date;
    }

    public int getOccurrence() {
        return occurrence;
    }

    BaseData(String date, int occurrence) {
        this.date = date;
        this.occurrence = occurrence;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + occurrence;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseData baseData = (BaseData) o;

        return date.equals(baseData.date);

    }

    @Override
    public int compareTo(BaseData o) {
        if(this.equals(o)) {
            return 0;
         } else {
            try {
                return StatisticsGenerator.compareDate(date, o.date);
            } catch (Exception e) {
                // do nothing
            }
            return 0;
        }
    }
}
