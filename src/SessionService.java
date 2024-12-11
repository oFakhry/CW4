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

    public Session createSessionForConference(Conference conference, String name, LocalDateTime startTime, LocalDateTime endTime, SessionStatus status) {
        String sessionId = String.valueOf(IDGenerator.generateSessionId());
        Session newSession = new Session(sessionId, name, startTime, endTime, status);

        List<Session> allSessions = getAllSessions();
        allSessions.add(newSession);
        saveSessionsToFile(allSessions);

        return newSession;
    }


    public Session findSessionById(String id) {
        Session session = sessionRepository.findById(id);
        if (session == null) {
            throw new NoSuchElementException("Session not found for ID: " + id);
        }
        return session;
    }

    // Find a session by name
    public Session getSessionByName(String name) {
        return sessionRepository.findAll().stream()
                .filter(session -> session.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Session not found with name: " + name));
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
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

    // New method to save all sessions to the file
    public void saveSessionsToFile(List<Session> sessions) {
        // Ensure repository has a method like saveAll
        sessionRepository.saveAll(sessions);
    }
}
