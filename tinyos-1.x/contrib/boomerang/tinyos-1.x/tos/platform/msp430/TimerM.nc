//$Id: TimerM.nc,v 1.1.1.1 2007/11/05 19:10:17 jpolastre Exp $

/* "Copyright (c) 2000-2003 The Regents of the University of California.  
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement
 * is hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY
 * OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 */

// @author Cory Sharp <cssharp@eecs.berkeley.edu>

includes Timer;

module TimerM
{
  provides interface StdControl;
  provides interface LocalTime;
  provides interface Timer[uint8_t timer];
  provides interface TimerMilli[uint8_t timer];
  provides interface TimerJiffy[uint8_t timer];
  uses interface MSP430Compare as AlarmCompare;
  uses interface MSP430TimerControl as AlarmControl;
  uses interface MSP430Timer as AlarmTimer;
}
implementation
{
  enum
  {
    COUNT_TIMER_OLD = uniqueCount("Timer"),
    COUNT_TIMER_MILLI = uniqueCount("TimerMilli"),
    COUNT_TIMER_JIFFY = uniqueCount("TimerJiffy"),

    OFFSET_TIMER_OLD = 0,
    OFFSET_TIMER_MILLI = OFFSET_TIMER_OLD   + COUNT_TIMER_OLD,
    OFFSET_TIMER_JIFFY = OFFSET_TIMER_MILLI + COUNT_TIMER_MILLI,
    NUM_TIMERS = OFFSET_TIMER_JIFFY + COUNT_TIMER_JIFFY,

    EMPTY_LIST = 255,
  };

  typedef struct Timer_s
  {
    uint32_t alarm;
    uint8_t next;
    bool isperiodic : 1;
    bool isset : 1;
    bool isqueued : 1;
    int _reserved_flags : 5;
    uint8_t _reserved_byte;
  } Timer_t;

  Timer_t m_timers[NUM_TIMERS];
  int32_t m_period[NUM_TIMERS]; //outside to get struct down to 8 bytes
  uint16_t m_hinow; //tested: overflow in m_hinow can occur without ill effect
  uint8_t m_head_short;
  uint8_t m_head_long;
  bool m_posted_checkShortTimers;

  command result_t StdControl.init()
  {
    atomic m_hinow = 0;
    m_head_short = EMPTY_LIST;
    m_head_long = EMPTY_LIST;
    bzero( m_timers, sizeof(m_timers) );
    atomic m_posted_checkShortTimers = FALSE;
    call AlarmControl.setControlAsCompare();
    call AlarmControl.disableEvents();
    return SUCCESS;
  }

  command result_t StdControl.start()
  {
    return SUCCESS;
  }

  command result_t StdControl.stop()
  {
    return SUCCESS;
  }

  void insertTimer( uint8_t num, bool isshort )
  {
    if( m_timers[num].isqueued == FALSE )
    {
      if( isshort )
      {
	m_timers[num].next = m_head_short;
	m_head_short = num;
      }
      else
      {
	m_timers[num].next = m_head_long;
	m_head_long = num;
      }
      m_timers[num].isqueued = TRUE;
    }
    m_timers[num].isset = TRUE;
  }

  void removeTimer( uint8_t num )
  {
    m_timers[num].isset = FALSE;
  }

  void signal_timer_fired( uint8_t num )
  {
    // CSS 20040529 - since msp430 is a 16-bit platform, make num a signed
    // 16-bit integer to avoid "warning: comparison is always true due to
    // limited range of data type" if it happens that (num >= 0) is tested.
    const int16_t num16 = num;

    if( (COUNT_TIMER_JIFFY > 0) && (num16 >= OFFSET_TIMER_JIFFY) )
    {
      signal TimerJiffy.fired[ num - OFFSET_TIMER_JIFFY ]();
    }
    else if( (COUNT_TIMER_MILLI > 0) && (num16 >= OFFSET_TIMER_MILLI) )
    {
      signal TimerMilli.fired[ num - OFFSET_TIMER_MILLI ]();
    }
    else
    {
      signal Timer.fired[ num ]();
    }
  }

  // checkTimers assumes that the list pointed to by head has been removed
  // from the active timer lists (m_head_short and m_head_long).
  void executeTimers( uint8_t head )
  {
    uint32_t now = call LocalTime.read();
    while( head != EMPTY_LIST )
    {
      uint8_t num = head;
      bool signal_timer = FALSE;

      atomic
      {
	Timer_t* timer = &m_timers[num];
	head = timer->next; //get timer->next before it's modified
        //once next is cached, this timer is officially no longer in queue
	timer->isqueued = FALSE; 

	if( timer->isset )
	{
	  int32_t remaining = timer->alarm - now;
	  timer->isset = FALSE; //insertTimer below will mark it set if appro
	  if( remaining <= 0 )
	  {
	    // If the alarm is now or in the past, fire the timer.
	    // If it's periodic, re-set it before firing.
	    if( timer->isperiodic )
	    {
	      timer->alarm += m_period[num];
	      insertTimer( num, ((int32_t)(timer->alarm - now)) <= 0xffffL );
	    }
	    signal_timer = TRUE;
	  }
	  else
	  {
	    // If the timer is in the future, reinsert it
	    insertTimer( num, remaining <= 0xffffL );
	  }
	}
      }

      if( signal_timer )
	signal_timer_fired( num );
    }
  }

  task void checkShortTimers();

  void post_checkShortTimers()
  {
    atomic
    {
      if( !m_posted_checkShortTimers )
      {
        if( post checkShortTimers() )
          m_posted_checkShortTimers = TRUE;
      }
    }
  }

  void setNextShortEvent()
  {
    uint32_t now = call LocalTime.read();
    if( m_head_short != EMPTY_LIST )
    {
      uint8_t head = m_head_short;
      uint8_t soon = head;
      int32_t remaining = m_timers[head].alarm - now;
      head = m_timers[head].next;
      while( head != EMPTY_LIST )
      {
	int32_t dt = m_timers[head].alarm - now;
	if( dt < remaining )
	{
	  remaining = dt;
	  soon = head;
	}
	head = m_timers[head].next;
      }

      now = call LocalTime.read();
      remaining = m_timers[soon].alarm - now;

      if( remaining <= 0 )
      {
	// timers are already past due, disable events and post a check
	call AlarmControl.disableEvents();
	post_checkShortTimers();
      }
      else
      {
	// timers are in the near future, set an alarm
	//call AlarmCompare.setEvent( m_timers[soon].alarm & 0xffffL );
	atomic
	{
	  // bug in MSP430 silicon causes interrupt to get lost if the
	  // next timer event doesn't occur at least 2 ticks in the future
	  // this bug is present on the F15x/F16x/F161x series
	  // see TI SLAZ018 Device Erratasheet
	  if (remaining > 2)
	    call AlarmCompare.setEventFromNow( remaining );
	  else
	    call AlarmCompare.setEventFromNow( 2 );
	  call AlarmControl.clearPendingInterrupt();
	  call AlarmControl.enableEvents();
	}
      }
    }
    else
    {
      // timers in the far future, disable near future events
      call AlarmControl.disableEvents();
    }
  }

  // There is no race condition on moving timers from the long to short list
  // because long timers are checked upon overflow, which is consistent and
  // absolute.

  task void checkShortTimers()
  {
    uint8_t head = m_head_short;
    atomic m_posted_checkShortTimers = FALSE;
    m_head_short = EMPTY_LIST;
    executeTimers( head );
    setNextShortEvent();
  }

  task void checkLongTimers()
  {
    uint8_t head = m_head_long;
    m_head_long = EMPTY_LIST;
    executeTimers( head );
    setNextShortEvent();
  }

  // only call from within atomic context
  uint16_t readTime()
  {
    // Brano Kusy notes MSP430 User's Guide, Section 12.2.1, Note says reading
    // a counter may return garbage if its clock source is async.  The noted
    // work around is to take a majority vote.  The code reads the timer
    // register until two consequtive readings agree.

    uint16_t t0;
    uint16_t t1=call AlarmTimer.read();
    do { t0=t1; t1=call AlarmTimer.read(); } while( t0 != t1 );
    return t1;
  }

  async command uint32_t LocalTime.read()
  {
    uint32_t now;
    atomic
    {
      // If there was not an overflow, use lonow from before the flag check.
      // If the was an overflow, use lonow from after the flag check.

      uint16_t hinow = m_hinow;
      uint16_t lonow = readTime();
      if( call AlarmTimer.isOverflowPending() )
      {
	hinow++;
	lonow = readTime();
      }
      now = (((uint32_t)hinow) << 16) | lonow;
    }
    return now;
  }

  async event void AlarmCompare.fired()
  {
    post_checkShortTimers();
  }

  async event void AlarmTimer.overflow()
  {
    atomic m_hinow++;
    post checkLongTimers();
  }

  result_t setTimer( uint8_t num, int32_t jiffy, bool isperiodic )
  {
    atomic
    {
      Timer_t* timer = &m_timers[num];
      int32_t now;
      if( timer->isset )
	removeTimer( num );
      m_period[num] = jiffy;
      timer->isperiodic = isperiodic;
      now = call LocalTime.read();
      timer->alarm = now + jiffy;
      insertTimer( num, jiffy <= 0xffffL );
      setNextShortEvent();
    }
    return SUCCESS;
  }


  // ---
  // --- Wrap the above in the TimerJiffy, TimerMilli, and Timer interfaces
  // ---


  // --- TimerJiffy ---

  uint8_t fromNumJiffy( uint8_t num )
  {
    return num + OFFSET_TIMER_JIFFY;
  }

  command result_t TimerJiffy.setOneShot[uint8_t num]( int32_t jiffy )
  {
    return setTimer( fromNumJiffy(num), jiffy, FALSE );
  }

  command result_t TimerJiffy.setPeriodic[uint8_t num]( int32_t jiffy )
  {
    return setTimer( fromNumJiffy(num), jiffy, TRUE );
  }

  command result_t TimerJiffy.stop[uint8_t num]()
  {
    atomic removeTimer( fromNumJiffy(num) );
    return SUCCESS;
  }

  command bool TimerJiffy.isSet[uint8_t num]()
  {
    return m_timers[fromNumJiffy(num)].isset;
  }

  command bool TimerJiffy.isPeriodic[uint8_t num]()
  {
    return m_timers[fromNumJiffy(num)].isperiodic;
  }

  command bool TimerJiffy.isOneShot[uint8_t num]()
  {
    return !m_timers[fromNumJiffy(num)].isperiodic;
  }

  command int32_t TimerJiffy.getPeriod[uint8_t num]()
  {
    return m_period[fromNumJiffy(num)];
  }

  default event result_t TimerJiffy.fired[uint8_t num]()
  {
    return SUCCESS;
  }


  // --- TimerMilli ---

  uint8_t fromNumMilli( uint8_t num )
  {
    return num + OFFSET_TIMER_MILLI;
  }

  command result_t TimerMilli.setOneShot[uint8_t num]( int32_t milli )
  {
    return setTimer( fromNumMilli(num), milli*32, FALSE );
  }

  command result_t TimerMilli.setPeriodic[uint8_t num]( int32_t milli )
  {
    return setTimer( fromNumMilli(num), milli*32, TRUE );
  }

  command result_t TimerMilli.stop[uint8_t num]()
  {
    atomic removeTimer( fromNumMilli(num) );
    return SUCCESS;
  }

  command bool TimerMilli.isSet[uint8_t num]()
  {
    return m_timers[fromNumMilli(num)].isset;
  }

  command bool TimerMilli.isPeriodic[uint8_t num]()
  {
    return m_timers[fromNumMilli(num)].isperiodic;
  }

  command bool TimerMilli.isOneShot[uint8_t num]()
  {
    return !m_timers[fromNumMilli(num)].isperiodic;
  }

  command int32_t TimerMilli.getPeriod[uint8_t num]()
  {
    return m_period[fromNumMilli(num)];
  }

  default event result_t TimerMilli.fired[uint8_t num]()
  {
    return SUCCESS;
  }


  // --- Timer ---

  command result_t Timer.start[uint8_t num]( char type, uint32_t milli )
  {
    switch( type )
    {
      case TIMER_REPEAT:
	return setTimer( num, milli*32, TRUE );

      case TIMER_ONE_SHOT:
	return setTimer( num, milli*32, FALSE );
    }

    return FAIL;
  }

  command result_t Timer.stop[uint8_t num]()
  {
    atomic removeTimer( num );
    return SUCCESS;
  }

  default event result_t Timer.fired[uint8_t num]()
  {
    return SUCCESS;
  }
}

