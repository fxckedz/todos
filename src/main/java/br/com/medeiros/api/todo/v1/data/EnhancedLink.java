package br.com.medeiros.api.todo.v1.data;

public class EnhancedLink {
    private String rel;
    private String href;
    private String method;
    private String description;

    public EnhancedLink(String rel, String href, String method, String description) {
        this.rel = rel;
        this.href = href;
        this.method = method;
        this.description = description;
    }

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }

    public String getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }
}
