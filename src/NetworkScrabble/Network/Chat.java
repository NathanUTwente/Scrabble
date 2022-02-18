//package NetworkScrabble.Network;
//import java.io.IOException;
//
//public class Chat extends ScrabbleServer{
//
//
//    Thread sender = new Thread(new Runnable() {
//        String msg;
//
//        @Override
//        public void run() {
//        while (true){
//            msg = sc.nextLine(); //reads data from player keyboard
//            out.println(msg);
//            out.flush();
//        }
//        }
//    });
//
//    Thread recieve = new Thread(new Runnable() {
//        String msg;
//        @Override
//        public void run() {
//                try {
//                    msg = in.readLine();
//                    while (msg != null) {
//                        System.out.println("Player : " + msg);
//                        msg = in.readLine();
//                    }
//                    //When msg == null
//                    System.out.println("Player disconnected");
//                    out.close();
//                    connection.close();
//                    listener.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//    });
//
//
//
//
//
//
//}
