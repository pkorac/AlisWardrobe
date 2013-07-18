import controlP5.*;
import processing.video.*;

// Global vars
boolean supPressed;
boolean supReleased = true;
float mx, my;

boolean tryMode; // Am I trying it on or fiddling with it
boolean info = false;
boolean camOn = false;
float scaling = 1;

PFont f;

FileLoader fl;
Outfit outfit;

Capture cam;

ControlP5 controlP5;
int ic = color( 62, 151, 157 ); // info color
int bc = color( 210 ); // initial background color
int lc = color( 120 ); // locked background color
int ac = color( 80 ); // active color
int fc = color( 180 ); // foreground color
int lbc = color ( 0 );  // label color

Button b; // fuck knows why


// Mighty Setup //////////////////////////////////////////////////////////////////////
void setup() {
  size( 800, 600 );
//  size( screen.width, screen.height-21 );
  smooth();
  frameRate(25);

  f = loadFont( "Misopop-36.vlw");
  textFont(f);

  cam = new Capture(this, 640, 480);

  // Interface and Buttons
  controlP5 = new ControlP5(this);

  int stx = 183;
  int sty = 19;
  int stw = 82;
  controlP5.addButton("UNIFORM GENERATOR", 0, 19, 19, 163, 19);
  controlP5.addButton("PALETTE", 0, 19, 39, 163, 19);
  controlP5.addButton("SHIRT", 0, stx, sty, stw-1, 19);
  controlP5.addButton("BOTTOMS", 0, stx+stw, sty, stw-1, 19);
  controlP5.addButton("CARDIGANS", 0, stx+stw*2, sty, stw-1, 19);
  controlP5.addButton("JUMPERS", 0, stx+stw*3, sty, stw-1, 19);
  controlP5.addButton(" ", 0, stx+stw*4, sty, width-(stx+stw*4) - 43, 19);
  controlP5.addButton("INFO", 0, width-42, sty, 23, 19);

  controlP5.addButton("TRY", 0, 19, height-38, 163, 19);
  controlP5.addButton("WEBCAM ON", 0, 19, height-58, 58, 19);
  controlP5.addButton("SAVE", 0, width-120, height-38, 50, 19);
  controlP5.addButton("QUIT", 0, width-69, height-38, 50, 19);
  controlP5.addButton("RESET", 0, width-69, height-58, 50, 19);
  
  controlP5.addButton(" +", 0, 183, height-38, 23, 19);
  controlP5.addButton(" -", 0, 207, height-38, 23, 19);

  controlP5.setColorBackground( bc );
  controlP5.setColorActive( ac );
  controlP5.setColorForeground( fc );
  controlP5.setColorLabel( lbc );
  controlP5.setMoveable(false);

  controlP5.controller("UNIFORM GENERATOR").lock();
  controlP5.controller("UNIFORM GENERATOR").setColorBackground( ic );
  controlP5.controller("UNIFORM GENERATOR").setColorLabel( color(255) );
  controlP5.controller("PALETTE").setColorBackground( color( 255, 255, 255, 0) );
  controlP5.controller("PALETTE").lock();
  controlP5.controller("INFO").setColorBackground( ic );
  controlP5.controller("INFO").setColorLabel( color(255) );
  controlP5.controller("SHIRT").lock();
  controlP5.controller("SHIRT").setColorBackground( lc );
  controlP5.controller("SHIRT").setColorLabel( color(255) );

  controlP5.controller("WEBCAM ON").hide();
  controlP5.controller("SAVE").hide();
  controlP5.controller("TRY").setColorBackground( ic );
  controlP5.controller("TRY").setColorLabel( color(255) );
  controlP5.controller(" ").lock();

  fl = new FileLoader();
  outfit = new Outfit();
}


// The lonelly draw ////////////////////////////////////////////////////////////////
void draw() {
  background( 255 );
  mx = mouseX;
  my = mouseY; 

  if ( camOn ) {
    frameRate(15);
    myMirror();
  } else{
   frameRate(30); 
  }

  outfit.display();
  info();
  //println( supPressed + " and released: " + supReleased);
}



// Others /////////////////////////////////////////////////////////////////////////
void mousePressed() {
  if ( mouseButton == LEFT ) {
    supPressed = true;
    supReleased = false;
  }
}

void mouseReleased() {
  supReleased = true;
  supPressed = false;
}

void mouseDragged() {
  supPressed = false;
  supReleased = false;
}


//// THE INTERFACE //////////////////////////////////////////////////
void info() {
  if (info) {
    fill(200);
    noStroke();
    rectMode(CORNERS);
    rect( 19, 57, width-19, height-58);

    fill(255);
    textFont(f, 36);
    text("DESIGNED BY: PETER KORACA & ALINA BREUIL", 38, height-77 );
    fill(255, 200);
    textFont(f, 13);
    text("WWW.PETERKORACA.COM", 555, height-77 );
  }
}

