package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.{AnswerState, Controller, ControllerState}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Board, Player, State}
import de.htwg.se.rummy.util.Command

import scala.collection.immutable.SortedSet

class NameCommand(newName: String, max: Int, controller: Controller) extends Command {
  override def undoStep(): Unit = {
    controller.desk = controller.desk.removePlayer(Player(newName, controller.desk.amountOfPlayers - 1, Board(SortedSet[TileInterface]()), if (controller.desk.players.nonEmpty) State.WAIT else State.TURN))
    controller.switchState(AnswerState.PLAYER_REMOVED, ControllerState.INSERTING_NAMES)
  }
  override def redoStep(): Unit = doStep()
  override def doStep(): Unit = {
    controller.desk = controller.desk.addPlayer(Player(newName, controller.desk.amountOfPlayers, Board(SortedSet[TileInterface]()), if (controller.desk.players.nonEmpty) State.WAIT else State.TURN))
    for (_ <- 1 to max) {
      controller.desk = controller.desk.takeTileFromBagToPlayer(controller.desk.players.find(_.number == controller.desk.amountOfPlayers - 1).get, controller.desk.getRandomTileInBag)
    }
    controller.switchState(AnswerState.ADDED_PLAYER, ControllerState.INSERTING_NAMES)
  }
}
