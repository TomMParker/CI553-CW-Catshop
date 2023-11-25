package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;
import org.apache.commons.lang3.time.DateUtils;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.io.PrintWriter;

import java.util.Date;
import java.text.SimpleDateFormat;
// Format date to append to new file name



/**
 * Implements the Model of the cashier client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CashierModel extends Observable
{
  private enum State { process, checked }

  private State       theState   = State.process;   // Current state
  private Product     theProduct = null;            // Current product
  private Basket      theBasket  = null;            // Bought items

  private String      pn = "";                      // Product being processed

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;

  SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss EE MMM dd");
  SimpleDateFormat sdfForCollection = new SimpleDateFormat("hh:mm");
  int collectionTime = 15;


 /* public void doEdit(String productNum, int userInput ) (removed doEdit method)
  {
    String theAction = "";
    theState  = State.process;                  // State process
    pn  = productNum.trim();                    // Product no.
    int    amount  = userInput;                         //  & quantity
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails(pn);   //  Get details
        if ( pr.getQuantity() >= userInput )       //  In stock?
        {                                       //  T
          theAction =                           //   Display
                  String.format( "%s : %7.2f (%2d) ", //
                          pr.getDescription(),              //    description
                          pr.getPrice(),                    //    price
                          userInput);               //    quantity
          theProduct = pr;                      //   Remember prod.
          theProduct.setQuantity( userInput );     //    & quantity
          theBasket.editQuantity(pr, userInput);
          theStock.addStock(theProduct.getProductNum(), amount);
          theState = State.checked;             //   OK await BUY
        } else {                                //  F
          theAction =                           //   Not in Stock
                  pr.getDescription() +" not in stock";
        }
      } else {                                  // F Stock exists
        theAction =                             //  Unknown
                "Unknown product number " + pn;       //  product no.
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s",
              "CashierModel.doEdit", e.getMessage() );
      theAction = e.getMessage();
    }
    setChanged(); notifyObservers(theAction);
  }*/

  /**
   * Construct the model of the Cashier
   * @param mf The factory to create the connection objects
   */

  public CashierModel(MiddleFactory mf)
  {
    try                                           //
    {
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      DEBUG.error("CashierModel.constructor\n%s", e.getMessage() );
    }
    theState   = State.process;
    // Current state
    //theStock.addStock();
  }

  /**
   * Get the Basket of products
   * @return basket
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum, int userInput )
  {
    String theAction = "";
    theState  = State.process;                  // State process
    pn  = productNum.trim();                    // Product no.
    int    amount  = userInput;                         //  & quantity
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails(pn);   //  Get details
        if ( pr.getQuantity() >= userInput )       //  In stock?
        {                                       //  T
          theAction =                           //   Display
            String.format( "%s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              userInput);               //    quantity
          theProduct = pr;                      //   Remember prod.
          theProduct.setQuantity( userInput );     //    & quantity
          theState = State.checked;             //   OK await BUY
        } else {                                //  F
          theAction =                           //   Not in Stock
            pr.getDescription() +" not in stock";
        }
      } else {                                  // F Stock exists
        theAction =                             //  Unknown
          "Unknown product number " + pn;       //  product no.
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s",
            "CashierModel.doCheck", e.getMessage() );
      theAction = e.getMessage();
    }
    setChanged(); notifyObservers(theAction);
  }


  /**
   * Buy the product
   */
