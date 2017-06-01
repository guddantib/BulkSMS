package com.vodacom.utilities.bulksms;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.vodacom.utilities.bulksms.jpa.SMS;
import com.vodacom.utilities.bulksms.jpa.SmsBatch;

@SessionScoped
public class BulkSMSBatchManager implements Serializable {

	private static final long serialVersionUID = -1284436546228118994L;

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private BulkSMSManager smsManager;
	
	private String batchName;
	
	private String message;
	
	private Date scheduleDate;

	public String getMessage() {
		return message;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
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

	@Transactional
	public SmsBatch createBatch (File uploadedFile, String batchName, String message, Date scheduleDate) {
		SmsBatch batch = new SmsBatch();
		batch.setBatchId(batch.getBatchId());
		batch.setBatchName(batchName);
		batch.setDateTime(new Timestamp(System.currentTimeMillis()));
		batch.setMessage(message);
		batch.setScheduleDate(scheduleDate);
		em.persist(batch);
		em.flush();
		return batch;
	}
	
	@SuppressWarnings("unchecked")
	public List<SmsBatch> retrieveAll() {
		List<SmsBatch> batches = null;
		Query query = em.createNamedQuery("SmsBatch.findAll", SmsBatch.class);
		batches = query.getResultList();
		for (SmsBatch batch : batches)
		{
			List<SMS> smsList = smsManager.retrieveByBatchId(batch.getBatchId());
			batch.setSmsList(smsList);
		}
		return batches;
	}
}