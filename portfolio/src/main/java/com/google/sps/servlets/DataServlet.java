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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String COMMENT_PARAMETER = "comment-area";
  private static final String LIST_ITEM = "listItem";
  private static final String TIME = "time";
  private static final String ITEM = "item";
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter(COMMENT_PARAMETER);
    Long timestamp = System.currentTimeMillis();

    Entity bucketListEntity = new Entity(LIST_ITEM);
    bucketListEntity.setProperty(ITEM, comment);
    bucketListEntity.setProperty(TIME, timestamp);

    datastore.put(bucketListEntity);

    response.setContentType("text/html;");
    response.getWriter().println(comment);
    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(LIST_ITEM).addSort(TIME, SortDirection.ASCENDING);

    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> buckList = new ArrayList<String>();
    for (Entity entity : results.asIterable()) {
      String item = (String) entity.getProperty(ITEM);
      buckList.add(item);
    }

    String json = convertMsgToJSON(buckList);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
 
  private String convertMsgToJSON(ArrayList<String> listParam) {
    Gson gson = new Gson();
    String json = gson.toJson(listParam);
    return json;
  }
}
