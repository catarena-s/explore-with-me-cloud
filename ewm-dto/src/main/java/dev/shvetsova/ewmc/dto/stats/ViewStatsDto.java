package dev.shvetsova.ewmc.dto.stats;

public class ViewStatsDto implements Comparable<ViewStatsDto> {
    private String app;
    private String uri;
    private long hits;

    public ViewStatsDto(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    public ViewStatsDto() {
    }

    public static ViewStatsDtoBuilder builder() {
        return new ViewStatsDtoBuilder();
    }

    @Override
    public int compareTo(ViewStatsDto o) {
        return (int) (o.getHits() - hits);
    }

    public String getApp() {
        return this.app;
    }

    public String getUri() {
        return this.uri;
    }

    public long getHits() {
        return this.hits;
    }

    public static class ViewStatsDtoBuilder {
        private String app;
        private String uri;
        private long hits;

        ViewStatsDtoBuilder() {
        }

        public ViewStatsDtoBuilder app(String app) {
            this.app = app;
            return this;
        }

        public ViewStatsDtoBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public ViewStatsDtoBuilder hits(long hits) {
            this.hits = hits;
            return this;
        }

        public ViewStatsDto build() {
            return new ViewStatsDto(this.app, this.uri, this.hits);
        }

        public String toString() {
            return "ViewStatsDto.ViewStatsDtoBuilder(app=" + this.app + ", uri=" + this.uri + ", hits=" + this.hits + ")";
        }
    }
}