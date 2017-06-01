package com.vodacom.utilities.bulksms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.vodacom.utilities.bulksms.jpa.SMS;
import com.vodacom.utilities.bulksms.jpa.SmsBatch;

@SessionScoped
public class BulkSMSManager implements Serializable {
	
	private static final long serialVersionUID = 5554466317695112708L;

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private BulkSMSBatchManager batchManager;
	
	private String batchName;

	private String message;
	
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
	
	@Transactional
	public void saveSMSs (File uploadedFile, SmsBatch batch, String message, String batchName) throws IOException {
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(uploadedFile));
		try {
			List<SMS> smsList = new ArrayList<SMS>();
			batch.setSmsList(smsList);
			while ((line = br.readLine()) != null) {
				SMS sms = new SMS();
				sms.setBatchId(batch.getBatchId());
				sms.setBatchName(batchName);
				sms.setMsisdn(line.trim());
				sms.setDateTime(new Timestamp(System.currentTimeMillis()));
				sms.setMessage(message);
				batch.addSms(sms);
				em.persist(sms);
				em.flush();
			}
		}
		catch(Exception e) {
			FacesMessage messageS = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, messageS);
		}
		finally {
			br.close();
		}
	}
	
	public BulkSMSBatchManager getBatchManager() {
		return batchManager;
	}

	public void setBatchManager(BulkSMSBatchManager batchManager) {
		this.batchManager = batchManager;
	}

	@SuppressWarnings("unchecked")
	public List<SMS> retrieveByBatchId(long batchId)
	{
		//Find and return list of all SMSs matching the given batchId
		List<SMS> smsList = null;
		Query query = em.createNamedQuery("SMS.findAllByBatchId", SMS.class);
		query.setParameter("batchId", batchId);
		smsList = query.getResultList();
		
		return smsList;
	}
}