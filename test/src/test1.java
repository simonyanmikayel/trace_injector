public class test1 {
    public static void main(String[] args) {
        System.out.println("hi 3");
        test2.f();
        try {
            Class cls = Class.forName("dummy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
