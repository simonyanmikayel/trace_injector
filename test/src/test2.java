import java.lang.reflect.Parameter;

public class test2 implements test3{
    static void f(int i1, int i2, int i3, int i4) {
        System.out.println("test2.f");
    }

    static void f2() {
        System.out.println("test2.f2");
    }

    @Override
    public void vf() {
        System.out.println("test2.vf");
    }
}
