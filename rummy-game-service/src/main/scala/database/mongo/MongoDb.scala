package database.mongo

import org.mongodb.scala._
import org.mongodb.scala.connection.ClusterSettings

object MongoDb{

  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("test")


  def main(args: Array[String]): Unit = {
    println(database.listCollections())
  }


}
