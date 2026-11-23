import java.awt.font.NumericShaper;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exercise (Chapter: APIs, JSON, and Files) — reading data from a file into objects.
 *
 * This exercise practises reading data from a file and building a structure of
 * objects to represent it. You will complete two methods:
 * {@link #loadTriangle(String)} and {@link #retrieve(String)}.
 * Edit only this file.
 *
 * A {@code NumberTriangle} is like a tree, but some nodes in the structure have
 * two parents. The structure is shown below. Observe that the parents of e are b
 * and c, whereas d and f each only have one parent. Each row is complete and will
 * never be missing a node, so each row has one more {@code NumberTriangle} object
 * than the row above it.
 *
 * <pre>
 *                  a
 *                b   c
 *              d   e   f
 *            h   i   j   k
 * </pre>
 *
 * The sharing of nodes between rows is the interesting part: if {@code base} refers
 * to the topmost object, then {@code base.left.right == base.right.left} — they are
 * the very same object (aliasing), not two equal copies.
 *
 * This class is minimally defined and is only intended to be constructed using the
 * {@link #loadTriangle(String)} method. There is no code enforcing the structure
 * described above, and you do not have to write any either.
 *
 * Relevant reading: the file-reading section of the "APIs, JSON, and Files" chapter.
 */
public class NumberTriangle {

  private final int root;

  private NumberTriangle left;
  private NumberTriangle right;

  /**
   * Constructs a NumberTriangle with the given value at its root and no children.
   *
   * @param root the value at the root of this NumberTriangle
   */
  public NumberTriangle(int root) {
    this.root = root;
  }

  /**
   * Sets the left child of this NumberTriangle.
   *
   * @param left the NumberTriangle to use as the left child
   */
  public void setLeft(NumberTriangle left) {
    this.left = left;
  }

  /**
   * Sets the right child of this NumberTriangle.
   *
   * @param right the NumberTriangle to use as the right child
   */
  public void setRight(NumberTriangle right) {
    this.right = right;
  }

  /**
   * Returns the value stored at the root of this NumberTriangle.
   *
   * @return the root value
   */
  public int getRoot() {
    return root;
  }

  /**
   * Reports whether this NumberTriangle has no children.
   *
   * @return true if and only if both children are null
   */
  public boolean isLeaf() {
    return right == null && left == null;
  }

  /**
   * Follows path through this NumberTriangle structure ('l' = left; 'r' = right) and
   * returns the root value at the end of the path. An empty string returns the root
   * of this NumberTriangle.
   *
   * You can decide if you want to use a recursive or an iterative approach.
   *
   * You can assume that:
   * <ul>
   *   <li>the length of path is less than the height of this NumberTriangle structure;</li>
   *   <li>each character in the string is either 'l' or 'r'.</li>
   * </ul>
   *
   * @param path the path to follow through this NumberTriangle
   * @return the root value at the location indicated by path
   */
  public int retrieve(String path) {

    // if the string is empty, return the root of this triangle
    if (path.isEmpty()) {
      return this.root;
    }

    // if the current NumberTriangle does not have any children, return the root
    if (this.isLeaf()) {
      return this.root;
    }

    // otherwise, the path is not empty, so we can start to walk it
    char direction = path.charAt(0);
    if (direction == 'l'){
      if (this.left == null) {
        return this.root; //this means that there is no left child for us to move to, so return this value
      }
      // otherwise, recursively call this feature
      return this.left.retrieve(path.substring(1));
    }

    // the path is not empty and does not say to go left, so it must say to go right
    if (this.right == null){
      return this.root; //same logic as above
    }
    return this.right.retrieve(path.substring(1));
  }

  /**
   * Reads in the NumberTriangle structure from a file.
   *
   * You may assume that it is a valid format with a height of at least 1, so there
   * is at least one line with a number on it to start the file. Numbers on a line
   * are separated by spaces. See {@code input_tree.txt} for an example.
   *
   * The first row is the root of the NumberTriangle (call it 0).
   * The second line contains the two children of the root (call them 1L and 1R).
   * The third line contains the three numbers corresponding to:
   * <ul>
   *   <li>the left child of 1L, call it 2LL;</li>
   *   <li>the right child of 1L, call it 2LR;</li>
   *   <li>the left child of 1R, call it 2RL;</li>
   *   <li>the right child of 1R, call it 2RR.</li>
   * </ul>
   * NOTE: 2RL and 2LR must refer to the SAME underlying NumberTriangle object.
   * That is, if variable {@code base} refers to the object at the top of the
   * triangle, then {@code base.left.right == base.right.left}.
   *
   * <pre>
   *              0
   *            /   \
   *          1L    1R
   *         /  \  /  \
   *       2LL  2LR*  2RR    *2LR == 2RL
   * </pre>
   *
   * Hint 0: Start by making a plan and scaffolding what you plan to do. If you are
   *         still finding it hard to "think in Java", write some comments describing
   *         what you want to do first.
   *
   * Hint 1: Think about what you need to keep track of on each iteration of the loop
   *         and make appropriate variables to store those things. Working through
   *         {@code little_tree.txt} by hand should help you develop the general logic.
   *
   * Hint 2: Related to Hint 1, think about how to connect NumberTriangle objects
   *         between adjacent rows in the structure. Remember that the row you just
   *         built becomes the children of the row before it.
   *
   * Hint 3: If you are still stuck, look for a subproblem that you can solve, then
   *         design the logic of your solution around a helper method that you
   *         implement separately. Decomposing a problem this way makes it much more
   *         manageable.
   *
   * @param fname the file to load the NumberTriangle structure from
   * @return the topmost NumberTriangle object in the structure read from the file
   * @throws IOException may naturally occur if an issue reading the file occurs
   */
  public static NumberTriangle loadTriangle(String fname) throws IOException {
    // Open the file and get a BufferedReader object, whose methods are convenient
    // to work with when reading a file line by line. See the file-reading section
    // of the "APIs, JSON, and Files" chapter.
    BufferedReader br = Files.newBufferedReader(Path.of(fname));

    // Keeping track of the last row
    NumberTriangle[] prev_row = null;


    // We need to return the top of the NumberTriangle, so here is a variable for it.
    NumberTriangle top = null;

    String line = br.readLine();
    while (line != null) {

      // Remove this line when you are done; it is here so that the starter code
      // prints the contents of the file when you run it.
      System.out.println(line);

      // TODO: process the line. Splitting it on spaces gives you the numbers in
      //       this row; make a NumberTriangle for each one, then wire this row up
      //       as the children of the previous row. Remember the aliasing: the
      //       right child of one node is the left child of the next node over.
      // First, I am going to check if the previous row is empty, if that is the case this is the top
      if (prev_row == null){
        top = new NumberTriangle(Integer.parseInt(line));
        prev_row = new NumberTriangle[] {top};

      }
      else{
        String[] numbers_as_string = line.split(" ");

        // Making a current row for the number triangle
        NumberTriangle[] current_row = new NumberTriangle[numbers_as_string.length];

        // Now I am adding a current row
        for (int i = 0; i < numbers_as_string.length; i += 1){
          int temp_root = Integer.parseInt(numbers_as_string[i]); //Convert the string of the integer to an int
          NumberTriangle childless = new NumberTriangle(temp_root);
          current_row[i] = childless;
        }

        // Connect the rows
        for (int i = 0; i < prev_row.length; i += 1) {
          prev_row[i].setLeft(current_row[i]);
          prev_row[i].setRight(current_row[i + 1]);
        }

        prev_row = current_row;

      }


      // read the next line
      line = br.readLine();
    }
    br.close();
    return top;
  }

  /**
   * Loads the example triangle and prints its root value.
   *
   * @param args unused
   * @throws IOException if the file cannot be read
   */
  public static void main(String[] args) throws IOException {
    NumberTriangle mt = NumberTriangle.loadTriangle("input_tree.txt");
    System.out.println("Root of the triangle: " + mt.getRoot());
  }
}
