# bw-activemqclient

ActiveMQ client for TIBCO ActiveMatrix BusinessWorks 5.x

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