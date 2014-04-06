package bootstrap

object msgKind extends Enumeration{
    type msgKind = Value
    val IMG_UPLOAD, IMG_DELETE, UPLOAD_ACK, NORMAL_ACK, THUMB_PUT, THUMB_ACK, REGISTER, QUERY, LOGIN, BOOTSTRAP_ERROR, LOGOFF= Value
  }
import msgKind._
import scala.actors.remote.RemoteActor
@SerialVersionUID(14l)
class UserMessage(kind:msgKind, data: Object, timestamp:TimeStamp) extends Serializable {
  
  RemoteActor.classLoader = getClass().getClassLoader()
  def getKind() : msgKind = 
  {
    return kind;
  }
  
  def getData() : Object = 
  {
    return data;
  }
  
  def getTimeStamp() : TimeStamp = 
  {
    return timestamp;
  }
  
  def canEqual(other: Any) = {
    other.isInstanceOf[bootstrap.UserMessage]
  }
  
  override def equals(other: Any) = {
    other match {
      case that: bootstrap.UserMessage => that.canEqual(UserMessage.this)
      case _ => false
    }
  }
  
  override def hashCode() = {
    val prime = 41
    prime
  }
  
  
}