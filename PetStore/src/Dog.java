import java.io.Serializable;
public class Dog extends Pet implements Serializable{
    public Dog(String name, String gender, String breed, int age) {
        super(name, gender, breed, age);
    }
    public Dog(String name, String gender, String breed, int age,String classification,int price) {
        super(name, gender, breed, age,classification,price);
    }
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("This pet is a Dog.");
    }
}
