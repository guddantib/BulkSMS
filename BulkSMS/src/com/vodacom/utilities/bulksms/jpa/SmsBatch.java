package com.vodacom.utilities.bulksms.jpa;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the SMS_BATCH database table.
 * 
 */
@Entity
@Table(name="SMS_BATCH")
@NamedQuery(name="SmsBatch.findAll", query="SELECT s FROM SmsBatch s order by s.batchId asc")
public class SmsBatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SMS_BATCH_BATCHID_GENERATOR", sequenceName="SMS_BATCH_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SMS_BATCH_BATCHID_GENERATOR")
	@Column(name="BATCH_ID")
	private long batchId;

	@Column(name="BATCH_NAME")
	private String batchName;

	@Column(name="DATE_TIME")
	private Timestamp dateTime;

	@Column(name="\"MESSAGE\"")
	private String message;
	
	@Column (name="SCHEDULE_DATE")
	private Date scheduleDate;
	
	@Transient
	public List<SMS> smsList;

	public SmsBatch() {
	}

	public long getBatchId() {
		return this.batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Timestamp getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public SMS addSms(SMS sms) {
		getSmsList().add(sms);
		sms.setSmsBatch(this);
		return sms;
	}

	public SMS removeSms(SMS sms) {
		getSmsList().remove(sms);
		sms.setSmsBatch(null);
		return sms;
	}

	public List<SMS> getSmsList() {
		return this.smsList;
	}

	public void setSmsList(List<SMS> smsList) {
		this.smsList = smsList;
	}

	public int getSize()
	{
		if (this.smsList != null)
			return this.smsList.size();
		
		return 0;
	}
}