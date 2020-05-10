package org.ppk.accounts.dto;

import java.util.Date;

public class SyncLease {

    private String leaderId;
    private Long issueTimeStamp;
    private Date issueDate;
    private Long serialId;
    private String command;
    private Long leaseTime;

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public Long getIssueTimeStamp() {
        return issueTimeStamp;
    }

    public void setIssueTimeStamp(Long issueTimeStamp) {
        this.issueTimeStamp = issueTimeStamp;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(Long leaseTime) {
        this.leaseTime = leaseTime;
    }

    @Override
    public String toString() {
        return "SyncLease{" +
                "leaderId='" + leaderId + '\'' +
                ", issueTimeStamp=" + issueTimeStamp +
                ", issueDate=" + issueDate +
                ", serialId=" + serialId +
                ", command='" + command + '\'' +
                ", leaseTime=" + leaseTime +
                '}';
    }
}
