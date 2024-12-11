import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger attendeeId = new AtomicInteger(0);
    private static final AtomicInteger conferenceId = new AtomicInteger(0);
    private static final AtomicInteger sessionId = new AtomicInteger(0);
    private static final AtomicInteger speakerId = new AtomicInteger(0);
    private static final AtomicInteger organizerId = new AtomicInteger(0);

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

    public static int generateOrganizerId() {
        return organizerId.incrementAndGet();
    }

    // Initialization methods to set counters based on existing data
    public static void initializeAttendeeId(int maxId) {
        attendeeId.set(maxId);
    }

    public static void initializeConferenceId(int maxId) {
        conferenceId.set(maxId);
    }

    public static void initializeSessionId(int maxId) {
        sessionId.set(maxId);
    }

    public static void initializeSpeakerId(int maxId) {
        speakerId.set(maxId);
    }

    public static void initializeOrganizerId(int maxId) {
        organizerId.set(maxId);
    }
}
