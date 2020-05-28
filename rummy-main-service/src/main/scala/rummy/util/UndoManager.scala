package rummy.util

import model.DeskInterface

case class UndoManager(undoStack: List[DeskInterface] = List(), redoStack: List[DeskInterface] = List()) {

  def putOnStack(desk: DeskInterface): UndoManager =
    copy(undoStack = desk :: undoStack)

  def undoStep(): (UndoManager, Option[DeskInterface]) = undoStack match {
    case Nil           => (copy(), None)
    case head :: stack => (copy(undoStack = stack, redoStack = head :: redoStack), Some(head))
  }

  def redoStep(): (UndoManager, Option[DeskInterface]) = redoStack match {
    case Nil           => (copy(), None)
    case head :: stack => (copy(undoStack = head :: undoStack, redoStack = stack), Some(head))
  }

}
