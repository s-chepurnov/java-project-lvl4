package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public final class UrlCheck extends Model {

    @Id
    private int id;
    private int statusCode;
    private String title;
    private String h1;
    @Lob
    private String description;
    @ManyToOne
    private Url url;
    @WhenCreated
    private Instant createdAt;

    public int getId() {
        return id;
    }

    public void setId(int idUrlCheck) {
        this.id = idUrlCheck;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCodeUrlCheck) {
        this.statusCode = statusCodeUrlCheck;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String titleUrlCheck) {
        this.title = titleUrlCheck;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1UrlCheck) {
        this.h1 = h1UrlCheck;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descriptionUrlCheck) {
        this.description = descriptionUrlCheck;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url urlUrlCheck) {
        this.url = urlUrlCheck;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant instant) {
        this.createdAt = instant;
    }
}
