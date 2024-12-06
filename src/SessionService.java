import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(String name, LocalDateTime startTime, LocalDateTime endTime, SessionStatus status) {
        String id = "SESSION-" + IDGenerator.generateSessionId();
        Session session = new Session(id, name, startTime, endTime, status);
        sessionRepository.save(session);
        return session;
    }

    public Session findSessionById(String id) {
        Session session = sessionRepository.findById(id);
        if (session == null) {
            throw new NoSuchElementException("Session not found for ID: " + id);
        }
        return session;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public void registerAttendee(String sessionId, String attendeeId) {
        Session session = findSessionById(sessionId);
        session.registerAttendee(attendeeId);
        sessionRepository.save(session);
    }

    public void deregisterAttendee(String sessionId, String attendeeId) {
        Session session = findSessionById(sessionId);
        session.deregisterAttendee(attendeeId);
        sessionRepository.save(session);
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public void updateSessionDetails(String sessionId, String newName, LocalDateTime newStartTime, LocalDateTime newEndTime, SessionStatus newStatus) {
        Session session = findSessionById(sessionId);
        session.setName(newName);
        session.setStartTime(newStartTime);
        session.setEndTime(newEndTime);
        session.setStatus(newStatus);
        sessionRepository.save(session);
    }
}
