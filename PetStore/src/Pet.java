import java.io.Serializable;
public class Pet implements Serializable {
    private String name;
    private String gender;
    private String breed;
    private int age;
    private static final long serialVersionUID = 1L;
    private String classification;
    private int price;
    public Pet(String name, String gender, String breed, int age) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
    }
    public Pet(String name, String gender, String breed, int age, String classification, int price) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.classification = classification;
        this.price = price;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setBreed(String breed){
        this.breed = breed;
    }
    public void setAge(int age){
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }
    public String getClassification() {
        return classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public void displayInfo() {
        System.out.println("Pet Name: " + name);
        System.out.println("Gender: " + gender);
        System.out.println("Breed: " + breed);
        System.out.println("Age: " + age);
    }
}

