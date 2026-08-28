class Product {
    int id;
    String name;
    int stock;

    Product(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = Math.max(0, stock);
    }

    @Override
    public String toString() {
        return id + " " + name + " stock=" + stock;
    }
}

class ProductNode {
    Product data;
    ProductNode left;
    ProductNode right;

    ProductNode(Product data) {
        this.data = data;
    }
}

class ProductBst {
    private ProductNode root;

    boolean add(Product product) {
        if (product == null) return false;
        if (root == null) {
            root = new ProductNode(product);
            return true;
        }
        ProductNode current = root;
        while (true) {
            if (product.id == current.data.id) return false;
            if (product.id < current.data.id) {
                if (current.left == null) {
                    current.left = new ProductNode(product);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ProductNode(product);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Product find(int id) {
        ProductNode current = root;
        while (current != null) {
            if (id == current.data.id) return current.data;
            current = id < current.data.id ? current.left : current.right;
        }
        return null;
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(ProductNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.data + " | ");
        inorder(node.right);
    }
}

public class ProductBstIndexSystem {
    public static void main(String[] args) {
        ProductBst tree = new ProductBst();
        System.out.println(tree.add(new Product(300, "Keyboard", 5)));
        System.out.println(tree.add(new Product(100, "Mouse", 8)));
        System.out.println(tree.add(new Product(500, "Monitor", 2)));
        System.out.println(tree.add(new Product(200, "Hub", 4)));
        System.out.println(tree.add(new Product(100, "Duplicate", 1)));
        tree.inorder();
        System.out.println("find=" + tree.find(200));
        System.out.println("missing=" + tree.find(999));
    }
}