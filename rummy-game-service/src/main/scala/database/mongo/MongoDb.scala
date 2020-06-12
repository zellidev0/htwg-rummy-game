package database.mongo

import database.PlayerDao
import model.DeskInterface
import model.deskComp.deskBaseImpl.TileInterface
import model.fileIO.FileIOJson
import model.deskComp.deskBaseImpl.PlayerInterface
import model.deskComp.deskBaseImpl.deskImpl.Player
import model.deskComp.deskBaseImpl.deskImpl.Tile
import model.deskComp.deskBaseImpl.deskImpl.Board
import org.mongodb.scala.Observable._
import org.mongodb.scala._
import org.mongodb.scala.bson.BsonValue
import play.api.libs.json.Json

import scala.collection.immutable
import scala.collection.immutable.SortedSet
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, FiniteDuration}

class MongoDb extends PlayerDao {
  val DURATION: FiniteDuration = Duration.fromNanos(1000000000)
  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("rummy")
  val playerCollection: MongoCollection[Document] = database.getCollection("players")
  val deskCollection: MongoCollection[Document] = database.getCollection("desks")

  override def createPlayer(desk: DeskInterface): Option[DeskInterface] = {
    desk.players.foreach(player => {
      Await.result(playerCollection.insertOne(
        Document("name" -> player.name, "board" -> player.board.tiles.map(tile => tile.toString).mkString(","))
      ).toFuture(), DURATION)
    })

    Some(desk)
  }

  override def readPlayer(name: String): Option[PlayerInterface] = {
    val x = Await.result(playerCollection.find().toFuture(), DURATION)
    val list = x
      .map(doc => (doc.get("name"), doc.get("board")))
      .map(tuple => {
        (tuple._1.getOrElse(return None).asString().getValue, tuple._2.getOrElse(return None).asString().getValue)
      })

    val result = list.map(tuple => {
      (tuple._1, tuple._2.split(",").map(string => Tile.stringToTile(string).getOrElse(return None)).toSet)
    })
    val correctPlayer = result.find(p => p._1 == name).getOrElse(return None)
    Some(Player(correctPlayer._1, Board(correctPlayer._2.to[SortedSet])))
  }

  override def createGame(deskAsJsonString: String): Boolean = {
    Await.result(deskCollection.insertOne(Document("desk" -> deskAsJsonString)).toFuture(), DURATION)
    true
  }

  override def readGame(): Option[String] = {
    val x = Await.result(deskCollection.find().toFuture(), DURATION)
    val desk: Seq[String] = x
      .map(doc => doc.get("desk"))
      .map(desk => desk.getOrElse(return None).asString().getValue)

    desk.headOption
  }
}
