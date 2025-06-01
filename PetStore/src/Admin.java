public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void managePets() {
        System.out.println("Admin can manage all pets and orders.");
    }

    public void viewOrders() {
        System.out.println("Admin can view and modify orders.");
    }
}
