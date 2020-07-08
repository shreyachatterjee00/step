// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EncodingType;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.ArrayList;

@WebServlet("/entity")
public class EntityExtractionServlet extends HttpServlet {

  public final static String ENTITY_AREA = "entity-comment";
  String message;
  ArrayList<String> entities = new ArrayList<String>();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    message = request.getParameter(ENTITY_AREA);

    // Use Language API to get list of entities of message
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Document doc = Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
    AnalyzeEntitiesRequest analyze = AnalyzeEntitiesRequest.newBuilder().setDocument(doc).setEncodingType(EncodingType.UTF16).build();
    AnalyzeEntitiesResponse list = languageService.analyzeEntities(analyze);

    for (Entity entity : list.getEntitiesList()) {
      entities.add(entity.getName());
      response.getWriter().printf("Entity: %s", entity.getName());
    }

    response.setContentType("text/html");
    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertMsgToJSON(entities);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
 
  private String convertMsgToJSON(ArrayList<String> ents) {
    Gson gson = new Gson();
    String json = gson.toJson(ents);
    return json;
  }

}

