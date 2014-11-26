package auctionsniper;

import com.google.inject.Inject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

public class AuctionMessageTranslator implements MessageListener {
    private final AuctionEventListener listener;

    @Inject
    AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    public void processMessage(Chat messageChat, Message message) {
        Map<String, String> event = unpackEventFrom(message);

        String type = event.get("Event");
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else {
            listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")), Integer.parseInt(event.get("Increment")));
        }
    }

    private Map<String, String> unpackEventFrom(Message message) {
        return Arrays.stream(message.getBody().split(";"))
                .map((String field) -> {
                    String[] keyAndValue = field.split(":");
                    return new AbstractMap.SimpleEntry<>(keyAndValue[0].trim(), keyAndValue[1].trim());
                })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
