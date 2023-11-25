package catalogue;

import java.io.Serializable;
import java.util.*;

/**
 * A collection of products from the CatShop.
 *  used to record the products that are to be/
 *   wished to be purchased.
 * @author  Mike Smith University of Brighton
 * @version 2.2
 *
 */
public class Basket extends ArrayList<Product> implements Serializable
{
  private static final long serialVersionUID = 1;
  private int    theOrderNum = 0;          // Order number

  /**
   * Constructor for a basket which is
   *  used to represent a customer order/ wish list
   */
  public Basket()
  {
    theOrderNum  = 0;
  }
  
  /**
   * Set the customers unique order number
   * Valid order Numbers 1 .. N
   * @param anOrderNum A unique order number
   */
  public void setOrderNum( int anOrderNum )
  {
    theOrderNum = anOrderNum;
  }

  /**
   * Returns the customers unique order number
   * @return the customers order number
   */
  public int getOrderNum()
  {
    return theOrderNum;
  }
  
  /**
   * Add a product to the Basket.
   * Product is appended to the end of the existing products
   * in the basket.
   * @param pr A product to be added to the basket
   * @return true if successfully adds the product
   */
  // Will be in the Java doc for Basket
  //@Override (not required as method is overloaded)
  public boolean add( Product pr, int userInput )
  {
    if (productInBasket(pr)){
      editQuantity(pr, userInput);
      return true;
  }
    return super.add( pr );     // if product not already in basket add it
  }

  /**
   * Checks if product in basket
   * @param pr is the product to be checked
   * @return true if product in basket
   */

  private boolean productInBasket(Product pr){
    for(Product productInBasket: this){
      if(productInBasket.getProductNum().equals(pr.getProductNum())){
        return true;
      }
    }
    return false;
  }

  /**
   * @param pr is the product to be searched
   * @return quantity of selected product in basket
   */
  public int getQuantityInBasket(Product pr) {
    for (Product productInBasket : this) { //loops through basket
      if (productInBasket.getProductNum().equals(pr.getProductNum())) { //checks if product is in basket
        return productInBasket.getQuantity(); // returns quantity in basket
      }
    }
    return 0; // return 0 if product not found in basket
  }

  /**
   * removes selected product
   * @param pr is the product to be searched
   */
  public void removeProduct(Product pr){
    this.removeIf(productInBasket -> productInBasket.getProductNum().equals(pr.getProductNum()));
  }

  /**
   * adds quantity selected of product
   * @param pr is the product to be added
   * @param userInput is the new quantity
   */

  public void editQuantity(Product pr, int userInput){
    if (userInput <= 0){ //if new inputted quantity is 0, remove product from basket
      removeProduct(pr);
    } else {
      for(Product productInBasket : this){ //loop through basket
        if(productInBasket.getProductNum().equals(pr.getProductNum())){ // if product is in basket
          productInBasket.setQuantity(userInput);  //set quantity in basket to = userInput
          return;
        }
      }
      pr.setQuantity(userInput);
      this.add(pr);
    }
  }

 /* public void addQuantity(Product pr, int userInput) {
    for (Product productInBasket : this) {
      if (productInBasket.getProductNum().equals(pr.getProductNum())) {
        productInBasket.setQuantity(userInput);
        break;
      }
    }
  }*/



  /**
   * Returns a description of the products in the basket suitable for printing.
   * @return a string description of the basket products
   */
  public String getDetails()
  {
    Locale uk = Locale.UK;
    StringBuilder sb = new StringBuilder(256);
    Formatter     fr = new Formatter(sb, uk);
    String csign = (Currency.getInstance( uk )).getSymbol();
    double total = 0.00;
    if ( theOrderNum != 0 )
      fr.format( "Order number: %03d\n", theOrderNum );
      
    if ( this.size() > 0 )
    {
      for ( Product pr: this )
      {
        int number = pr.getQuantity();
        fr.format("%-7s",       pr.getProductNum() );
        fr.format("%-14.14s ",  pr.getDescription() );
        fr.format("(%3d) ",     number );
        fr.format("%s%7.2f",    csign, pr.getPrice() * number );
        fr.format("\n");
        total += pr.getPrice() * number;
      }
      fr.format("----------------------------\n");
      fr.format("Total                       ");
      fr.format("%s%7.2f\n",    csign, total );
      fr.close();
    }
    return sb.toString();
  }
}
