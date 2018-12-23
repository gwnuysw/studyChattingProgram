package chattingProgram;

import java.io.*;
import java.net.*;

public class ChatServer
{
   public static final int cs_port = 2777;
   public static final int cs_maxclient=10;

// wait client, make socket
   public static void main(String args[]){
      try{
         ServerSocket ss_socket = new ServerSocket(cs_port);
         while(true){
            Socket sock = null;
            ServerThread client = null; //serverSocket for communicate with client
            try{
               sock = ss_socket.accept(); //wait client connection
               client = new ServerThread(sock); 
               client.start();
            }catch(IOException e){
               System.out.println(e);
               try{
                  if(sock != null)
                     sock.close();
               }catch(IOException e1){
                  System.out.println(e);
               }finally{
                  sock = null;
               }
            }
         }
      }catch(IOException e){
         // todo when server faced errors...
      }
   }
}