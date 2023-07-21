package com.video.movie.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Spider implements Serializable {
	private List<Movie> movies;
	private List<movieKind> movie_kinds;

    public Spider(List<Movie> movies, List<movieKind> movie_kinds) {
        this.movies = movies;
        this.movie_kinds = movie_kinds;
    }

    public Spider() {
    }
}
