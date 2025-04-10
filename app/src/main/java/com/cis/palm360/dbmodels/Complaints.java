package com.cis.palm360.dbmodels;

/**
 * Created by Calibrage11 on 8/8/2017.
 */

public class Complaints {

    private String Code;
    private String PlotCode;
    private Integer CriticalityByTypeId;
    private int IsActive;
    private int CreatedByUserId;
    private String CreatedDate;
    private int UpdatedByUserId;
    private String UpdatedDate;
    private int ServerUpdatedStatus;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getPlotCode() {
        return PlotCode;
    }

    public void setPlotCode(String plotCode) {
        PlotCode = plotCode;
    }

    public int getCriticalityByTypeId() {
        return CriticalityByTypeId;
    }

    public void setCriticalityByTypeId(Integer criticalityByTypeId) {
        CriticalityByTypeId = criticalityByTypeId;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public int getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(int updatedByUserId) {
        UpdatedByUserId = updatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public int getServerUpdatedStatus() {
        return ServerUpdatedStatus;
    }

    public void setServerUpdatedStatus(int serverUpdatedStatus) {
        ServerUpdatedStatus = serverUpdatedStatus;
    }
}


