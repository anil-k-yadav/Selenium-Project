/**
 * This class is used to define all application related methods
 * @author anil
 *
 */
public class FunctionLib {
	
	/**
	 * This method is used to login into the application
	 * based on parameters available in properties file.
	 * All possible actions required for login are listed here
	 */
	public void login()
	{
	
		Lib lib=new Lib();
		String Browser=lib.getProperty("Browser");
		String UserName =lib.getProperty("UserName");
		String Password=lib.getProperty("Password");
		String mainURL=lib.getProperty("mainURL");
		lib.Method_WebBrowserOpen(Browser,mainURL);
		lib.Method_waitForPageLoad();
		lib.Method_SetText("Login.Login_GovtEmailID_Textbox", UserName);
		lib.Method_SetText("Login.Login_Password_Textbox", Password);
		lib.Method_ObjectClick("Login.Login_Login_Button");
	}

}
