package dev.shvetsova.ewmc.dto.stats;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStatsDto that = (ViewStatsDto) o;
        return hits == that.hits && Objects.equals(app, that.app) && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
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

    }
}