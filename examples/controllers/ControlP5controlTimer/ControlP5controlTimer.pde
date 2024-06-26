/**
 * ControlP5 Timer
 * by andreas schlegel, 2009
 */

import controlP5.*;

ControlP5 cp5;
ControlTimer c;
Textlabel t;

void setup() {
  size(400,400);
  frameRate(30);
  cp5 = new ControlP5(this);
  c = new ControlTimer();
  t = new Textlabel(cp5,"--",100,100);
  c.setSpeedOfTime(1);
}


void draw() {
  background(0);
  t.setValue(c.toString());
  t.draw(this);
  t.setPosition(mouseX, mouseY);
}


void mousePressed() {
  c.reset();
}


/*
a list of all methods available for the ControlTimer Controller
use ControlP5.printPublicMethodsFor(ControlTimer.class);
to print the following list into the console.

You can find further details about class ControlTimer in the javadoc.

Format:
ClassName : returnType methodName(parameter type)


main.java.controlp5.ControlTimer : String toString()
main.java.controlp5.ControlTimer : int day()
main.java.controlp5.ControlTimer : int hour()
main.java.controlp5.ControlTimer : int millis()
main.java.controlp5.ControlTimer : int minute()
main.java.controlp5.ControlTimer : int second()
main.java.controlp5.ControlTimer : long time()
main.java.controlp5.ControlTimer : void reset()
main.java.controlp5.ControlTimer : void setSpeedOfTime(float)
main.java.controlp5.ControlTimer : void update()
java.lang.Object : String toString() 
java.lang.Object : boolean equals(Object) 

created: 2015/03/24 12:21:02

*/


