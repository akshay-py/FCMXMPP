package com.wedevol.xmpp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.XMPPException;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.util.Util;

public class LocatorWorker implements Callable<String> {
	String toRegId;
	String fcmProjectSenderId;
	String fcmServerKey;
	CountDownLatch latch = new CountDownLatch(1);
	
	public LocatorWorker(String fcmProjectSenderId, String fcmServerKey, String toRegId2) {
		this.toRegId = toRegId2;
		this.fcmProjectSenderId = fcmProjectSenderId;
		this.fcmServerKey = fcmServerKey;
	}

	public static final Logger logger = Logger.getLogger(EntryPoint.class.getName());
	
	@Override
	public String call() {
		CcsClient ccsClient = CcsClient.prepareClient(fcmProjectSenderId, fcmServerKey, false, latch);

		try {
			ccsClient.connect();
		} catch (XMPPException e) {
			logger.log(Level.SEVERE, "Error trying to connect.", e);
		}

		// Send a sample downstream message to a device
		String messageId = Util.getUniqueMessageId();
		Map<String, String> dataPayload = new HashMap<String, String>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, "GETLOC");
		CcsOutMessage message = new CcsOutMessage(toRegId, messageId, dataPayload);
		String jsonRequest = MessageHelper.createJsonOutMessage(message);
		ccsClient.send(jsonRequest);
		String locationMessage = null;
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (ccsClient.getMessage() != null) {
			locationMessage = ccsClient.getMessage();
		}
		return locationMessage;
	}

}
