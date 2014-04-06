package bootstrap

import scala.actors.remote.RemoteActor
@SerialVersionUID(13l)
class testMessage extends Serializable{
	//val uid:Long = new SerialVersionUID()
    //String
  RemoteActor.classLoader = getClass().getClassLoader()
}
