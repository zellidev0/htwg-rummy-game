package de.htwg.se.rummy.model.fileIO.xml

import com.google.inject.Inject
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{BoardInterface, PlayerInterface, TileInterface}
import de.htwg.se.rummy.model.fileIO.FileIOInterface
import play.api.libs.json.JsObject

import scala.collection.immutable.SortedSet
import scala.util.Try
import scala.xml.PrettyPrinter

class FileIO @Inject() extends FileIOInterface {

  override def load: Option[DeskInterface] = {
    val file = Try(scala.xml.XML.loadFile("/target/desk.xml")).getOrElse(return None);
    var players = List[PlayerInterface]()
    var bagOfTiles = Set[TileInterface]()
    var ssets = Set[SortedSet[TileInterface]]()
    for (playerNodes <- file \\ "desk" \\ "players") {
      for (player <- playerNodes \\ "player") {
        val playerName: String = (player \ "@name").text
        val playerState: Boolean = (player \ "@state").text.toBoolean
        var board: BoardInterface = Board(SortedSet[TileInterface]())
        for (tile <- player \\ "board" \\ "tile") {
          board = board add Tile.stringToTile((tile \ "@identifier").text).get
        }
        players = players :+ Player(playerName, board, playerState)
      }

      for (tileNodes <- file \\ "desk" \\ "bagOfTiles") {
        for (tile <- tileNodes \\ "tile") {
          bagOfTiles = bagOfTiles + Tile.stringToTile((tile \ "@identifier").text.toString.trim).get
        }
      }

      for (ssetsNodes <- file \\ "desk" \\ "sets") {
        for (set <- ssetsNodes \\ "sortedSet") {
          var sorted = SortedSet[TileInterface]()
          for (tile <- set \\ "tile") {
            sorted = sorted + Tile.stringToTile((tile \ "@identifier").text.toString.trim).get
          }
          ssets = ssets + sorted
        }
      }
    }
    Some(deskBaseImpl.Desk(players, bagOfTiles, ssets))
  }

  override def save(grid: DeskInterface): Unit =
    saveString(grid)

  private def saveString(desk: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("/target/desk.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(deskToXml(desk))
    pw.write(xml)
    pw.close()
  }


  private def deskToXml(desk: DeskInterface) = {
    <desk amountOfPlayers={desk.amountOfPlayers.toString}>
      <players>
        {for {
        player <- desk.players
      } yield playerToXml(player)}
      </players>
      <bagOfTiles>
        {for {
        tile <- desk.bagOfTiles
      } yield tiletoXml(tile)}
      </bagOfTiles>
      <sets>
        {for {
        sset <- desk.table
      } yield setToXml(sset)}
      </sets>
    </desk>
  }


  private def playerToXml(player: PlayerInterface) = {
    <player name={player.name.toString}
            state={player.hasTurn.toString}>
      {boardToXml(player.tiles)}
    </player>
  }

  private def setToXml(sset: SortedSet[TileInterface]) = {
    <sortedSet>
      {sset.map(s => tiletoXml(s))}
    </sortedSet>
  }

  private def boardToXml(set: SortedSet[TileInterface]) =
    <board>
      {set.map(s => tiletoXml(s))}
    </board>

  private def tiletoXml(tile: TileInterface) =
    <tile identifier={tile.toString}></tile>

  override def toJson(grid: DeskInterface): JsObject = throw new NotImplementedError("Not available in this class, use JSON")
}
