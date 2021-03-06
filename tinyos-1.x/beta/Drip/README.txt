//$Id: README.txt,v 1.7 2005/06/14 18:19:35 gtolle Exp $

Drip is a transport-layer component for epidemically disseminating
messages throughout a network.

Why Drip is better than Bcast:

- Reliable injection: Injecting a message is a dialog between the host
  and the network. The host knows whether the message was injected or
  not, detecting and preventing radio failures at the first hop.

- Epidemic reliable delivery. After sending a message, Drip will
  continually retransmit to ensure that it eventually reaches every
  node in the network.

- Neighborhood suppression: If a node transmits, its neighbors will
  delay their own transmissions. This means that fewer messages will
  be sent, as compared to Bcast.

- Trickle timers: Each time a node retransmits, it waits double the
  amount of time until it retransmits again. This ensures that traffic
  eventually drops down to a low base rate, while still maintaining
  epidemic reliability. A new message kicks the transmit rate back up,
  for fast dissemination.

- Transport layer: With Drip, the identifiers you use for Drip
  messages are nested within the Drip header. This means you can only
  collide identifiers with other Drip messages. With Bcast, you have
  to place your message AM id within the top-level header by playing
  wiring tricks.

One important note:

- Drip does not use the serial connection. To inject messages into a
  Drip network, you must send them through a mote running TOSBase.

Programming With Drip:

Drip provides two interfaces: Receive and Drip. To maintain its
per-channel metadata, it uses a third interface: DripState. 

Wire to them like this:

  TestDripM.ReceiveDrip -> DripC.Receive[YOUR_MESSAGE_ID_HERE];
  TestDripM.Drip -> DripC.Drip[YOUR_MESSAGE_ID_HERE];
  DripC.DripState[YOUR_MESSAGE_ID_HERE] ->
    DripStateC.DripState[unique("DripState")];

In your StdControl.init(), you must call Drip.init(). This initializes
the sequence number for the channel. If you wire Drip multiple times,
you must call Drip.init() for each time.

Receive.receive(...) will be signaled when Drip receives a new
message. Your component is then responsible for caching the data
within that message, so that the message can be regenerated when Drip
needs to retransmit it. This is when you should act on the contents of
the message, as well.

 event TOS_MsgPtr Receive.receive(TOS_MsgPtr msg, void* payload,
                                  uint16_t payloadLen) {
   memcpy(&clientCache, payload, sizeof(clientCache));
   // client acts on the message here
 }


To make the dissemination propagate epidemically, Drip will
occasionally signal Drip.rebroadcastRequest(TOS_MsgPtr msg, void*
payload) when it needs to retransmit. Your component should then copy
the cached data into the payload, and call Drip.rebroadcast(TOS_MsgPtr
msg, void *payload, uint8_t len), with the length of the payload.

 event result_t Drip.rebroadcastRequest(TOS_MsgPtr msg, void* payload) {
   memcpy(payload, &clientCache, sizeof(clientCache));
   call Drip.rebroadcast(msg, payload, sizeof(clientCache));
 }

To inject new messages from the PC side, you can use the Drip.java
component.

To inject a new message from the mote, you call Drip.changed(). Then,
the next time you get a Drip.rebroadcastRequest() event, you give it
the new message instead of the message you had previously cached.

Testing Drip:

There is a test application in the TestDrip directory. This
application listens for one Drip message on channel 254, containing a
2-byte payload.

Injecting Drip Messages:

There is a java tool for injecting Drip messages in
tools/Drip.java. It must be used through a TOSBase node plugged into a
serial connection.

From the command line, it can be used with TestDrip:

  java Drip <payload>

This injects the appropriate message for the TestDrip
application, with your argument as the payload.

From another Java app, it can be used to inject arbitrary messages. 

  Drip drip = new Drip(YOUR_MESSAGE_ID_HERE);
  YourMsg msg = new YourMsg();
  msg.set_data(someData);
  drip.send(msg, YourMsg.DEFAULT_MESSAGE_SIZE);

Known Shortcomings:

The send() command must be given the size of the message.

Email get@cs.berkeley.edu with any questions.
