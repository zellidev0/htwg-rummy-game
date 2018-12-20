package controller

import controller.ContState.{INSERTED_NAME, PLAYER_REMOVED}
import model._
import util.Command

import scala.collection.SortedSet

class NameCommand(newName: String, max: Int, controller: Controller) extends Command {
  override def undoStep: Unit = {
    controller.desk = controller.desk.removePlayer(Player(newName, controller.desk.amountOfPlayers - 1, Board(SortedSet[Tile]()), if (controller.desk.players.nonEmpty) State.WAIT else State.TURN))
    controller.swState(PLAYER_REMOVED)
    controller.swState(ContState.INSERTING_NAMES)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    controller.desk = controller.desk.addPlayer(Player(newName, controller.desk.amountOfPlayers, Board(SortedSet[Tile]()), if (controller.desk.players.nonEmpty) State.WAIT else State.TURN))
    for (_ <- 1 to max) {
      controller.desk = controller.desk.takeTileFromBagToPlayer(controller.desk.players.find(_.number == controller.desk.amountOfPlayers - 1).get, controller.desk.randomTileInBag)
    }
    controller.swState(INSERTED_NAME)
    controller.swState(ContState.INSERTING_NAMES)
  }
}
