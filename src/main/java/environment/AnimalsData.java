package environment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimalsData {

    @JsonProperty("animals_on_january_1st")
    private List<AnimalRecord> animalRecords;

    public List<AnimalRecord> getAnimalRecords() {
        return animalRecords;
    }

    public void setAnimalRecords(List<AnimalRecord> animalRecords) {
        this.animalRecords = animalRecords;
    }
    

    public static class AnimalRecord {
        private int year;
        private int heck_cattle;
        private int konik_horses;
        private int red_deer;
        private int total;

        // Getters and setters
        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getHeck_cattle() {
            return heck_cattle;
        }

        public void setHeck_cattle(int heck_cattle) {
            this.heck_cattle = heck_cattle;
        }

        public int getKonik_horses() {
            return konik_horses;
        }

        public void setKonik_horses(int konik_horses) {
            this.konik_horses = konik_horses;
        }

        public int getRed_deer() {
            return red_deer;
        }

        public void setRed_deer(int red_deer) {
            this.red_deer = red_deer;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getGrey_wolves() {
            return 0;
        }

        
    }
}
