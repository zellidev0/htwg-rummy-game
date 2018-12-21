package model.fileIO.xml

import model.DeskInterface
import model.component.component.{PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface

import scala.collection.SortedSet
import scala.xml.PrettyPrinter

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    null
  }

  def save(grid: DeskInterface): Unit = saveString(grid)

  //  def saveXML(grid: DeskInterface): Unit = {
  //    scala.xml.XML.save("grid.xml", deskToXml(grid))
  //  }

  def saveString(desk: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("desk.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(deskToXml(desk))
    pw.write(xml)
    pw.close()
  }


  private def deskToXml(desk: DeskInterface) = {
    <desk>amountOfPlayers=
      {desk.amountOfPlayers}{for (player <- desk.players) {
      playerToXml(player)
    }}
      bagOfTiles =
      {for (tile <- desk.bagOfTiles) {
      tiletoXml(tile)
    }}
      sets =
      {for (sset <- desk.sets) {
      boardToXml(sset)
    }}
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

  private def boardToXml(set: SortedSet[TileInterface]) =
    <board>
      {set.map(s => tiletoXml(s))}
    </board>

  private def tiletoXml(tile: TileInterface) =
    <tile>
      <value>
        {tile.getValue}
      </value>
      <color>
        {tile.getColor.toString}
      </color>
      <ident>
        {tile.getIdent}
      </ident>
    </tile>

}
