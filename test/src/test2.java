public class test2 {
    static void f() {
        System.out.println("test2.f()");
        try {
            Class cls = Class.forName("dummy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    static void f2() {
        System.out.println("test2.f()");
        try {
            Class cls = Class.forName("dummy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
