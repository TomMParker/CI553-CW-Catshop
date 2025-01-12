package clients.cashier;


import catalogue.Product;

/**
 * The Cashier Controller
 * @author M A Smith (c) June 2014
 */

public class CashierController
{
  private CashierModel model = null;
  private CashierView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CashierController( CashierModel model, CashierView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
//  public void doCheck( String pn )
//  {
//    model.doCheck(pn);
//  }
  public void doCheck( String pn, int amount )
  {
    model.doCheck(pn, amount);
  }


   /**
   * Buy interaction from view
   */
  public void doBuy(String productNum, int userInput)
  {
    model.doBuy(productNum, userInput);
  }
  
   /**
   * Bought interaction from view
   */
  public void doBought()
  {
    model.doBought();
  }}

  /*public void doEdit(String productNum, int userInput) {
    model.doEdit(productNum, userInput);
  }
}*/
