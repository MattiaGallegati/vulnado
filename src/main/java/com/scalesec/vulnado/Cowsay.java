package com.scalesec.vulnado;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

  private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = \"/usr/games/cowsay \\\"\" + input.replaceAll(\"[^a-zA-Z0-9 ]\", \"\") + \"\\\"\";
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    processBuilder.command(\"bash\", \"-c\", cmd.replaceAll(\"[^a-zA-Z0-9 ]\", \"\"));

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      Logger.getLogger(Cowsay.class.getName()).log(Level.WARNING, \"Exception occurred\", e);
    }
    return output.toString();
  }
}
