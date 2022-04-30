public class Rat implements Comparable<Rat> {
    private String gender;
    private double weight;
    private int age;

    public Rat(String g, double wt) {
        gender = g;
        weight = wt;
        age = 0;
    }

    public String getGender() {
        return gender;
    }

    public double getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public void addYear() {
        age++;
    }

    public int compareTo(Rat other) {
        if (getWeight() == other.getWeight())
            return 0;
        if (getWeight() > other.getWeight())
            return 1;
        return -1;
    }

    public String toString() {
        String s = (gender.equals("male")) ? "male" : "female";
        s += " rat weighting " + weight;
        s += " grams and is " + age + " in age";
        return s;
    }
}