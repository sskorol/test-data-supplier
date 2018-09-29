package io.github.sskorol.entities;

import java.util.Arrays;

public class Food {

    private final String[] likes;
    private final String[] dislikes;

    public Food(final String[] likes, final String[] dislikes) {
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String[] getLikes() {
        return likes;
    }

    public String[] getDislikes() {
        return dislikes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var food = (Food) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(likes, food.likes)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(dislikes, food.dislikes);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(likes);
        result = 31 * result + Arrays.hashCode(dislikes);
        return result;
    }

    @Override
    public String toString() {
        return "Food(" +
            "likes=" + Arrays.toString(likes) +
            ", dislikes=" + Arrays.toString(dislikes) +
            ")";
    }
}