package bootstrap
import java.util.Scanner
import scala.actors.Actor._
import scala.actors.remote.Node
import scala.actors.remote.RemoteActor._
import msgKind._
import scala.actors.remote.RemoteActor

object simpletest {
    RemoteActor.classLoader = getClass().getClassLoader()
    val ip:String = boot.getBoot
	def main(args: Array[String]){  
      
	  actor {
	    RemoteActor.classLoader = getClass().getClassLoader()
        val remoteActor = select(Node("localhost", 10111), 'ragtime)
        RemoteActor.classLoader = getClass().getClassLoader()
	  while(true)
      {
        println("Enter 1 to register")
        println("Enter 2 to login")
        println("Enter 3 to query")
        println("Enter 4 to logoff:")
        println("Enter 5 to exit")
        
        val scan:Scanner = new Scanner(System.in)
        val selection: Int = scan.nextInt();       
            selection match {
              case 1 => 
                println("Enter the Username and Password. Use \':\' to separate")
                scan.nextLine()
        	    var in_msg : String = scan.nextLine()
        	    in_msg = in_msg + ":"+ip+":10111"
        	    println("reg" + in_msg)
        	    val mes:UserMessage = new UserMessage(REGISTER,in_msg,null)
                remoteActor !? mes
                match {
                  case mesg: UserMessage =>
                    mesg.getKind match {
                    case NORMAL_ACK =>
                      println("Registered")
                    case BOOTSTRAP_ERROR =>
                      println("Failed. Error: "+mesg.getData.toString())
                  }
                }
              case 2 =>
                println("Enter the Username and Password. Use \':\' to separate")
                scan.nextLine()
        	    val in_msg : String = scan.nextLine()
        	    val mes:UserMessage = new UserMessage(LOGIN,in_msg,null)
                remoteActor !? mes
                match {
                  case mesg: UserMessage =>
                    mesg.getKind match {
                    case NORMAL_ACK =>
                      println("Login successful")
                    case BOOTSTRAP_ERROR =>
                      println("Failed. Error: "+mesg.getData.toString())
                  }
                }
              case 3 => 
                println("Enter the Username")
                scan.nextLine()
        	    val in_msg : String = scan.nextLine()
        	    val mes:UserMessage = new UserMessage(QUERY,in_msg,null)
                remoteActor !? mes
                match {
                  case mesg: UserMessage =>
                    mesg.getKind match {
                    case NORMAL_ACK =>
                      println("The result is: "+mesg.getData.toString())
                    case BOOTSTRAP_ERROR =>
                      println("Failed. Error: "+mesg.getData.toString())
                  }
                }
              case 4 => 
                println("Enter the username")
                scan.nextLine()
        	    val in_msg : String = scan.nextLine()
        	    val mes:UserMessage = new UserMessage(LOGOFF,in_msg,null)
                remoteActor !? mes
                match {
                  case mesg: UserMessage =>
                    mesg.getKind match {
                    case NORMAL_ACK =>
                      println("Logoff succeed")
                    case BOOTSTRAP_ERROR =>
                      println("Failed. Error: "+mesg.getData.toString())
                  }
                }
              case 5 => println("Bye")
          	    System.exit(0)
             
            }
        }
	  }
	}  
}