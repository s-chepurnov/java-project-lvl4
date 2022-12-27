package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public final class Url extends Model {

    @Id
    private long id;
    private String name;
    @WhenCreated
    private Instant createdAt;

    @OneToMany
    private List<UrlCheck> urlCheck;

    public Url(String urlName) {
        this.name = urlName;
        this.urlCheck = new ArrayList<>();
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

    public List<UrlCheck> getUrlCheck() {
        return urlCheck;
    }

    public void setUrlCheck(List<UrlCheck> list) {
        this.urlCheck = list;
    }

}
