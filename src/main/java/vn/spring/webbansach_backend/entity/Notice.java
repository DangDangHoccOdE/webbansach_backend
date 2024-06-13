package vn.spring.webbansach_backend.entity;

public class Notice {
    private String content;

    public Notice(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
