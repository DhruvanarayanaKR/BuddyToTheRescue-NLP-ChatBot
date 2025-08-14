package game;
import gui.test.test.Test;
import raven.chat.component.ChatBox;
import raven.chat.model.ModelMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RockPaperScissors {
  private static String playerMove;
  public static void sub() {
    String mes;
    while (true) {
      String[] rps = {"r", "p", "s"};
      String computerMove = rps[new Random().nextInt(rps.length)];
      while(true) {
        mes = "Enter your move (r p or s)";
        displayMessage("Buddy", mes);

        playerMove = null;
        getPlayerMove();
        mes = "You played: "+playerMove;
        //displayMessage("Buddy", mes);
        if (playerMove.equals("r") || playerMove.equals("p") || playerMove.equals("s")) {
          break;
        }
        mes = playerMove + " is not a valid move.";
        displayMessage("Buddy", mes);
      }

      mes =  mes +"\nComputer played: " + computerMove;
      //displayMessage("Buddy", mes);

      if (playerMove.equals(computerMove)) {
        mes = mes + "\nThe game was a tie!";
        displayMessage("Buddy", mes);
      }
      else if (playerMove.equals("r")) {
        if (computerMove.equals("p")) {
          mes = mes +"\nYou lose!";
          displayMessage("Buddy", mes);

        } else if (computerMove.equals("s")) {
          mes = mes+ "\nYou win!";
          displayMessage("Buddy", mes);
        }
      }

      else if (playerMove.equals("p")) {
        if (computerMove.equals("r")) {
          mes = mes+ "\nYou win!";
          displayMessage("Buddy", mes);

        } else if (computerMove.equals("s")) {
          mes = mes+ "\nYou lose!";
          displayMessage("Buddy", mes);
        }
      }

      else if (playerMove.equals("s")) {
        if (computerMove.equals("p")) {
          mes = mes+"\nYou win!";
          displayMessage("Buddy", mes);

        } else if (computerMove.equals("r")) {
          mes = mes+"\nYou lose!";
          displayMessage("Buddy", mes);
        }
      }

      mes = "Play again? (y/n)";
      displayMessage("Buddy", mes);
      playerMove = null;
      PLayAgain();
      if (!playerMove.equals("y"))
      {
        break;
      }
    }
}
  private static void displayMessage(String playerName, String message) {
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
    String date = df.format(new Date());
    Test.chatArea.addChatBox(new ModelMessage(null, playerName, date, message), ChatBox.BoxType.LEFT);
  }

  private static void getPlayerMove() {
    // You can use your existing logic to add a text box and submit button
    // Here, I'm using a simple JOptionPane for demonstration purposes

    String[] options = {"r", "p", "s"};
    int result = JOptionPane.showOptionDialog(
            null,
            "Select your move:",
            "Player Move",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );

    if (result >= 0 && result < options.length) {
      playerMove = options[result];
    }
  }

  private static void PLayAgain() {
    // You can use your existing logic to add a text box and submit button
    // Here, I'm using a simple JOptionPane for demonstration purposes

    String[] options = {"y", "n"};
    int result = JOptionPane.showOptionDialog(
            null,
            "Do you want to Play again?",
            "Player Choice",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );

    if (result >= 0 && result < options.length) {
      playerMove = options[result];
    }
  }
}

