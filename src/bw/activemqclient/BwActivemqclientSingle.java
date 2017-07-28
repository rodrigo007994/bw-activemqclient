/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bw.activemqclient;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.activemq.ActiveMQConnectionFactory;
 
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author rodrigo
 */
public class BwActivemqclientSingle {
    
    public static void main(String[] args){
/*
Producer(String url,Boolean transacted, int acknowledgeMode, String queue, int deliveryMode, String messagesText)
    String url: example:"tcp://localhost:61616".
    Boolean transacted: indicates whether the session is transacted.
    int acknowledgeMode: indicates whether the consumer or the client will acknowledge any messages it receives; ignored if the session is transacted.
        AUTO_ACKNOWLEDGE = 1;
        CLIENT_ACKNOWLEDGE = 2;
        DUPS_OK_ACKNOWLEDGE = 3;
        SESSION_TRANSACTED = 0;
    String queue: example "TEST.FOO"
    int deliveryMode:
        NON_PERSISTENT = 1;
        PERSISTENT = 2;
    String messagesText
        
Full example: Producer("tcp://localhost:61616",false,1,"TEST.FOO",2,"TEST 1");
        
        */

/*
Consumer(String url,Boolean transacted, int acknowledgeMode, String queue, int receiveTimeout)
    String url: example:"tcp://localhost:61616".
    Boolean transacted: indicates whether the session is transacted.
    int acknowledgeMode: indicates whether the consumer or the client will acknowledge any messages it receives; ignored if the session is transacted.
        AUTO_ACKNOWLEDGE = 1;
        CLIENT_ACKNOWLEDGE = 2;
        DUPS_OK_ACKNOWLEDGE = 3;
        SESSION_TRANSACTED = 0;
    String queue: example "TEST.FOO"
    int receiveTimeout: Receives the next message that arrives within the specified timeout interval.

        
Full example: Consumer("tcp://localhost:61616",false,1,"TEST.FOO",1000);
        
        */

       


       Producer("tcp://localhost:61616",false,1,"TEST.FOO",2,"TEST 1");
        
        
        Consumer("tcp://localhost:61616",false,1,"TEST.FOO",1000);
    }
    
    public static Boolean Producer(String url,Boolean transacted, int acknowledgeMode, String queue, int deliveryMode, String messagesText){
        try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a Session
                Session session = connection.createSession(transacted,acknowledgeMode);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue(queue);
 
                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(deliveryMode);
 
                // Create a messages
                
                producer.send(session.createTextMessage(messagesText));
                session.close();
                connection.close();
                return true;
            }
            catch (JMSException e) {
                System.out.println("Caught: " + e.getMessage());
                return false;
            }
    
    }
    
    public static String Consumer(String url,Boolean transacted, int acknowledgeMode, String queue, int receiveTimeout){
        String out=null;
        try {
 
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
                // Create a Session
                Session session = connection.createSession(transacted, acknowledgeMode);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue(queue);
 
                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);
                Message message=null;
 
                // Wait for a message
                    message = consumer.receive(receiveTimeout);
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            String text = textMessage.getText();
                            out= text;
                        } catch (JMSException ex) {
                            Logger.getLogger(BwActivemqclientSingle.class.getName()).log(Level.WARNING, " Activemq client No Msg to consume. ", ex);
                        }
                    } else {
                        if (message == null){
                            out=null;
                        }else{
                            out= message.toString();
                        }
                    }
                consumer.close();
                session.close();
                connection.close();
                return out;
            } catch (JMSException ex) {
                Logger.getLogger(BwActivemqclientSingle.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
    
    
}
