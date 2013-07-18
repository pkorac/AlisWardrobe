import processing.core.*; 
import processing.xml.*; 

import controlP5.*; 
import processing.video.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class aliswardrobe_tests20 extends PApplet {




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
public void setup() {
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
public void draw() {
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
public void mousePressed() {
  if ( mouseButton == LEFT ) {
    supPressed = true;
    supReleased = false;
  }
}

public void mouseReleased() {
  supReleased = true;
  supPressed = false;
}

public void mouseDragged() {
  supPressed = false;
  supReleased = false;
}


//// THE INTERFACE //////////////////////////////////////////////////
public void info() {
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

public void buttonsWhite() {
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

public void trying() {
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
    scaling += 0.1f;
  } 
  else if ( theEvent.controller().name() == " -" ) {
    scaling -= 0.1f;
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

public void myMirror() {

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


class FileLoader {

  String folderPath;

  FileLoader() {

  } 

  public File[] filePaths( String subFolder ) {

    folderPath = sketchPath + subFolder;

    if ( folderPath != null ) {
      File file = new File(folderPath);
      File[] files = file.listFiles();

      if ( files != null ) {
        return files;
      } 
      else {
        println( "No Files Found Here:" ); 
        println(  folderPath );
        return null;
      }
    } 
    else {
      println( "No Such Folder" ); 
      return null;
    }
  } // End of FilePaths
}


class Layer {

  ArrayList shapes;
  int[] colours = new int[6];
  int[] coloursMultiply = new int[6];

  boolean visible = true;

  Layer( String name, boolean visible_) {

    // The Default Colour Palette
    colours[0] = color( 255 );  // This is the 'Empty Colour'
    colours[1] = color( 78, 181, 188 );
    colours[2] = color( 214, 59, 2 );
    colours[3] = color( 97, 184, 90 );
    colours[4] = color( 214, 222, 224 );
    colours[5] = color( 39, 37, 50 );

    coloursMultiply[0] = color( 255 );  // This is the 'Empty Colour'
    coloursMultiply[1] = color( 78, 181, 188, MULTIPLY );
    coloursMultiply[2] = color( 214, 59, 2, MULTIPLY );
    coloursMultiply[3] = color( 97, 184, 90, MULTIPLY );
    coloursMultiply[4] = color( 214, 222, 224, MULTIPLY );
    coloursMultiply[5] = color( 39, 37, 50, MULTIPLY );

    visible = visible_;

    shapes = new ArrayList();

    // Get the fileNames of the shapes to load in the Array
    File[] filePaths = fl.filePaths( name );

    float x = 39;
    float y = 79;
    for ( int i = 0; i < filePaths.length; i++ ) { 

      myShape[] colouredShapes = new myShape[ colours.length ];

      for ( int j = 0; j < colours.length; j++ ) {

        if ( j == 0 ) {
          colouredShapes[j] = new myShape( 
          filePaths[i], x, y, colours[j], 40, true, colouredShapes ); // Create the default shape
        } 
        else {
          colouredShapes[j] = new myShape( 
          filePaths[i], x, y, colours[j], 40, false, colouredShapes ); // Create Other Shapes
        }


        x += 41;
        if ( j == colours.length-1 ) {
          x = 39;
          y += 41;
        }
      }

      shapes.add( colouredShapes );
    }
  }

  // DISPLAY ////////////////////////////////////////////////////////////////////////////////////
  public void display( float dx, float dy, float x, float y) {
    for ( int i = 0; i < shapes.size(); i++ ) { // Shapes
      myShape[] s = (myShape[]) shapes.get(i);
      for ( int j = 0; j < colours.length; j ++ ) { // Colours
        if ( camOn ) {
          s[j].c = coloursMultiply[j];
        } 
        else {
          s[j].c = colours[j];
        }

        if ( visible ) {
          s[j].display( dx, dy, x, y );
        } 
        else if ( !visible && s[j].isLifted() && !s[j].isDefault() ) {
          s[j].display( dx, dy, x, y );
        }
      }
    }
  }
}


class Outfit {

  float x, y;
  float dx, dy;
  float outfitSize = 200;

  boolean locked;
  boolean lifted;

  PShape baseLayer;

  Layer Shirt;
  Layer Bottoms;
  Layer Cardigans;
  Layer Jumpers;
//  Layer Hoodie;
//  Layer Blazer;
//  Layer TopElements;

  Outfit() {

    baseLayer = loadShape("/data/base/base.svg");
    baseLayer.disableStyle();

    Shirt = new Layer( "/data/shirt/", true );
    Bottoms = new Layer( "/data/bottoms/", false );
    Cardigans = new Layer( "/data/cardigans/", false );
    Jumpers = new Layer( "/data/jumpers/", false );

//    Hoodie = new Layer( "/data/hoodie/", false );
//    Blazer = new Layer( "/data/blazer/", false );
//    TopElements = new Layer( "/data/topelements/", false );

    x = 2*width/3;
    y = height/2;
  }

  public void display() {

    if ( tryMode ) {
      rectMode(CENTER);
      noFill();
      stroke(0, 20);
      rect( x, y, outfitSize*scaling*1.2f, outfitSize*scaling*1.2f );
    } 
    else {
      noStroke();
      fill( 0, 20 );
      shapeMode(CENTER);
      shape( baseLayer, x, y, outfitSize*scaling, outfitSize*scaling );
      rectMode(CENTER);
      noFill();
      stroke(0, 20);
      rect( x, y, outfitSize*scaling*1.2f, outfitSize*scaling*1.2f );
    }

    Shirt.display( dx, dy, x, y );
    Bottoms.display( dx, dy, x, y );
    Cardigans.display( dx, dy, x, y );
    Jumpers.display( dx, dy, x, y );
//    Hoodie.display( dx, dy, x, y );
//    Blazer.display( dx, dy, x, y );
//    TopElements.display( dx, dy, x, y );


    // TRY MODE CLICK AND DRAG
    if ( tryMode ) {
      clickAndDrag();
      if ( hovered() ) {
        fill( ic );
        stroke(lbc);
        ellipse( x, y, 10, 10 );
      }
    }
  }


  // HOVERED OUTFIT ////////////////////////////////
  public boolean hovered() {
    if ( mx > x - 0.5f*outfitSize && mx < x + 0.5f*outfitSize && 
      my > y - 0.5f*outfitSize && my < y + 0.5f*outfitSize ) {
      return true;
    } 
    else {
      return false;
    }
  }


  // ISLOCKED CHECKER OUTFIT ////////////////////
  public boolean isLocked() {
    return locked;
  }


  // CLICK AND DRAG OUTFIT ////////////////////
  public void clickAndDrag() {

    if ( supPressed && hovered() ) {
      locked = true;
      //      println("JIHAD");
    }

    // The Release
    if ( supReleased ) {
      locked = false;
    }

    // Being Dragged
    if ( locked && !supPressed && !supReleased ) {
      dx = x - mx;
      dy = y - my;
      x = mx;
      y = my;
    } 
    else {
      dx = 0;
      dy = 0;
    }
  }
}


class myShape {

  PShape s;
  float x, y;
  float initX, initY;
  float sSize; // small size / standard size
  float aSize = 200; // big size / away size
  int c;

  boolean locked;
  boolean lifted;
  boolean theDefault = false;

  myShape[] others;

  myShape( File fileName, float x_, float y_, int c_, float sSize_, 
  boolean theDefault_, myShape[] others_ ) {

    s = loadShape( fileName.toString() );
    s.disableStyle();

    x = initX = x_;
    y = initY = y_;
    c = c_;
    sSize = sSize_;

    others = others_;
    theDefault = theDefault_;
  }

  // DISPLAY //
  public void display( float dx, float dy, float oX, float oY ) {
    
    extraLifting();

    if ( tryMode ) {
      if ( lifted && !theDefault ) {
        x -= dx;
        y -= dy;
        fill(c);
        stroke(0, 200);
        strokeCap(ROUND);
        strokeWeight(0.5f);
        shapeMode(CENTER);
        shape( s, x, y, aSize*scaling, aSize*scaling );
      }
    } 
    else {
      clickAndDrag( oX, oY);

      // DRAW THE SHAPE
      fill(c);
      noStroke();
      shapeMode(CENTER);
      if ( isLifted() && theDefault ) {  
        rectMode(CENTER);
        noStroke();
        fill(0, 20); 
        rect( x, y, sSize, sSize );
        noStroke();

        fill(c);
        shape( s, x, y, sSize, sSize );
        if ( hovered() && supReleased ) { // HOVERING
          rectMode(CENTER);
          noFill();
          stroke(ac, 150); 
          rect( x, y, sSize, sSize );
        }
      } 
      else if ( isLifted() ) {  // BIG SHAPE ON THE OUTFIT
        fill(c);
        stroke(0, 200);
        strokeCap(ROUND);
        strokeWeight(0.5f);
        shape( s, x, y, aSize*scaling, aSize*scaling );
        if ( hovered() && supReleased ) { // HOVERING
          rectMode(CENTER);
          noFill();
          stroke(ac, 150); 
          rect( x, y, aSize, aSize );
        }
      } 
      else { ///////////////////////// SMALL SHAPES IN THE PALETTE /////////////
        if( theDefault ) {
          rectMode(CENTER);
          noStroke();
          fill(0, 20); 
          rect( x, y, sSize, sSize );
          noStroke();
        } else {
        rectMode(CENTER);
        noStroke();
        fill(0, 10); 
        rect( x, y, sSize, sSize );
        noStroke();
        }
        fill(c);
        shape( s, x, y, sSize, sSize );
        if ( hovered() && supReleased ) { // HOVERING
          rectMode(CENTER);
          noFill();
          stroke(ac, 150); 
          rect( x, y, sSize, sSize );
        }
      }

      if ( !theDefault && !lifted ) {
        rectMode(CENTER);
        noFill();
        stroke(bc, 10);
        rect( x, y, sSize, sSize );
      }
    }
  } // end of Display()



  // Other Functions ////////////////////////////////////////////////////////////////////////////////////////

  // HOVERED ////////////////////////////////
  public boolean hovered() {
    if ( !isLifted() ) {
      if ( mx > x - 0.5f*sSize && mx < x + 0.5f*sSize && 
        my > y - 0.5f*sSize && my < y + 0.5f*sSize ) {
        return true;
      } 
      else {
        return false;
      }
    } 
    else {
      if ( mx > x - 0.20f*aSize*scaling && mx < x + 0.20f*aSize*scaling && 
        my > y - 0.5f*aSize*scaling && my < y + 0.5f*aSize*scaling && !theDefault) {
        return true;
      } 
      else {
        return false;
      }
    }
  }



  // LIFTED ////////////////////////////////
  public boolean isDefault() {
    return theDefault;
  }

  // LIFTED ////////////////////////////////
  public boolean isLifted() {
    return lifted;
  }


  // ISLOCKED CHECKER ////////////////////
  public boolean isLocked() {
    return locked;
  }

  public void extraLifting(){
   if ( !lifted && !locked ){
    x = initX;
    y = initY;
   } 
  }

  // CLICK AND DRAG ////////////////////
  public void clickAndDrag( float oX, float oY ) {

    boolean othersLocked = false;
    boolean othersLifted = false;
    for ( int i = 0; i < others.length; i ++ ) {
      if ( others[i].isLocked() ) othersLocked = true;
      if ( others[i].isLifted() ) othersLifted = true;
    }

    if ( supPressed && hovered() && !othersLocked ) {
      locked = true;
      lifted = true;
      //      println("JIHAD");
    }

    // Being Dragged
    if ( locked && !supPressed && !supReleased && !theDefault) {
      x = mx;
      y = my;
      if ( dist( oX, oY, x, y ) < aSize*0.3f ) {
        x = oX;
        y = oY;
      }
    }

    // Back to initial location
    if ( othersLocked && !locked ) {
      lifted = false;
      x = initX;
      y = initY;
    }

    // The Release
    if ( supReleased ) {
      locked = false;
    }
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "aliswardrobe_tests20" });
  }
}
