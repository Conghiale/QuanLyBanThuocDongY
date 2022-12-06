package model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String username;
    private String email;
    private String password;
    private List<Integer> listIDMedicineCart;

    public Account() {
    }

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.listIDMedicineCart = new ArrayList<> ();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getListIDMedicineCart() {
        return listIDMedicineCart;
    }

    public void setListIDMedicineCart(List<Integer> listIDMedicineCart) {
        this.listIDMedicineCart = listIDMedicineCart;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", IDMedicineCart=" + listIDMedicineCart +
                '}';
    }
}
