public class test1 {
    public static void main(String[] args) {
        System.out.println("hi main");
        test2.f(5, 6, 111, 1111);
        test2.f2();
        test3 v = new test2();
        v.vf();
        test1 t = new test1();
        t.runIt(1, null);
    }

    public void runIt_test(Object o) {
        System.out.println("in runIt_test");
        runIt(2, this);
    }
    public class MyRunnable implements Runnable {

        public void run(){
            System.out.println("My running");
        }
    }

    public void runIt(int i, Object o) {

//        Runnable myRunnable2 = new Runnable(){
//
//            public void run(){
//                System.out.println("Run It");
//            }
//        };
        //Thread thread = new Thread(myRunnable2);
        System.out.println("in runIt");
        Thread thread = new Thread(new MyRunnable());
        thread.start();
    }
}
