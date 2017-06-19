package org.k2.processmining.controller;

import org.k2.processmining.support.normal.transform.FormatInfo;
import org.k2.processmining.support.normal.transform.LogConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Created by nyq on 2017/6/17.
 */
public class NormalizeRawLogConfigForm {
    private String rawLogId;
    private List<Format> formats;
    private String timeNames;
    private Map<String, String[]> renameOrMergeItems;
    private String oriItemSeparator;
    private String oriNameValSeparator;
    private String oriNulVal;

    public String getRawLogId() {
        return rawLogId;
    }

    public void setRawLogId(String rawLogId) {
        this.rawLogId = rawLogId;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public void setFormats(List<Format> formats) {
        this.formats = formats;
    }

    public String getTimeNames() {
        return timeNames;
    }

    public void setTimeNames(String timeNames) {
        this.timeNames = timeNames;
    }

    public Map<String, String[]> getRenameOrMergeItems() {
        return renameOrMergeItems;
    }

    public void setRenameOrMergeItems(Map<String, String[]> renameOrMergeItems) {
        this.renameOrMergeItems = renameOrMergeItems;
    }

    public String getOriItemSeparator() {
        return oriItemSeparator;
    }

    public void setOriItemSeparator(String oriItemSeparator) {
        this.oriItemSeparator = oriItemSeparator;
    }

    public String getOriNameValSeparator() {
        return oriNameValSeparator;
    }

    public void setOriNameValSeparator(String oriNameValSeparator) {
        this.oriNameValSeparator = oriNameValSeparator;
    }

    public String getOriNulVal() {
        return oriNulVal;
    }

    public void setOriNulVal(String oriNulVal) {
        this.oriNulVal = oriNulVal;
    }

    public LogConfiguration toLogConfiguration() {
        FormatInfo[] formatInfos = new FormatInfo[formats.size()];
        int i = 0;
        for (Format format : formats) {
            formatInfos[i] = new FormatInfo(
                    format.getDataItem(),
                    format.getPlaceholder().toCharArray(),
                    format.getIdentifyItem(),
                    format.getOriFormat(),
                    format.getGoalFormat());
        }
        return new LogConfiguration(
                timeNames.split(";"),
                renameOrMergeItems,
                "[Method]",
                formatInfos,
                oriNameValSeparator,
                oriItemSeparator,
                oriNulVal);
    }
}

class Format {
    private String dataItem;
    private String placeholder;
    private String identifyItem;
    private String goalFormat;
    private Map<String, String> oriFormat;

    public String getDataItem() {
        return dataItem;
    }

    public void setDataItem(String dataItem) {
        this.dataItem = dataItem;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getIdentifyItem() {
        return identifyItem;
    }

    public void setIdentifyItem(String identifyItem) {
        this.identifyItem = identifyItem;
    }

    public String getGoalFormat() {
        return goalFormat;
    }

    public void setGoalFormat(String goalFormat) {
        this.goalFormat = goalFormat;
    }

    public Map<String, String> getOriFormat() {
        return oriFormat;
    }

    public void setOriFormat(Map<String, String> oriFormat) {
        this.oriFormat = oriFormat;
    }
}
