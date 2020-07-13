
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

  private static final int FIRST_CLASS = 1;
  private static final int SECOND_CLASS = 2;
  private static final int THIRD_CLASS = 3;

  private static final int FIRST_COLUMN_SURVIVED = 0;
  private static final int SECOND_COLUMN_CLASS = 1;

  @Override
  public void init() {
    TitanicStats classOneRate = new TitanicStats();
    TitanicStats classTwoRate = new TitanicStats();
    TitanicStats classThreeRate = new TitanicStats();

    final Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
      "/WEB-INF/train.csv"));

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      Integer passengerSurvived = Integer.valueOf(cells[FIRST_COLUMN_SURVIVED]);
      Integer shipClass = Integer.valueOf(cells[SECOND_COLUMN_CLASS]);

      switch (shipClass) {
        case FIRST_CLASS: 
          classOneRate.addPassenger(passengerSurvived);
        case SECOND_CLASS: 
          classTwoRate.addPassenger(passengerSurvived);
        case THIRD_CLASS: 
          classThreeRate.addPassenger(passengerSurvived);
      }

      titanicSurvivors.put("First Class", classOneRate.getSurvivorRate());
      titanicSurvivors.put("Second Class", classTwoRate.getSurvivorRate());
      titanicSurvivors.put("Third Class", classThreeRate.getSurvivorRate());
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

  class TitanicStats {
    Integer totalPassengers; 
    Integer survived; 

    TitanicStats() {
      this.totalPassengers = 0;
      this.survived = 0;
    }

    void addPassenger(Integer passenger) {
      this.totalPassengers += 1;
      this.survived += passenger;
    }

    Double getSurvivorRate() {
      return (Double.valueOf(this.survived) / Double.valueOf(this.totalPassengers));
    }
  }
}

