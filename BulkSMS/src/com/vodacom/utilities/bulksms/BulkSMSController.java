package com.vodacom.utilities.bulksms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import javax.transaction.Transactional;

import com.vodacom.utilities.bulksms.jpa.SmsBatch;

@ManagedBean(name="bulkSMSController")
@ViewScoped
@MultipartConfig(
        fileSizeThreshold   = 1024 * 1024 * 1,  // 1 MB
        maxFileSize         = 1024 * 1024 * 100, // 100 MB
        maxRequestSize      = 1024 * 1024 * 150, // 150 MB
        location            = "C:/uploaded/"
)
public class BulkSMSController {

	private Part filePart;

	private String batchName;

	private String message;

	private Date scheduleDate;

	@PersistenceContext
	public EntityManager em;

	public static final String FILE_UPLOAD_ROOT = "C:/uploaded/";

	@Inject
	public BulkSMSBatchManager batchManager;

	@Inject
	public BulkSMSManager smsManager;

	// @Inject
	// private SMSTransmitter smsTransmitter;

	@PostConstruct
	public void init() {
		
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
    
	public String getMessage() {
		return message;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Part getFile() {
		return filePart;
	}

	public void setFile(Part file) {
		this.filePart = file;
	}

	public void send() {
		File uploadedFile = new File(this.getFile().getSubmittedFileName());
		try (InputStream input = filePart.getInputStream()) {
			Files.copy(input, uploadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			validateFilePart(filePart);
			validateMSISDNs(uploadedFile);
			createSMS(this.getMessage(), uploadedFile);
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"The Bulk SMS has been sent successfully.", null);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		clear();
	}

	public void clear() {
		setBatchName(null);
		setMessage(null);
		setScheduleDate(null);
	}

	private void validateFilePart(Part part) {
		if (part == null || part.getSize() <= 0 || part.getContentType().isEmpty())
			throw new RuntimeException("Select a file that contains a list of MSISDNs");
		else if (part.getSize() > 2000000)
			throw new RuntimeException("File size too big. File size allowed  is less than or equal to 2 MB.");
	}
	
	
	/**
	 * Read the file from disk, iterate through its contents and check each
	 * MSISDN for validity, i.e. each MSISDN must be 11 characters, must start
	 * with 27 and the third character may not be a 0. Use a regular expression
	 * to do the check.
	 * 
	 * If any of the MSISDNs in the file are improperly formatted, throw a
	 * RuntimeException to convey to the user that the file uploaded contained
	 * improperly formatted numbers that need to be corrected. Once the file has
	 * been corrected, the user should upload the file again.
	 * 
	 * @param uploadedFile
	 */

	public static final String MSISDN_PATTERN = "27([0-9]){9}";

	private void validateMSISDNs(File uploadedFile) {

		/*
		 * Using a buffered reader, read the file line by line, trim each line,
		 * and then check the value of the line against the regular pattern. If
		 * the pattern doesn't match, break out of your loop and report the
		 * faulty number by throwing a RuntimeException. Otherwise, return
		 * without throwing a runtime exception.
		 */
		try {
			String line = null;
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(uploadedFile));
			while ((line = br.readLine()) != null) {
				if (!line.toString().trim().isEmpty()) {
					Pattern pattern = Pattern.compile(MSISDN_PATTERN);
					if (!pattern.matcher(line.toString()).matches()) {
						throw new RuntimeException(line.toString().trim()
								+ ", MSISDN must be 11 digits, may have numbers only, and must start with 27.");
					}
				} else {
					throw new RuntimeException("MSISDN is a required value.");
				}
			}
			br.close();
		} catch (IOException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * 
	 * @param message
	 * @param uploadedFile
	 */

	@Transactional
	private void createSMS(String message, File uploadedFile) throws Exception {
		try {
			SmsBatch batch = batchManager.createBatch(uploadedFile, batchName, message, scheduleDate);
			smsManager.saveSMSs(uploadedFile, batch, message, batchName);
			// smsTransmitter.sendSMSs(batch.getSMS(), message);
		} catch (IOException e) {
			FacesMessage messageF = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, messageF);
		}
	}
}