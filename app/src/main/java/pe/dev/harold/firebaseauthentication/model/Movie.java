package pe.dev.harold.firebaseauthentication.model;


import com.google.firebase.firestore.IgnoreExtraProperties;

public class Movie {

    private String title;
    private String poster;
    private String overview;

    private String releaseDate;
    private float averageRatings;
    private int totalRatings;
    private int totalReviews;

    public float getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(float averageRatings) {
        this.averageRatings = averageRatings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
}