/*  public void doBuy(String productNo, int userInput)
  {
    String theAction = "";
    int    amount  = 1;                         //  & quantity
    try
    {
      if ( theState != State.checked )          // Not checked
      {                                         //  with customer
        theAction = "Check if OK with customer first";
      } else {
        boolean stockBought =                   // Buy
          theStock.buyStock(                    //  however
            theProduct.getProductNum(),         //  may fail
            theProduct.getQuantity() );         //
        if ( stockBought )                      // Stock bought
        {                                       // T
          makeBasketIfReq();                    //  new Basket ?
          theBasket.editQuantity(theProduct, userInput);          //  Add to bought
          theAction = "Purchased " +            //    details
                  theProduct.getDescription();  //
          System.out.println("testing");
        } else {                                // F
          theAction = "!!! Not in stock";       //  Now no stock
        }
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s",
            "CashierModel.doBuy", e.getMessage() );
      theAction = e.getMessage();
    }
    theState = State.process;                   // All Done
    setChanged(); notifyObservers(theAction);
  }*/

    public void doBuy(String productNo, int userInput)
    {
      int amountReturned = 0; // variable to hold the amount of stock to be returned if the quantity in the basket is edited
      String theAction = "";
      int    amount  = 1;                         //  & quantity
      try
      {
        if ( theState != State.checked )          // Not checked
        {                                         //  with customer
          theAction = "Check if OK with customer first";
        } else {
          boolean stockBought =                   // Buy
                  theStock.buyStock(                    //  however
                          theProduct.getProductNum(),         //  may fail
                          theProduct.getQuantity() );         //
          System.out.println("quantity ordered: "+theProduct.getQuantity());
          if ( stockBought )                      // Stock bought
          {                                       // T
            makeBasketIfReq();                    //  new Basket ?
            System.out.println("quantity in basket: " + theBasket.getQuantityInBasket(theProduct));
            amountReturned = theBasket.getQuantityInBasket(theProduct) - userInput;
            System.out.println("amount returned:" + amountReturned);
            if (amountReturned >= 0) {
              theStock.addStock(theProduct.getProductNum(), amountReturned + theProduct.getQuantity()); // add the quantity of product ordered as this will be taken from stock again when the buy goes through

            }
            theBasket.editQuantity(theProduct, userInput);          //  Add to bought

            theAction = "Purchased " +            //    details
                    theProduct.getDescription();  //


          } else {                                // F
            theAction = "!!! Not in stock";       //  Now no stock
          }
        }
      } catch( StockException e )
      {
        DEBUG.error( "%s\n%s",
                "CashierModel.doBuy", e.getMessage() );
        theAction = e.getMessage();
      }
      theState = State.process;                   // All Done
      setChanged(); notifyObservers(theAction);
    }

  /**
   * Customer pays for the contents of the basket
   */
  public void doBought()
  {
    String theAction = "";
    int    amount  = 1;                       //  & quantity
    try
    {
      if ( theBasket != null &&
           theBasket.size() >= 1 )            // items > 1
      {                                       // T
        theOrder.newOrder( theBasket );       //  Process order
        System.out.println("testing2");
        doReceipt();

        theBasket = null;                     //  reset
      }                                       //
      theAction = "Next customer";            // New Customer
      theState = State.process;               // All Done
      theBasket = null;
    } catch( OrderException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCancel", e.getMessage() );
      theAction = e.getMessage();
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
      theBasket = null;
    setChanged(); notifyObservers(theAction); // Notify
  }


/*public void doEdit(Product productNum){
  theBasket.removeQuantity(productNum);
}*/

  /**
   * Grabs basket details and writes to receipt.txt file
   * Text message to customer advising of collection
   * time.
   */
  public void doReceipt() throws FileNotFoundException {
    Date curDate = new Date();
    Date collectionDate = DateUtils.addMinutes(curDate, collectionTime);
    PrintWriter out = new PrintWriter("receipt.txt");
    out.println(sdf.format(curDate));
    out.println(theBasket.getDetails());
    out.println("Please retain this receipt as proof");
    out.println("of purchase. Show at the collection");
    out.println("desk when requested.");
    out.println("");
    out.println("Your expected pick up time is:");
    out.println(sdfForCollection.format(collectionDate));

    out.close();

    theBasket = null;                     //  reset
  }



  /**
   * ask for update of view callled at start of day
   * or after system reset
   */
  public void askForUpdate()
  {
    setChanged(); notifyObservers("Welcome");
  }
  
  /**
   * make a Basket when required
   */
  private void makeBasketIfReq()
  {
    if ( theBasket == null )
    {
      try
      {
        int uon   = theOrder.uniqueNumber();     // Unique order num.
        theBasket = makeBasket();                //  basket list
        theBasket.setOrderNum( uon );            // Add an order number
      } catch ( OrderException e )
      {
        DEBUG.error( "Comms failure\n" +
                     "CashierModel.makeBasket()\n%s", e.getMessage() );
      }
    }
  }

  /**
   * return an instance of a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
}
  
