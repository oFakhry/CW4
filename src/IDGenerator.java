import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger attendeeId = new AtomicInteger(0);
    private static final AtomicInteger conferenceId = new AtomicInteger(0);
    private static final AtomicInteger sessionId = new AtomicInteger(0);
    private static final AtomicInteger speakerId = new AtomicInteger(0);

    public static int generateAttendeeId() {
        return attendeeId.incrementAndGet();
    }

    public static int generateConferenceId() {
        return conferenceId.incrementAndGet();
    }

    public static int generateSessionId() {
        return sessionId.incrementAndGet();
    }

    public static int generateSpeakerId() {
        return speakerId.incrementAndGet();
    }
}
