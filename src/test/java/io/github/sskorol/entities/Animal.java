package io.github.sskorol.entities;

public class Animal {

    private final String name;
    private final String species;
    private final Food foods;

    public Animal(final String name, final String species, final Food foods) {
        this.name = name;
        this.species = species;
        this.foods = foods;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public Food getFoods() {
        return foods;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var animal = (Animal) o;

        if (name != null ? !name.equals(animal.name) : animal.name != null) {
            return false;
        }
        if (species != null ? !species.equals(animal.species) : animal.species != null) {
            return false;
        }
        return foods != null ? foods.equals(animal.foods) : animal.foods == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (foods != null ? foods.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Animal(" +
            "name=" + name +
            ", species=" + species +
            ", foods=" + foods +
            ")";
    }
}
