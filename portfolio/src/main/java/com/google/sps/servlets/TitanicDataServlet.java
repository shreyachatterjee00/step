
package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns titanic survivor data by class in a hash map*/
@WebServlet("/titanic-data")
public class TitanicDataServlet extends HttpServlet {

  private LinkedHashMap<String, Double> titanicSurvivors = new LinkedHashMap<>();

  @Override
  public void init() {
    Double firstPercent = 0.0;
    Double secondPercent = 0.0;
    Double thirdPercent = 0.0;

    Double firstTotal = 0.0;
    Double secondTotal = 0.0;
    Double thirdTotal = 0.0;

    Double firstSurvived = 0.0;
    Double secondSurvived = 0.0;
    Double thirdSurvived = 0.0;

    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/train.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      Integer survived = Integer.valueOf(cells[0]);
      Integer shipClass = Integer.valueOf(cells[1]);

      switch(shipClass) {
          case 1: 
            firstSurvived += survived;
            firstTotal += 1;
          case 2: 
            secondSurvived += survived;
            secondTotal += 1;
          case 3: 
            thirdSurvived += survived;
            thirdTotal += 1;
      }

      firstPercent = firstSurvived / firstTotal;
      secondPercent = secondSurvived / secondTotal;
      thirdPercent = thirdSurvived / thirdTotal;

      titanicSurvivors.put("First Class", firstPercent);
      titanicSurvivors.put("Second Class", secondPercent);
      titanicSurvivors.put("Third Class", thirdPercent);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(titanicSurvivors);
    response.getWriter().println(json);
  }
}