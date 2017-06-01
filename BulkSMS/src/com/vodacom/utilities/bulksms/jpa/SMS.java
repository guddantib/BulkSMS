package com.vodacom.utilities.bulksms.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the SMS database table.
 * 
 */
@Entity
@Table(name="SMS")
@NamedQuery(name="SMS.findAllByBatchId", query="SELECT s FROM SMS s WHERE s.batchId = :batchId")
public class SMS implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SMS_ID_GENERATOR", sequenceName="SMS_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SMS_ID_GENERATOR")
	private long id;

	@Column(name="DATE_TIME")
	private Timestamp dateTime;
	
	@Column(name="BATCH_NAME")
	private String batchName;
	
	@Column(name="MSISDN")
	private String msisdn;
	
	@Column(name="\"MESSAGE\"")
	private String message;

	@Column(name="BATCH_ID", nullable=false)
	private long batchId;
	
	@Transient
	public SmsBatch smsBatch;

	public SMS() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public long getBatchId() {
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	public SmsBatch getSmsBatch() {
		return this.smsBatch;
	}

	public void setSmsBatch(SmsBatch smsBatch) {
		this.smsBatch = smsBatch;
	}
}