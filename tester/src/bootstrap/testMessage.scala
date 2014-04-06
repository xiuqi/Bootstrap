package bootstrap
import scala.actors.remote.RemoteActor
@SerialVersionUID(13l)
class testMessage extends Serializable{
    RemoteActor.classLoader = getClass().getClassLoader()

}