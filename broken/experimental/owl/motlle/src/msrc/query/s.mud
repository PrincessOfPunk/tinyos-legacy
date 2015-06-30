/*
   select nodeid, parent, light, ... sample period N
*/

if (id() == 0)
  {
    Intercept = fn () send(126, intercept_msg());
    Snoop = fn () send(126, snoop_msg());
  }
else
  {
    Timer0 = fn ()
      mhopsend(encode(vector(next_epoch(), id(), parent(), light())));

    settimer0(20);

    epoch = 0;
    next_epoch = fn () ++epoch;
  };
