
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

  /** Key = First, Second, Third Class, Value = % Survivors of that class **/
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

      while (cells.length != 2) {
        System.out.println("Error, too many or too little columns. Skipping line");
        line = scanner.nextLine();   
        cells = line.split(",");   
      }

      Integer passengerSurvived = Integer.valueOf(cells[FIRST_COLUMN_SURVIVED]);
      System.out.println("survived?" + passengerSurvived);
      Integer shipClass = Integer.valueOf(cells[SECOND_COLUMN_CLASS]);

      switch (shipClass) {
        case FIRST_CLASS: 
          classOneRate.addPassenger(passengerSurvived);
          break;
        case SECOND_CLASS: 
          classTwoRate.addPassenger(passengerSurvived);
          break;
        case THIRD_CLASS: 
          classThreeRate.addPassenger(passengerSurvived);
          break;
      }
    }
    
    titanicSurvivors.put("First Class", classOneRate.getSurvivorPercent());
    titanicSurvivors.put("Second Class", classTwoRate.getSurvivorPercent());
    titanicSurvivors.put("Third Class", classThreeRate.getSurvivorPercent());   
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(titanicSurvivors);
    response.getWriter().println(json);
  }

  static class TitanicStats {
    Integer totalPassengers; 
    Integer survived; 

    TitanicStats() {
      this.totalPassengers = 0;
      this.survived = 0;
    }

    void addPassenger(Boolean survived) {
      this.totalPassengers += 1;
      if (survived) {
        this.survived += 1;
      }
    }

    Double getSurvivorPercent() {
      Double rate = (Double.valueOf(this.survived) / Double.valueOf(this.totalPassengers));
      System.out.println(this.survived);
      System.out.println(this.totalPassengers);
      return rate * 100;
    }
  }
}

