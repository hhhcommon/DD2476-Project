4
https://raw.githubusercontent.com/abdalmoniem/Movie-App/master/base/src/main/java/butter/droid/base/providers/media/response/models/ResponseItem.java
package butter.droid.base.providers.media.response.models;

public class ResponseItem {

    public String title;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
