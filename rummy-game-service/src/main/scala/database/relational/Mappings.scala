package database.relational

import model.deskComp.deskBaseImpl.PlayerInterface
import model.deskComp.deskBaseImpl.deskImpl.{Board, Player}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import slick.driver.H2Driver.api._

import scala.collection.immutable.SortedSet
//todo add when intellij changes imports
//import scala.concurrent.Await
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.Duration
//import slick.driver.H2Driver.api._

object CaseClassMapping {

  // the base query for the Users table
  val players = TableQuery[Players]

  val db = Database.forConfig("h2mem1")
  //
  //  try {
  //    Await.result(db.run(DBIO.seq(
  //      // create the schema
  //      players.schema.create,
  //
  //      // insert two User instances
  //      players += DbPlayer("John Doe"),
  //      players += DbPlayer("Fred Smith"),
  //
  //      // print the users (select * from USERS)
  //      players.result.map(println)
  //    )), Duration.Inf)
  //  } finally db.close

  def create(player: PlayerInterface): Boolean = {
    try {
      Await.result(db.run(DBIO.seq(
        // create the schema
        players.schema.create,
        players += DbPlayer(player.name),
      )), Duration.Inf)
      true
    } catch {
      case err: Exception =>
        println("Error in database", err)
        false;
    }
//    finally db.close
  }

  def read(): Option[PlayerInterface] = {
    var player: Option[PlayerInterface] = None
    Await.result(db.run(DBIO.seq(
      players.result.map(pl => {
        println(pl)
        player = Some(Player(pl.head.name, Board(SortedSet())))
      })
    )), Duration.Inf)

    player
  }
}


case class DbPlayer(name: String, id: Option[Int] = None)

class Players(tag: Tag) extends Table[DbPlayer](tag, "USERS") {
  // Auto Increment the id primary key column
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  // The name can't be null
  def name = column[String]("NAME")

  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User
  def * = (name, id.?) <> (DbPlayer.tupled, DbPlayer.unapply)
}


