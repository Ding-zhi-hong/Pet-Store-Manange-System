import java.io.Serializable;
public class Cat extends Pet implements Serializable{
    public Cat(String name, String gender, String breed, int age) {
        super(name, gender, breed, age);
    }
    public Cat(String name, String gender, String breed, int age,String classification,int price) {
        super(name, gender, breed, age,classification,price);
    }
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("This pet is a Cat.");
    }
}
