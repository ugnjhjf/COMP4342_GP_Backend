package com.comp4342.frontend.api;

import org.springframework.web.socket.TextMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<TextMessage> {
    @Override
    public TextMessage decode(String s) throws DecodeException {
        return new TextMessage(s);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
