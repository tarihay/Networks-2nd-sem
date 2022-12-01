package ru.nsu.ccfit.gorin.snakes.net.messages;

public class PingMessage extends Message {
    public PingMessage() {
        super(MessageType.PING);
    }
}
