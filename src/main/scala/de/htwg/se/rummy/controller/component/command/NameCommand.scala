package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.{AnswerState, Controller, ControllerState}
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Board, Player}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import de.htwg.se.rummy.util.Command

import scala.collection.immutable.SortedSet


class NameCommand(newName: String, max: Int, controller: Controller) extends Command {

  override def undoStep(): Unit = {
    controller.desk =
      controller.desk.removePlayer(controller.getPlayerByName(newName).get)
    controller.switchState(AnswerState.REMOVED_PLAYER, ControllerState.INSERTING_NAMES)
  }

  override def redoStep(): Unit =
    doStep()

  override def doStep(): Unit = {
    controller.desk = takeMaxTilesFromBagToPlayersBoard(
      controller.desk addPlayer Player(newName, Board(SortedSet[TileInterface]()), controller.getAmountOfPlayers == 0))
    controller.switchState(AnswerState.ADDED_PLAYER, ControllerState.INSERTING_NAMES)
  }

  /** Adds max tiles to the player. Works recursively. */
  def takeMaxTilesFromBagToPlayersBoard(desk: DeskInterface, count: Int = 0): DeskInterface = count match {
    case count if this.max == count => desk
    case _ => takeMaxTilesFromBagToPlayersBoard(takeTileFromBagToPlayer(desk), count + 1)
  }

  /** Wrapper to take one tile from the bag and put it on the users board */
  def takeTileFromBagToPlayer(desk: DeskInterface): DeskInterface = {
    desk.takeTileFromBagToPlayer(desk.getPlayerByName(newName).get, desk.getTileFromBag)
  }



}
