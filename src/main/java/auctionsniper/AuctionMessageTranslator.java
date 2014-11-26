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
        AuctionEvent event = AuctionEvent.from(message.getBody());

        String type = event.getType();
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else {
            listener.currentPrice(event.getCurrentPrice(), event.getIncrement());
        }
    }

    private static class AuctionEvent {

        private final Map<String, String> eventData;

        public AuctionEvent(Map<String, String> eventData) {
            this.eventData = eventData;
        }

        static AuctionEvent from(String messageBody) {
            Map<String, String> eventData = Arrays.stream(messageBody.split(";"))
                    .map((String field) -> {
                        String[] keyAndValue = field.split(":");
                        return new AbstractMap.SimpleEntry<>(keyAndValue[0].trim(), keyAndValue[1].trim());
                    })
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

            return new AuctionEvent(eventData);
        }

        public String getType() {
            return get("Event");
        }

        public int getCurrentPrice() {
            return getInt("CurrentPrice");
        }

        public int getIncrement() {
            return getInt("Increment");
        }

        private String get(String fieldName) {
            return eventData.get(fieldName);
        }

        private int getInt(String fieldName) {
            return Integer.parseInt(get(fieldName));
        }
    }
}
