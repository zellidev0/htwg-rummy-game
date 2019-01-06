package model.fileIO.xml

import model.DeskInterface
import model.component.Desk
import model.component.component.component.{Board, Player}
import model.component.component.{BoardInterface, PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import util.UtilMethods

import scala.collection.SortedSet
import scala.xml.PrettyPrinter

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    val file = scala.xml.XML.loadFile("/home/julian/Documents/se/rummy/desk.xml")
    val amountOfPlayersAttr = (file \\ "desk" \ "@amountOfPlayers")
    val amountOfPlayers = amountOfPlayersAttr.text.toInt
    var players = Set[PlayerInterface]()
    var bagOfTiles = Set[TileInterface]()
    var ssets = Set[SortedSet[TileInterface]]()
    for (playerNodes <- file \\ "desk" \\ "players") {
      for (player <- playerNodes \\ "player") {
        val playerName: String = (player \ "@name").text.toString
        val playerNumber: Int = (player \ "@number").text.toInt
        val playerState: String = (player \ "@state").text.toString
        var board: BoardInterface = Board(SortedSet[TileInterface]())
        for (tile <- player \\ "board" \\ "tile") {
          board = board + UtilMethods.regexToTile((tile \ "@identifier").text.toString)
        }

        players = players.+(Player(playerName, playerNumber, board, UtilMethods.stringToState(playerState)))
      }


      for (tileNodes <- file \\ "desk" \\ "bagOfTiles") {
        for (tile <- tileNodes \\ "tile") {
          bagOfTiles = bagOfTiles + UtilMethods.regexToTile((tile \ "@identifier").text.toString.trim)
        }
      }

      for (ssetsNodes <- file \\ "desk" \\ "sets") {
        for (set <- ssetsNodes \\ "sortedSet") {
          var sorted = SortedSet[TileInterface]()
          for (tile <- set \\ "tile") {
            sorted = sorted + UtilMethods.regexToTile((tile \ "@identifier").text.toString.trim)
          }
          ssets = ssets + sorted
        }
      }
    }
    Desk(players, bagOfTiles, ssets)
  }

  def save(grid: DeskInterface): Unit = saveString(grid)


  private def saveString(desk: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("desk.xml"))
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
        sset <- desk.sets
      } yield setToXml(sset)}
      </sets>
    </desk>
  }


  private def playerToXml(player: PlayerInterface) = {
    <player name={player.getName.toString}
            number={player.getNumber.toString}
            state={player.getState.toString}>
      {boardToXml(player.getTiles)}
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
    <tile identifier={tile.identifier.toString}></tile>

}
