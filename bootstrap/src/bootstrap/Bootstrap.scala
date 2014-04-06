package bootstrap
import scala.collection.mutable.HashMap
import java.net._
import java.io._
import scala.io._
import scala.actors.Actor._
import scala.actors.remote.RemoteActor._
import msgKind._
import scala.actors.remote.RemoteActor

object Bootstrap {
    RemoteActor.classLoader = getClass().getClassLoader()
    def main(args: Array[String])  {
	val portList:HashMap[String,Int] = new HashMap[String, Int]
	val ipList:HashMap[String,String] = new HashMap[String, String]
	val passList:HashMap[String,String] = new HashMap[String, String]
	val loginList:HashMap[String, Boolean] = new HashMap[String, Boolean]
	actor {
		alive(10111)
		register('ragtime, self)

		println("Server started...")

		loop {receive { 
		        case msg:testMessage =>
		          println("received test message: " )
		  		case message:UserMessage =>{
		  		  println("received: "+message)
		  		  message.getKind match 
		  		  {
		  		    case REGISTER =>
		  		      val data:String = message.getData.toString()
		  		      val dataList:Array[String] = data.split(":")
		  		      if(dataList.size!=4){//Error format
		  		        val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Invalid format",null)
		  		        reply(msg)
		  		      }
		  		      else{
		  		        val name:String = dataList(0)
		  		        val password:String = dataList(1)
		  		        if(portList.keySet.exists(_.equals(name))){
		  		          val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Name exist",null)
		  		          reply(msg)
		  		        }
		  		        else{
		  		          println(name+" : " + password+ " is going to be registered")
		  		          var port:Int = 10111
		  		          try{
		  		          	port = dataList(3).toInt
		  		          }
		  		          catch{
		  		            case e:IOException => 
		  		              e.printStackTrace()
		  		              println("Error port number, change to default")
		  		          }
		  		          val ip:String = dataList(2)
		  		          portList.put(name, port)
		  		          passList.put(name, password)
		  		          ipList.put(name, ip)
		  		          loginList.put(name,false)
		  		          println("Registration process complete")
		  		          val msg:UserMessage = new UserMessage(NORMAL_ACK,"Registration successful",null)
		  		          reply(msg)
		  		        }
		  		      }
		  		      case LOGIN => 
		  		        val data:String = message.getData.toString()
		  		        var i:Int = data.indexOf(':')
		  		        if(i<=0 || i==data.length()-1){//Error username or password
		  		          val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Invalid format",null)
		  		          reply(msg)
		  		        }
		  		        else{
		  		          val name:String = data.substring(0, i)
		  		          val password:String = data.substring(i+1)
		  		          if(portList.keySet.exists(_.equals(name))==false){
		  		            val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Name does not exist",null)
		  		            reply(msg)
		  		          }
		  		          else{
		  		            if(loginList.get(name).get){
		  		              val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Already in login status",null)
		  		              reply(msg)
		  		            }
		  		            else if(password.equals(passList.get(name).get)==false){
		  		              val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Wrong password",null)
		  		              reply(msg)
		  		            }
		  		            else{
		  		              loginList.put(name, true)
		  		              val msg:UserMessage = new UserMessage(NORMAL_ACK,"Login succeed",null)
		  		              reply(msg)
		  		            }
		  		          }
		  		        }
		  		      case QUERY => 
		  		        val name:String = message.getData.toString()
		  		        if(portList.keySet.exists(_.equals(name))==false){
		  		            val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Name does not exist",null)
		  		            reply(msg)
		  		        }
		  		        else{
		  		          if(loginList.get(name).get == false){
		  		            val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Target user not active",null)
		  		            reply(msg)
		  		          }
		  		          else{
		  		            val ans:String = name+" : "+ipList.get(name).get + " and " + portList.get(name).get
		  		            val msg:UserMessage = new UserMessage(NORMAL_ACK,ans,null)
		  		            reply(msg)
		  		          }
		  		        }
		  		      case LOGOFF =>  
		  		        val name:String = message.getData.toString()
		  		        if(portList.keySet.exists(_.equals(name))==false){
		  		            val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Name does not exist",null)
		  		            reply(msg)
		  		        }
		  		        else{
		  		          if(loginList.get(name).get == false){
		  		            val msg:UserMessage = new UserMessage(BOOTSTRAP_ERROR,"Already logged off",null)
		  		            reply(msg)
		  		          }
		  		          else{
		  		
		  		            loginList.put(name, false)
		  		            val msg:UserMessage = new UserMessage(NORMAL_ACK,"Logoff succeed",null)
		  		            reply(msg)
		  		          }
		  		        }
		  		  }    
		  		}
				
			}
		}
	}
}
}