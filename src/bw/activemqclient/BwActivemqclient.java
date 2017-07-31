/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bw.activemqclient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class BwActivemqclient {
    
    public static void main(String[] args){
/*
Producer(String url,boolean transacted, int acknowledgeMode, String queue, int deliveryMode, String messagesText)
    String url: example:"tcp://localhost:61616".
    boolean transacted: indicates whether the session is transacted.
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
        
Full example: boolean[] out= bw.activemqclient.BwActivemqclient.Producer("tcp://localhost:61616",false,1,"TEST.FOO",2,new String[]{"TEST 2","TEST 3","TEST 4"});
        This returns an Array of booleans that are true if the msg was sent.
Full example: boolean out= bw.activemqclient.BwActivemqclient.Producer("tcp://localhost:61616",false,1,"TEST.FOO",2,"TEST 2");
        This returns a boolean that is true if the msg was sent.
        
        
        
Consumer(String url,boolean transacted, int acknowledgeMode, String queue, int receiveTimeout)
    String url: example:"tcp://localhost:61616".
    boolean transacted: indicates whether the session is transacted.
    int acknowledgeMode: indicates whether the consumer or the client will acknowledge any messages it receives; ignored if the session is transacted.
        AUTO_ACKNOWLEDGE = 1;
        CLIENT_ACKNOWLEDGE = 2;
        DUPS_OK_ACKNOWLEDGE = 3;
        SESSION_TRANSACTED = 0;
    String queue: example "TEST.FOO"
    int receiveTimeout: Receives the next message that arrives within the specified timeout interval.
    int receiveNum: number of max msgs to recive.

        
Full example: String xml= bw.activemqclient.BwActivemqclient.Consumer("tcp://localhost:61616",false,1,"TEST.FOO",1000,50);
        This returns an XML String.
        */

    }
    
    public static boolean[] Producer(String url,boolean transacted, int acknowledgeMode, String queue, int deliveryMode, String[] messagesText){
        boolean[] out=new boolean[messagesText.length];
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
                for(int c=0;c<messagesText.length;c++){
                producer.send(session.createTextMessage(messagesText[c]));
                out[c]=true;
                }
                session.close();
                connection.close();
                return out;
            }
            catch (JMSException ex) {
                Logger.getLogger(BwActivemqclient.class.getName()).log(Level.SEVERE, null, ex);
                return out;
            }
    
    }
    
    public static boolean Producer(String url,boolean transacted, int acknowledgeMode, String queue, int deliveryMode, String messagesText){
        boolean out=false;
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
                out=true;
                session.close();
                connection.close();
                return out;
            }
            catch (JMSException ex) {
                Logger.getLogger(BwActivemqclient.class.getName()).log(Level.SEVERE, null, ex);
                return out;
            }
    
    }
    
    
    public static String Consumer(String url, boolean transacted, int acknowledgeMode, String queue, int receiveTimeout, int receiveNum){
        Message[] messages= new Message[receiveNum];
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
 
                // Wait for a message
                boolean keeplooping=true;
                for(int c=0;c<receiveNum&&keeplooping;c++){
                    Message message = consumer.receive(receiveTimeout);
                    
                    if (message == null){
                        keeplooping=false;
                        }else{
                            messages[c]= message;
                        }
                }
                
                consumer.close();
                session.close();
                connection.close();
           } catch (JMSException ex) {
            Logger.getLogger(BwActivemqclient.class.getName()).log(Level.SEVERE, null, ex);
          }
        return MsgToXml(messages);
    }
    
    
    public static String MsgToXml(Message[] messages){
    StringBuilder sBuilder = new StringBuilder();
    sBuilder.append("<messages>");
    for(int c=0;c<messages.length&&messages[c]!=null;c++){
      sBuilder.append("\n<message>");
      sBuilder.append("\n<id>");
        try {
            sBuilder.append(messages[c].getJMSMessageID());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</id>");
      sBuilder.append("\n<content>");
        try {
            sBuilder.append(URLEncoder.encode(((TextMessage) messages[c]).getText(),"UTF-8"));
        } catch (JMSException ex) {
            sBuilder.append("null");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BwActivemqclient.class.getName()).log(Level.SEVERE, null, ex);
        }
      sBuilder.append("</content>");  
      sBuilder.append("\n<destination>");
        try {
            sBuilder.append(messages[c].getJMSDestination().toString());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</destination>");
      sBuilder.append("\n<correlationid>");
        try {
            sBuilder.append(messages[c].getJMSCorrelationID());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</correlationid>");
      sBuilder.append("\n<priority>");
        try {
            sBuilder.append(messages[c].getJMSPriority());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</priority>");  
      sBuilder.append("\n<redelivered>");
        try {
            sBuilder.append(messages[c].getJMSRedelivered());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</redelivered>");  
      sBuilder.append("\n<replyto>");
        try {
            sBuilder.append(messages[c].getJMSReplyTo());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</replyto>");
      sBuilder.append("\n<timestamp>");
        try {
            sBuilder.append(messages[c].getJMSTimestamp());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</timestamp>");
      sBuilder.append("\n<type>");
        try {
            sBuilder.append(messages[c].getJMSType());
        } catch (JMSException ex) {
            sBuilder.append("null");
        }
      sBuilder.append("</type>");  
      sBuilder.append("\n</message>");
    }
      sBuilder.append("\n</messages>");
      return sBuilder.toString();
    }
    
    
}
