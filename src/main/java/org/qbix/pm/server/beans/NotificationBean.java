package org.qbix.pm.server.beans;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.qbix.pm.server.dto.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class NotificationBean {

	private static Logger log = LoggerFactory.getLogger(NotificationBean.class);

	private static final String JMS_USER = "jmsuser";
	
	private static final String JMS_USER_PASSWORD = "123123";

	@Resource(mappedName = "java:jboss/exported/jms/RemoteConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:jboss/exported/jms/queue/NotificationQueue")
	private Queue queue;

	/**
	 *  Sends a JMS message with the provided notification as its content
	 */
	public void notifyWithJMS(Notification notification) {
		Connection jmsConnection = null;
		Session jmsSession = null;
		try {
			jmsConnection = connectionFactory.createConnection(JMS_USER,
					JMS_USER_PASSWORD);
			
			jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer messageQueue = jmsSession.createProducer(queue);
			TextMessage message = jmsSession.createTextMessage();
			
			Gson gson = new Gson();
			message.setText(gson.toJson(notification,
					new TypeToken<Notification>() {
					}.getType()));
			
			messageQueue.send(message);
		} catch (JMSException thr) {
			log.error("Error sending message.", thr);
		} finally {
			if (jmsConnection != null) {
				try {
					jmsConnection.close();
				} catch (Exception e) {
					log.info("Unable to close jms connection", e);
				}
			}
		}
	}
	
}
