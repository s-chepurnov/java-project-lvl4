package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public final class Url extends Model {

    @Id
    private long id;
    private String name;
    @WhenCreated
    private Instant createdAt;

    public Url(String urlName) {
        this.name = urlName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(long urlId) {
        this.id = urlId;
    }

    public void setName(String urlName) {
        this.name = urlName;
    }

    public void setCreatedAt(Instant instant) {
        this.createdAt = instant;
    }
}
