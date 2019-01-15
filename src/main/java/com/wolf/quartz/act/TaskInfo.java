package com.wolf.quartz.act;

import java.util.List;
import java.util.Map;

/**
 * Description:配置信息，被转化成job
 * <br/> Created on 04/08/2018 4:34 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TaskInfo {

    protected String categoryId; // 业务Id
    protected String categoryName; // 业务名称
    protected String sourceId; // 信源Id
    protected String sourceName; // 信源名称
    protected int sourceStatus; // 信源状态
    protected String pipelineConf; // 信源pipeline配置信息
    protected List<String> dbStoreTypes; // 业务的存储类型
    protected String esConfInfo; // ES存储配置
    protected String dbConfInfo; // DB存储配置
    protected String cronInfo; // 定时任务信息
    protected int sourceType; // 实时更新还是离线更新
    protected List<String> indexBuildEles; // 更新索引的信息
    protected List<String> idBuildEles; // id的构建因素
    protected String indexType; // 全量或增量
    protected String categoryLevel1; // 一级分类
    protected String zhName; // 中文信息
    protected Map<String,String> outputType; //输出参数名及其类型
    protected String providerName;
    protected String functionName; //category_function名称

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(int sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getPipelineConf() {
        return pipelineConf;
    }

    public void setPipelineConf(String pipelineConf) {
        this.pipelineConf = pipelineConf;
    }

    public String getEsConfInfo() {
        return esConfInfo;
    }

    public void setEsConfInfo(String esConfInfo) {
        this.esConfInfo = esConfInfo;
    }

    public String getDbConfInfo() {
        return dbConfInfo;
    }

    public void setDbConfInfo(String dbConfInfo) {
        this.dbConfInfo = dbConfInfo;
    }

    public String getCronInfo() {
        return cronInfo;
    }

    public void setCronInfo(String cronInfo) {
        this.cronInfo = cronInfo;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public List<String> getIdBuildEles() {
        return idBuildEles;
    }

    public void setIdBuildEles(List<String> idBuildEles) {
        this.idBuildEles = idBuildEles;
    }

    public List<String> getIndexBuildEles() {
        return indexBuildEles;
    }

    public void setIndexBuildEles(List<String> indexBuildEles) {
        this.indexBuildEles = indexBuildEles;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getCategoryLevel1() {
        return categoryLevel1;
    }

    public void setCategoryLevel1(String categoryLevel1) {
        this.categoryLevel1 = categoryLevel1;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public TaskInfo(){}

    public List<String> getDbStoreTypes() {
        return dbStoreTypes;
    }

    public void setDbStoreTypes(List<String> dbStoreTypes) {
        this.dbStoreTypes = dbStoreTypes;
    }

    public Map<String, String> getOutputType() {
        return outputType;
    }

    public void setOutputType(Map<String, String> outputType) {
        this.outputType = outputType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    /**
     * 是否有相同的定时信息
     * @param taskInfo
     * @return
     */
    public boolean hasSameCronInfo(TaskInfo taskInfo){
        if(taskInfo == null) return false;
        return this.getCronInfo().equalsIgnoreCase(taskInfo.getCronInfo());
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", sourceStatus=" + sourceStatus +
                ", pipelineConf='" + pipelineConf + '\'' +
                ", dbStoreTypes=" + dbStoreTypes +
                ", esConfInfo='" + esConfInfo + '\'' +
                ", dbConfInfo='" + dbConfInfo + '\'' +
                ", cronInfo='" + cronInfo + '\'' +
                ", sourceType=" + sourceType +
                ", indexBuildEles=" + indexBuildEles +
                ", idBuildEles=" + idBuildEles +
                ", indexType='" + indexType + '\'' +
                ", categoryLevel1='" + categoryLevel1 + '\'' +
                ", zhName='" + zhName + '\'' +
                ", outputType='" + outputType + '\'' +
                ", providerName='" + providerName + '\'' +
                ", functionName='" + functionName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskInfo taskInfo = (TaskInfo) o;

        if (sourceStatus != taskInfo.sourceStatus) return false;
        if (sourceType != taskInfo.sourceType) return false;
        if (categoryName != null ? !categoryName.equals(taskInfo.categoryName) : taskInfo.categoryName != null)
            return false;
        if (sourceName != null ? !sourceName.equals(taskInfo.sourceName) : taskInfo.sourceName != null) return false;
        if (providerName != null ? !providerName.equals(taskInfo.providerName) : taskInfo.providerName != null) return false;
        if (pipelineConf != null ? !pipelineConf.equals(taskInfo.pipelineConf) : taskInfo.pipelineConf != null)
            return false;
        if (dbStoreTypes != null ? !dbStoreTypes.equals(taskInfo.dbStoreTypes) : taskInfo.dbStoreTypes != null)
            return false;
        if (esConfInfo != null ? !esConfInfo.equals(taskInfo.esConfInfo) : taskInfo.esConfInfo != null) return false;
        if (dbConfInfo != null ? !dbConfInfo.equals(taskInfo.dbConfInfo) : taskInfo.dbConfInfo != null) return false;
        if (cronInfo != null ? !cronInfo.equals(taskInfo.cronInfo) : taskInfo.cronInfo != null) return false;
        if (indexBuildEles != null ? !indexBuildEles.equals(taskInfo.indexBuildEles) : taskInfo.indexBuildEles != null)
            return false;
        if (idBuildEles != null ? !idBuildEles.equals(taskInfo.idBuildEles) : taskInfo.idBuildEles != null)
            return false;
        if (indexType != null ? !indexType.equals(taskInfo.indexType) : taskInfo.indexType != null) return false;
        if (categoryLevel1 != null ? !categoryLevel1.equals(taskInfo.categoryLevel1) : taskInfo.categoryLevel1 != null)
            return false;
        if (outputType != null ? !outputType.equals(taskInfo.outputType) : taskInfo.outputType != null)
            return false;
        if (functionName != null ? !functionName.equals(taskInfo.functionName) : taskInfo.functionName != null)
            return false;
        return zhName != null ? zhName.equals(taskInfo.zhName) : taskInfo.zhName == null;

    }

    @Override
    public int hashCode() {
        int result = categoryName != null ? categoryName.hashCode() : 0;
        result = 31 * result + (sourceName != null ? sourceName.hashCode() : 0);
        result = 31 * result + (providerName != null ? providerName.hashCode() : 0);
        result = 31 * result + sourceStatus;
        result = 31 * result + (pipelineConf != null ? pipelineConf.hashCode() : 0);
        result = 31 * result + (dbStoreTypes != null ? dbStoreTypes.hashCode() : 0);
        result = 31 * result + (esConfInfo != null ? esConfInfo.hashCode() : 0);
        result = 31 * result + (dbConfInfo != null ? dbConfInfo.hashCode() : 0);
        result = 31 * result + (cronInfo != null ? cronInfo.hashCode() : 0);
        result = 31 * result + sourceType;
        result = 31 * result + (indexBuildEles != null ? indexBuildEles.hashCode() : 0);
        result = 31 * result + (idBuildEles != null ? idBuildEles.hashCode() : 0);
        result = 31 * result + (indexType != null ? indexType.hashCode() : 0);
        result = 31 * result + (categoryLevel1 != null ? categoryLevel1.hashCode() : 0);
        result = 31 * result + (zhName != null ? zhName.hashCode() : 0);
        result = 31 * result + (outputType != null ? outputType.hashCode() : 0);
        result = 31 * result + (functionName != null ? functionName.hashCode() : 0);
        return result;
    }
}
