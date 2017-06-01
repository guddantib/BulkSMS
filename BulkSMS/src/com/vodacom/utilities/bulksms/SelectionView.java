package com.vodacom.utilities.bulksms;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.event.SelectEvent;

import com.vodacom.utilities.bulksms.jpa.SmsBatch;
 
@ManagedBean(name="dtSelectionView")
@ViewScoped
public class SelectionView implements Serializable {
    
	private static final long serialVersionUID = 1L;

	private List<SmsBatch> batchList;
     
    private SmsBatch selectedBatch;
     
    @PersistenceContext
    public EntityManager em;
     
    @Inject
    public BulkSMSBatchManager batchManager;
    
    @Inject
    public BulkSMSManager smsManager;
    
    @PostConstruct
    public void init() {
    	batchList = (batchManager.retrieveAll());
    }
 
    public List<SmsBatch> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<SmsBatch> batchList) {
		this.batchList = batchList;
	}

	public SmsBatch getSelectedBatch() {
        return selectedBatch;
    }

    public BulkSMSBatchManager getBatchManager() {
		return batchManager;
	}

	public void setBatchManager(BulkSMSBatchManager batchManager) {
		this.batchManager = batchManager;
	}
	
    public BulkSMSManager getSmsManager() {
		return smsManager;
	}

	public void setSmsManager(BulkSMSManager smsManager) {
		this.smsManager = smsManager;
	}

	public void setSelectedBatch(SmsBatch selectedBatch) {
        this.selectedBatch = selectedBatch;
    }

    public void onRowSelect(SelectEvent event) {
        try {
        	((SmsBatch) event.getObject()).getBatchId();
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}