void buttonsWhite() {
  controlP5.controller("SHIRT").setColorBackground( bc );
  controlP5.controller("BOTTOMS").setColorBackground( bc );
  controlP5.controller("CARDIGANS").setColorBackground( bc );
  controlP5.controller("JUMPERS").setColorBackground( bc );

  controlP5.controller("SHIRT").setColorLabel( lbc );
  controlP5.controller("BOTTOMS").setColorLabel( lbc );
  controlP5.controller("CARDIGANS").setColorLabel( lbc );
  controlP5.controller("JUMPERS").setColorLabel( lbc );

  controlP5.controller("SHIRT").unlock();
  controlP5.controller("BOTTOMS").unlock();
  controlP5.controller("CARDIGANS").unlock();
  controlP5.controller("JUMPERS").unlock();


  outfit.Shirt.visible = false;
  outfit.Bottoms.visible = false;
  outfit.Cardigans.visible = false;
  outfit.Jumpers.visible = false;
}

void trying() {
  if ( tryMode ) {
    controlP5.controller("TRY").setColorBackground( lc );

    controlP5.controller("SHIRT").hide();
    controlP5.controller("BOTTOMS").hide();
    controlP5.controller("CARDIGANS").hide();
    controlP5.controller("JUMPERS").hide();
    controlP5.controller("UNIFORM GENERATOR").hide();
    controlP5.controller("INFO").hide();
    controlP5.controller("PALETTE").hide();


    controlP5.controller(" +").show();
    controlP5.controller(" -").show();
    controlP5.controller("WEBCAM ON").show();
    controlP5.controller("SAVE").show();
    controlP5.controller(" ").hide();
  } 
  else {
    controlP5.controller("TRY").setColorBackground( ic );

    controlP5.controller("SHIRT").show();
    controlP5.controller("BOTTOMS").show();
    controlP5.controller("CARDIGANS").show();
    controlP5.controller("JUMPERS").show();
    controlP5.controller("UNIFORM GENERATOR").show();
    controlP5.controller("INFO").show();
    controlP5.controller("PALETTE").show();

    controlP5.controller(" +").show();
    controlP5.controller(" -").show();
    controlP5.controller("WEBCAM ON").hide();
    controlP5.controller("SAVE").hide();
    controlP5.controller(" ").show();
  }
}


// BUTTONS ////////////////////////////////////////////////////////////
public void controlEvent(ControlEvent theEvent) {
  supPressed = false;

//  println(theEvent.controller().name());

  if ( theEvent.controller().name() == "TRY" ) {
    tryMode = !tryMode;
    trying();
    if ( camOn) camOn = false;
  } 
  else if ( theEvent.controller().name() == " +" ) {
    scaling += 0.1;
  } 
  else if ( theEvent.controller().name() == " -" ) {
    scaling -= 0.1;
  } 
  else if ( theEvent.controller().name() == "SHIRT" ) {

    buttonsWhite();
    controlP5.controller("SHIRT").lock();
    controlP5.controller("SHIRT").setColorBackground( lc );
    controlP5.controller("SHIRT").setColorLabel( color(255) );
    outfit.Shirt.visible = true;
  } 
  else if ( theEvent.controller().name() == "BOTTOMS" ) {

    buttonsWhite();
    controlP5.controller("BOTTOMS").lock();
    controlP5.controller("BOTTOMS").setColorBackground( lc );
    controlP5.controller("BOTTOMS").setColorLabel( color(255) );
    outfit.Bottoms.visible = true;
  } 
  else if ( theEvent.controller().name() == "CARDIGANS" ) {
    buttonsWhite();
    controlP5.controller("CARDIGANS").lock();
    controlP5.controller("CARDIGANS").setColorBackground( lc );
    controlP5.controller("CARDIGANS").setColorLabel( color(255) );
    outfit.Cardigans.visible = true;
  } 
  else if ( theEvent.controller().name() == "JUMPERS" ) {
    buttonsWhite();
    controlP5.controller("JUMPERS").lock();
    controlP5.controller("JUMPERS").setColorBackground( lc );
    controlP5.controller("JUMPERS").setColorLabel( color(255) );
    outfit.Jumpers.visible = true;
  }
  else if ( theEvent.controller().name() == "SAVE" ) {
    saveFrame("myOutfit-####.png");
  }
  else if ( theEvent.controller().name() == "INFO" ) {
    info = !info;
  }
  else if ( theEvent.controller().name() == "WEBCAM ON" ) {
    camOn = !camOn;
    if ( camOn ) controlP5.controller("WEBCAM ON").setColorBackground( color(80) );
    if ( !camOn ) controlP5.controller("WEBCAM ON").setColorBackground( bc );
  }
    else if ( theEvent.controller().name() == "QUIT" ) {
      exit();
    }
    else if ( theEvent.controller().name() == "RESET" ) {
      outfit = new Outfit();
    }
}

void myMirror() {

 if ( cam.available() ) {
    cam.read();
    
    float ar = width/(float)height;
    float ari = cam.width/(float)cam.height;
    float at = ar/(float)ari;

    pushMatrix();
      translate(width, 0);
      scale(-1,1);
      image(cam, 0, 0, width, height*at); 
    popMatrix();
  }
  
  
}

