// SHA256 using base36 String generation implementation. Taking Input from User

import java.security.MessageDigest

import java.io.PrintWriter
import java.security.MessageDigest
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import akka.actor.PoisonPill
import scala.collection.mutable.ArrayBuffer
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._

object StorageForA {
  var noOfZeros: Int = 20
  var prependChar = 'a'
  var serverIp = ""
}
class ClientActor extends Actor {
  val client = context.actorSelection("akka.tcp://BitCoinSystem@" + StorageForA.serverIp + ":2552/user/client")
  //passing integer to server for case Int and get values from it
  client ! 9999

  def receive = {
    case (zeroes: Integer, tempChar: Char) =>
      println("ClientActor received " + zeroes + " from " + sender);
      StorageForA.prependChar = tempChar
      StorageForA.noOfZeros = zeroes
      println(StorageForA.noOfZeros)
    case _ => println("Received unknown msg ")
  }
}

object project1_client extends App {

  StorageForA.serverIp = args(0)

  println("Client ready")
  val system = ActorSystem("BitCoinSystemCient")
  val clientActor = system.actorOf(Props[ClientActor], name = "clientActor")
  val checkString = "000000000000000000000000000000000000000000000000000000000000000000"
  var count = 0
  var rangeStart = 0

  var timeOn: Boolean = true
  val actorList: ArrayBuffer[ActorRef] = new ArrayBuffer[ActorRef]
  val nrOfWorkers: Int = Runtime.getRuntime().availableProcessors()
  println("no of cores:	" + nrOfWorkers)

  sealed trait BitCoinMessage

  case object FindBitCoin extends BitCoinMessage
  case class Work(start: Int, nrOfElementsPerWorker: Int) extends BitCoinMessage
  case class Result(list: StringBuilder) extends BitCoinMessage
  case class miningShut(masterList: StringBuilder)

  var masterList = new StringBuilder("": String)

  initialize(nrOfWorkers, nrOfElementsPerWorker = 1000)

  def initialize(nrOfWorkers: Int, nrOfElementsPerWorker: Int) {
    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val master = system.actorOf(Props(new Master(nrOfWorkers, nrOfElementsPerWorker, listener)), name = "master")

    //Scheduler to give STOP command after 2 min
    import system.dispatcher
    system.scheduler.scheduleOnce(240000 milliseconds, master, "STOP")
    // start the mining
    master ! FindBitCoin
  }

  class Listener extends Actor {
    def receive = {
      case miningShut(masterList: StringBuilder) =>
        //Closing Master actor, giving poison pill
        val client = system.actorSelection("akka.tcp://BitCoinSystem@" + StorageForA.serverIp + ":2552/user/client")
        client ! masterList.toString()
        sender ! PoisonPill
        context.system.shutdown()
    }
  }

  class Master(nrOfWorkers: Int, nrOfElementsPerWorker: Int, listener: ActorRef) extends Actor {
    var masterList = new StringBuilder("": String)
    var nrOfResults: Int = _

    def receive = {
      case FindBitCoin =>
        for (i <- 0 until nrOfWorkers) {
          val w = context.actorOf(Props[Worker], "w" + i)
          actorList += w
          w ! Work(rangeStart, nrOfElementsPerWorker)
          rangeStart += nrOfElementsPerWorker
        }

      case Result(list: StringBuilder) =>
        if (timeOn) {
          masterList.append(list)
          rangeStart += nrOfElementsPerWorker
          sender ! Work(rangeStart, nrOfElementsPerWorker)
        }

      case "STOP" =>
        timeOn = false
        for (act <- actorList) {
          act ! PoisonPill
        }
        // Send the result to the listener
        listener ! miningShut(masterList)
      // Stops this actor and all its supervised children
      //context.stop(self)
    }
  }

  class Worker extends Actor {
    def receive = {
      case Work(start, nrOfElementsPerWorker) =>
        // println("actor started : " + sender.toString + "	range :	" + start)
        sender ! Result(calculateHashFor(start, nrOfElementsPerWorker)) // perform the work
    }
  }

  def calculateHashFor(start: Int, nrOfElementsPerWorker: Int): StringBuilder = {
    var list = new StringBuilder("": String)
    for (i <- start until (start + nrOfElementsPerWorker)) {
      var sat1: String = StorageForA.prependChar.toString
      var stringval1 = "bharath92".concat(sat1)
      var stringValue = stringval1.concat(Integer.toString(i, 36));

      var hexStr = SHA256(stringValue)
      if (numberOfZeros(hexStr))
        list.append(stringValue + " : " + hexStr + "\n")
    }
    list
  }

  def SHA256(s: String): String = {
    var md = MessageDigest.getInstance("SHA-256");
    md.update(s.getBytes());
    getHexString(md.digest(), s)
  }

  def getHexString(messageDigest: Array[Byte], s: String): String = {
    var hexString: StringBuffer = new StringBuffer

    messageDigest.foreach { messageDigest =>
      var hex = Integer.toHexString(messageDigest & 0xff)
      if (hex.length == 1) hexString.append('0'); hexString.append(hex)
    }

    hexString.toString
  }

  def numberOfZeros(hexCode: String): Boolean = {
    var checkSub = checkString.substring(0, StorageForA.noOfZeros)
    var zeros = hexCode.substring(0, StorageForA.noOfZeros)
    if (checkSub.equals(zeros)) {
      count += 1
      true
    } else
      false
  }
}
