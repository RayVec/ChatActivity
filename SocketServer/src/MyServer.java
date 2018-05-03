import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 class MyServer extends Thread{
      public Socket client;
      public MyServer(Socket s) throws Exception{
          this.client=s;
      }
      public void run() {
          try {
              BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
              PrintWriter os = new PrintWriter(client.getOutputStream());
              BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
              while (true) {

                  String str = sin.readLine();//从系统标准输入读入一字符串
                  os.println(str);
                  os.flush(); //刷新输出流，使Server马上收到该字符串
                  String s = is.readLine();
                  System.out.println("客户端发来消息 : " + s);
                  if (str.equals("end")) {
                      break;
                  }

              }
              is.close();
              os.close();
              client.close();
          }
          catch (Exception e){
              e.printStackTrace();
          }
      }
      public static void main(String args[])throws  Exception{
             ServerSocket serer=new ServerSocket(5469);
             while(true){
                 MyServer s=new MyServer(serer.accept());
                 s.start();
             }
      }

}

