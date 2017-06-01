package com.vodacom.utilities.bulksms;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;

import com.vodacom.utilities.bulksms.jpa.SMS;

@Dependent
public class SMSTransmitter implements Serializable {

	private static final long serialVersionUID = 7506960237300039685L;

	public SMSTransmitter() {}
	
	/**
	 * Attempt to send SMS via the Fusion REST end-point. Throw an exception if the
	 * transmission fails, or if the HTTP response code from the fusion server is not 200.
	 * 
	 * @param msisdn
	 * @param message
	 * @throws Exception
	 */
	public void sendSMS(SMS sms, String message) throws Exception
	{
		//Send the SMS. Update the SMS record with the result
		//..
		
	}
	
	public void sendSMSs(List<SMS> smsList, String message)
	{
		//...
	}
}

