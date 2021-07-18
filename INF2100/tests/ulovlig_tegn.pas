/* Det er lov aa ha alle mulige rare ASCII-tegn i kommentarer:
   !"#%&/?'". */

program UlovligTegn;
var x: integer;
begin
   { Det er ogsaa lov i char-literaler: }
   write('!', '#', '%', '&', '/', '?', '"', '''', eol);

   { Men ikke ellers: }
   x := 127 % 4;
   write(x, eol);
end.
