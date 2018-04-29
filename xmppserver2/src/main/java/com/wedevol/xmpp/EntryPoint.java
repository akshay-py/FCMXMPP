package com.wedevol.xmpp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.util.MessageMapper;
import com.wedevol.xmpp.util.Util;

/**
 * Entry Point class for the XMPP Server
 * 
 * @author Charz++
 */
public class EntryPoint extends CcsClient {

  protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

  public EntryPoint(String projectId, String apiKey, boolean debuggable, String toRegId) {
    super(projectId, apiKey, debuggable);

    try {
      connect();
    } catch (XMPPException | InterruptedException | KeyManagementException | NoSuchAlgorithmException | SmackException
        | IOException e) {
      logger.error("Error trying to connect. Error: {}", e.getMessage());
    }

    // Send a sample downstream message to a device
    final String messageId = Util.getUniqueMessageId();
    final Map<String, String> dataPayload = new HashMap<String, String>();
    dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, "GETLOC");
    final CcsOutMessage message = new CcsOutMessage(toRegId, messageId, dataPayload);
    final String jsonRequest = MessageMapper.toJsonString(message);
    sendDownstreamMessage(messageId, jsonRequest);

    try {
      final CountDownLatch latch = new CountDownLatch(1);
      latch.await();
    } catch (InterruptedException e) {
      logger.error("An error occurred while latch was waiting. Error: {}", e.getMessage());
    }
  }

  public static void main(String[] args) throws SmackException, IOException {
    final String fcmProjectSenderId = args[0];
    final String fcmServerKey = args[1];
    final String toRegId = args[2];
    new EntryPoint(fcmProjectSenderId, fcmServerKey, false, toRegId);
  }
}
