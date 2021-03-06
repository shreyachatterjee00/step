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
import java.util.stream.Stream;


public final class FindMeetingQuery {
  public static final int MIN_INCREMENT = 5;

  /** 
  * Returns a Collection of {@link TimeRange} objects that 
  * span any available meeting times for all people in the MeetingRequest request.
  * For example, if 4pm - 8pm is free for a meeting, 
  * the Time Range object will represent that 
  * entire chunk of time rather than 4pm - 5pm, even if the duration of meeting is 1 hour. 
  */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long mtngDuration = request.getDuration();
    Collection<String> mandatoryAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    Collection<Event> allEvents = events;

    Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();

    if (optionalAttendees.size() != 0) {
      Collection<String> allAttendees = new ArrayList<String>();
      allAttendees.addAll(optionalAttendees);
      allAttendees.addAll(mandatoryAttendees);
    
      meetingTimes = findMeetingTimes(allEvents, mtngDuration, allAttendees);

      if (meetingTimes.size() == 0) {
        meetingTimes = findMeetingTimes(allEvents, mtngDuration, mandatoryAttendees);
      }
    } else {
        meetingTimes = findMeetingTimes(allEvents, mtngDuration, mandatoryAttendees);
    }
    return meetingTimes;
  }

  /**
  * If there are no optional attendees, simply create a hash map of mandatory events and find times a meeting can take place. 
  **/
  public Collection<TimeRange> findMeetingTimes (Collection<Event> allEvents, long mtngDuration, Collection<String> attendees) {
    HashMap<Integer, Integer> mandatoryEvents = makeMandatoryEventMap(allEvents, attendees);
    Collection<TimeRange> meetingTimes = getMtngAvailabilitySlots(mtngDuration, mandatoryEvents);
    return meetingTimes;
  }

  /** 
  * Creates a hash map <event start time, event end time> that ONLY contains meetings 
  * where 1 or more people from attendees must attend. 
  * If there are two meetings with the same start time,
  * only the one with a later end time is saved.
  **/
  public HashMap<Integer, Integer> makeMandatoryEventMap (Collection<Event> events, MeetingRequest request) {
    HashMap<Integer, Integer> mandatoryEvents = new HashMap<Integer, Integer>();
    Collection<String> attendees = request.getAttendees();

    for (Event event : events) {
      //check if any people in meeting request is present in this set
      for (String person : attendees) {
        if (people.contains(person)) {
          // If there's already an event with this start time in the map, but the current end time is later, add the current instead.  
          int eventStartTime = event.getWhen().start();
          int eventEndTime = event.getWhen().end();
          if (mandatoryEvents.containsKey(eventStartTime)) {
            if (eventEndTime > mandatoryEvents.get(eventStartTime)) {
              mandatoryEvents.put(eventStartTime, eventEndTime);
            }
            else {
              break;
            }
          } else {
            mandatoryEvents.put(eventStartTime, eventEndTime);
          }
        } else {
          mandatoryEvents.put(eventStartTime, eventEndTime);
        }
      }
    }
    return mandatoryEvents;
  }

  /**
  * Creates and returns an {@link Collection} of {@link TimeRange} objects that span available meeting times.
  */
  public Collection<TimeRange> getMtngAvailabilitySlots (long mtngDuration, HashMap<Integer, Integer> mandatoryEvents) {
    Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();
    long mtngDuration = request.getDuration();
    int currTime = TimeRange.START_OF_DAY;
    int startTime = TimeRange.START_OF_DAY;
    int currDuration = 0;

    while (currTime < TimeRange.END_OF_DAY) {
      // If there's an event starting at this time, see if there is a possible meeting time. 
      // Reset the current duration variable, move the current time to the end time of the event, and repeat loop. 
      if (mandatoryEvents.containsKey(currTime)) {
        int currentLongest = mandatoryEvents.get(currTime);
        if (currDuration >= mtngDuration) {
          // Not inclusive because a meeting starts at this time 
          meetingTimes.add(TimeRange.fromStartEnd(startTime, currTime, /* inclusive = */ false));
        }
        currDuration = 0;

        // Check if there is a conflict: another meeting that starts during this one, and goes later
        int longestMeetingEnd = getLongestEventEnd(currTime, currentLongest, mandatoryEvents);
            
        currTime = longestMeetingEnd;
        startTime = longestMeetingEnd;
      } else {
        // If there is no event at this time, add MIN_INCREMENT minutes to the duration and current time, and run the loop again. 
        currDuration += MIN_INCREMENT;
        currTime += MIN_INCREMENT;
      }
    }

    // When reaching the end of loop, if the current duration is longer than what is necessary, add it to the return Collection, inclusive. 
    if (currTime >= TimeRange.END_OF_DAY && currDuration >= mtngDuration) {
      meetingTimes.add(TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, /* inclusive = */ true));
    }
    return meetingTimes;
  }

  /**
  * Checks if there is a conflict: another meeting that starts during this one, and goes later. 
  * Returns the later time if there is a conflicting meeting. 
  **/
  public int getLongestEventEnd (int currTime, int longestMeetingEnd, HashMap<Integer, Integer> mandatoryEvents) {
    int tempTime = currTime + MIN_INCREMENT;
    int currentEventEnd = longestMeetingEnd;
    while (tempTime < currentEventEnd) {
      if (mandatoryEvents.containsKey(tempTime)) {
        int meetingEnd = mandatoryEvents.get(tempTime);
        if (meetingEnd > longestMeetingEnd) {
          longestMeetingEnd = meetingEnd;
        }
      }
      tempTime += MIN_INCREMENT;
    }
    return longestMeetingEnd;
  }
}
