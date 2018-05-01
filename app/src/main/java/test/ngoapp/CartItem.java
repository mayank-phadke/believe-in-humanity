package test.ngoapp;

public class CartItem {
    public CartItem() {
    }

    public CartItem(String title, int price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String title;
    public int price, quantity;
}
