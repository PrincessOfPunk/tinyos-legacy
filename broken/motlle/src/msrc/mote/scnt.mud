infinite = fn ()
{
  any i, shown;

  shown = fn (n)
  {
    led!(if (n & 1) led_r_on else led_r_off);
    led!(if (n & 2) led_g_on else led_g_off);
    led!(if (n & 4) led_y_on else led_y_off);
  };

  i = 0;
  while (1)
    {
      shown(i++);
      sleep(1);
    };
};
dump("scnt", infinite);
