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

package com.google.sps;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;


public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long mtngDuration = request.getDuration();
    Collection<String> attendees = request.getAttendees();
    Collection<Event> allEvents = events;

    /* Return an array list of Time Range objects that span any available time. For example, if 4pm - 8pm is free for a meeting, 
      the Time Range object will represent that entire chunk of time rather than 4pm - 5pm, even if the duration of meeting is 1 hour. */
    Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();

    // Create a hash map <start time, ArrayList<end time>> with only the meetings where one or more people from the meeting request MUST attend
    HashMap<Integer, ArrayList<Integer>> mandatoryEvents = new HashMap<Integer, ArrayList<Integer>>();

    for (Event event : allEvents) {
      //people who must attend 
      Set<String> people = event.getAttendees();
      //check if any people in meeting request is present in this set
      for (String person : attendees) {
        if (people.contains(person)) {
          // If event already exists in hash map, add the end time to the ArrayList of end times. Otherwise, create an ArrayList for end times and add event to hash map.
          int eventStartTime = event.getWhen().start();
          int eventEndTime = event.getWhen().end();
          if (mandatoryEvents.containsKey(eventStartTime)) {
            ArrayList<Integer> currEndTimes = mandatoryEvents.get(eventStartTime);
            currEndTimes.add(eventEndTime);
          }
          else {
            ArrayList<Integer> newEndTimes = new ArrayList<Integer>();
            newEndTimes.add(eventEndTime);
            mandatoryEvents.put(eventStartTime, newEndTimes);
          }
        }  
      }
    }

    int currTime = TimeRange.START_OF_DAY;
    int startTime = TimeRange.START_OF_DAY;
    int currDuration = 0;

    while (currTime < TimeRange.END_OF_DAY) {
      /* If there's an event starting at this time, compare the current duration and meeting duration to see if there is a possible meeting time. 
        Reset the current duration variable, move the current time to the end time of the event, and repeat loop. */
      if (mandatoryEvents.containsKey(currTime)) {
        ArrayList<Integer> endTimes = mandatoryEvents.get(currTime);
        // Get list item with latest end time
        int longestMeetingEnd = Collections.max(endTimes); 
        if (currDuration >= mtngDuration) {
            // Not inclusive because a meeting starts at this time 
            meetingTimes.add(TimeRange.fromStartEnd(startTime, currTime, false));
        }
        currDuration = 0;

        // Check if there is a conflict: another meeting that starts during this one, and goes later
        int tempTime = currTime + 10;
        int currentEventEnd = longestMeetingEnd;
        while (tempTime < currentEventEnd) {
          if (mandatoryEvents.containsKey(tempTime)) {
            ArrayList<Integer> conflictMeetingEndTimes = mandatoryEvents.get(tempTime);
            int meetingEnd = Collections.max(conflictMeetingEndTimes);
            if (meetingEnd > longestMeetingEnd) {
              longestMeetingEnd = meetingEnd;
            }
          }
          tempTime += 10;
        }
          
      currTime = longestMeetingEnd;
      startTime = longestMeetingEnd;
      }

      /* If there is no event at this time, add 30 minutes to the duration and current time, and run the loop again. */
      else {
        currDuration += 10;
        currTime +=10;
       }
    }

    /* When reaching the end of loop, if the current duration is longer than what is necessary, add it to the return Collection, inclusive. */
    if (currTime > TimeRange.END_OF_DAY) {
      if (currDuration >= mtngDuration) {
        meetingTimes.add(TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true));
      }
    }
    return meetingTimes;
  }
}
