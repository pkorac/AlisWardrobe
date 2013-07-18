
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

  void display() {

    if ( tryMode ) {
      rectMode(CENTER);
      noFill();
      stroke(0, 20);
      rect( x, y, outfitSize*scaling*1.2, outfitSize*scaling*1.2 );
    } 
    else {
      noStroke();
      fill( 0, 20 );
      shapeMode(CENTER);
      shape( baseLayer, x, y, outfitSize*scaling, outfitSize*scaling );
      rectMode(CENTER);
      noFill();
      stroke(0, 20);
      rect( x, y, outfitSize*scaling*1.2, outfitSize*scaling*1.2 );
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
  boolean hovered() {
    if ( mx > x - 0.5*outfitSize && mx < x + 0.5*outfitSize && 
      my > y - 0.5*outfitSize && my < y + 0.5*outfitSize ) {
      return true;
    } 
    else {
      return false;
    }
  }


  // ISLOCKED CHECKER OUTFIT ////////////////////
  boolean isLocked() {
    return locked;
  }


  // CLICK AND DRAG OUTFIT ////////////////////
  void clickAndDrag() {

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

