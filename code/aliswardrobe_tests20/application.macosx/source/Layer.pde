
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
  void display( float dx, float dy, float x, float y) {
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

