import java.util.*;

/*
BTree
Cache
Searchable database
*/
public class Main {
    public static void main(String args[]) {
        BTree testTree = new BTree(5);

        testTree.Insert("Idk dude");
        if(testTree.Contains("Idk dude")) {
            System.out.println("YAY!");
        } else {
            System.out.println("fuck.");
        }
    }
}
