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
public class NotificationBean {

	private static Logger log = LoggerFactory.getLogger(NotificationBean.class);

	private static final String JMS_USER = "jmsuser";
	private static final String JMS_USER_PASSWORD = "123123";

	@Resource(mappedName = "java:jboss/exported/jms/RemoteConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/NotificationQueue")
	private Queue queue;

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void notify(Notification notification) {
		log.info("sending message");
		Connection connection = null;
		Session session = null;
		try {
			// TODO does it have to be this way?
			connection = connectionFactory.createConnection(JMS_USER,
					JMS_USER_PASSWORD);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			// connection.start();
			// ObjectMessage doesn't work. Hail JSON!
			TextMessage message = session.createTextMessage();
			Gson gson = new Gson();
			message.setText(gson.toJson(notification,
					new TypeToken<Notification>() {
					}.getType()));
			producer.send(message);
			log.info("message has been sent");
		} catch (JMSException thr) {
			log.error("Error sending message.", thr);
		} finally {
			log.info("connection closed");
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					log.info("the connection wasn't closed. we're all gonna die");
				}
			}
		}
	}
}
