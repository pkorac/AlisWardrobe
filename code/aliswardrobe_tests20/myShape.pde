
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
  void display( float dx, float dy, float oX, float oY ) {
    
    extraLifting();

    if ( tryMode ) {
      if ( lifted && !theDefault ) {
        x -= dx;
        y -= dy;
        fill(c);
        stroke(0, 200);
        strokeCap(ROUND);
        strokeWeight(0.5);
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
        strokeWeight(0.5);
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
  boolean hovered() {
    if ( !isLifted() ) {
      if ( mx > x - 0.5*sSize && mx < x + 0.5*sSize && 
        my > y - 0.5*sSize && my < y + 0.5*sSize ) {
        return true;
      } 
      else {
        return false;
      }
    } 
    else {
      if ( mx > x - 0.20*aSize*scaling && mx < x + 0.20*aSize*scaling && 
        my > y - 0.5*aSize*scaling && my < y + 0.5*aSize*scaling && !theDefault) {
        return true;
      } 
      else {
        return false;
      }
    }
  }



  // LIFTED ////////////////////////////////
  boolean isDefault() {
    return theDefault;
  }

  // LIFTED ////////////////////////////////
  boolean isLifted() {
    return lifted;
  }


  // ISLOCKED CHECKER ////////////////////
  boolean isLocked() {
    return locked;
  }

  void extraLifting(){
   if ( !lifted && !locked ){
    x = initX;
    y = initY;
   } 
  }

  // CLICK AND DRAG ////////////////////
  void clickAndDrag( float oX, float oY ) {

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
      if ( dist( oX, oY, x, y ) < aSize*0.3 ) {
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

