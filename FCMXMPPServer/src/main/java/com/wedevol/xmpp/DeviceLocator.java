package com.wedevol.xmpp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DeviceLocator {

	public static void main(String[] args) {
		final String fcmProjectSenderId = "362421666820";
		final String fcmServerKey = "AAAAVGIDxAQ:APA91bEngyVXwe4im4g8vX-GOvW0Uw-sXgGMnyGIKXiJNw-DYWJJJvl0KX2LQNMd4WksjfSfBcbImQ6lgjmxWo2QnOkG4fso5wwrDB4xkYh3swhvUciV68ei6sBX1E32T8Xt5Z2AXqCD";
		final String toRegId = "eujgXWXowgA:APA91bEPgPyhjy9Qk08SBFMdHLraUtJv7HZ7Ph9n084IHE8Xv8ZCzrlZltlbpYjzwLlUcgcFir6YXH954BmgpA65rnHn4YgYloWJb_yCGZhccGX1W2sWoNDSzUeSsfZS6yek4kO0aLIE";
		
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<String> futureCall = executor.submit(new LocatorWorker(fcmProjectSenderId, fcmServerKey, toRegId));
		
		String result = null;
		try {
			result = futureCall.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Here the thread will be blocked 
	    System.out.println(result);
	}

}
