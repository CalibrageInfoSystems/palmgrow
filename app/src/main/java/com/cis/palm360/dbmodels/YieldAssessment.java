package com.cis.palm360.dbmodels;

public class YieldAssessment {
//    private Integer Id;
    private String CropMaintenaceCode;
    private String Question;
    private String Answer;
    private String Value;
    private Integer Year;
    private Integer IsActive;
    private Integer CreatedByUserId;
    private Integer UpdatedByUserId;
    private String CreatedDate;
    private String UpdatedDate;
    private int ServerUpdatedStatus;

    public int getServerUpdatedStatus() {
        return ServerUpdatedStatus;
    }

    public void setServerUpdatedStatus(int serverUpdatedStatus) {
        ServerUpdatedStatus = serverUpdatedStatus;
    }

//    public Integer getId() {
//        return Id;
//    }
//
//    public void setId(Integer id) {
//        Id = id;
//    }

    public String getCropMaintenaceCode() {
        return CropMaintenaceCode;
    }

    public void setCropMaintenaceCode(String cropMaintenaceCode) {
        CropMaintenaceCode = cropMaintenaceCode;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public Integer getIsActive() {
        return IsActive;
    }

    public void setIsActive(Integer isActive) {
        IsActive = isActive;
    }

    public Integer getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public Integer getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        UpdatedByUserId = updatedByUserId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }
}
