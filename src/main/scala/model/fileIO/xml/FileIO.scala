package model.fileIO.xml

import model.DeskInterface
import model.component.component.component.{Board, Player}
import model.component.component.{BoardInterface, PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import util.UtilMethods

import scala.collection.SortedSet
import scala.xml.PrettyPrinter

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    val desk: DeskInterface = null
    val file = scala.xml.XML.loadFile("/home/julian/Documents/se/rummy/desk.xml")
    val amountOfPlayers = (file \\ "desk" \ "@amountOfPlayers").text.toInt
    val players = Set[PlayerInterface]()
    for (plr <- file \\ "players") {
      val playerName: String = (plr \ "@name").text.toString
      val playerNumber: Int = (plr \ "@number").text.toInt
      val playerState: String = (plr \ "@state").text.toString
      val board: BoardInterface = Board(SortedSet[TileInterface]())
      for (tile <- file \\ "players" \\ "board") {
        board + UtilMethods.regexToTile((tile \ "@identifier").text.toString)
      }
      players.+(Player(playerName, playerNumber, board, UtilMethods.stringToState(playerState)))
      val bagOfTIles = Set[TileInterface]()
      for (tile <- file \\ "bagOfTile") {
        bagOfTIles + UtilMethods.regexToTile((tile \ "@identifier").text.toString)
      }
    }
    desk
  }

  def save(grid: DeskInterface): Unit = saveString(grid)


  def saveString(desk: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("desk.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(deskToXml(desk))
    pw.write(xml)
    pw.close()
  }

  //  def gridToXml(grid: GridInterface) = {
  //    <grid size={grid.size.toString}>
  //      {for {
  //      row <- 0 until grid.size
  //      col <- 0 until grid.size
  //    } yield cellToXml(grid, row, col)}
  //    </grid>
  //  }
  //
  //  def cellToXml(grid: GridInterface, row: Int, col: Int) = {
  //    <cell row={row.toString} col={col.toString} given={grid.cell(row, col).given.toString} isHighlighted={grid.isHighlighted(row, col).toString} showCandidates={grid.cell(row, col).showCandidates.toString}>
  //      {grid.cell(row, col).value}
  //    </cell>
  //  }


  private def deskToXml(desk: DeskInterface) = {
    <desk>amountOfPlayers=
      {desk.amountOfPlayers}<players>
      {for {
        player <- desk.players
      }
        yield playerToXml(player)}
    </players>
      <bagOfTiles>
        {for {
        tile <- desk.bagOfTiles}
        yield tiletoXml(tile)}
      </bagOfTiles>
      <sets>
        {for {sset <- desk.sets}
        yield setToXml(sset)}
      </sets>
    </desk>
  }


  private def playerToXml(player: PlayerInterface) = {
    <player>name=
      {player.getName}
      number=
      {player.getNumber.toString}
      state=
      {player.getState.toString}
      board=
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
    <tile>identifier =
      {tile.identifier}
    </tile>

}
