package course.concurrency.exams.auction;

public class Bid {
    private final Long id;
    private final Long participantId;
    public volatile Long price;

    public Bid(Long id, Long participantId, Long price) {
        this.id = id;
        this.participantId = participantId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public synchronized Long getPrice() {
        return price;
    }
}
