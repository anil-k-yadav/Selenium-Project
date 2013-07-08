
import com.opera.core.systems.OperaDriver;
import jxl.*;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.*;

//import java.sql.*;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;


/**
 * This class contains all generic methods which can be used across applications/environments
 * @author anil
 *
 */
public class Lib 
{
	public static BufferedWriter wr;
	public static String newdetresultsfile = null;
	public static String newdetresultsfile1 = null;
	public static BufferedWriter wr1;
	public static String loc;
	public static String locURL;
	public static String locIndex;
	public static String flownamevar;
	public static String flowPageLoadNamevar;
	public static WebDriver driver;
	public String message=" ";
	int int_Pass  = 0;
	int int_Fail = 0;
	int testcasetotal = 0;
	int SuccessRate = 0;
	String FailRate = "";
	int Passwidth = 0;
	String Failwidth = "";	


	//	*****************************************************************************************************************************
	//	This section is for application related methods
	//	*****************************************************************************************************************************

	/**
	 * This method is used to open specified browser with url
	 * @param browser
	 * @param url
	 */
	public void Method_WebBrowserOpen(String browser,String url)
	{
		String ret_mess;
		String opvar;
		try
		{
			if(browser.equalsIgnoreCase("INTERNET EXPLORER"))
			{
				System.setProperty("webdriver.ie.driver", Method_GetProjectLocation()+"\\JARs\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}
			else if(browser.equalsIgnoreCase("MOZILLA FIREFOX"))
			{
				driver = new FirefoxDriver();
			}
			else if(browser.equalsIgnoreCase("OPERA"))
			{
				driver = new OperaDriver();
			}

			else if(browser.equalsIgnoreCase("CHROME"))
			{
				System.setProperty("webdriver.chrome.driver", Method_GetProjectLocation()+"\\JARs\\chromedriver.exe");
				driver = new ChromeDriver();
			}
			else if(browser.equalsIgnoreCase("SAFARI"))
			{
				driver = new SafariDriver();	
			}
			driver.get(url);
			driver.navigate().to(url);
			driver.manage().window().maximize();
			opvar="Pass";
			ret_mess=browser+" successfully opened with URL "+url;
		}
		catch(Exception e)
		{
			ret_mess="Method_WebBrowserOpen1:::Some exception has occured:::" +e.toString();
			opvar="fail";
		}
		repoter(opvar, "NA", "NA", ret_mess);
	}

	public void Method_WebBrowserClose()
	{
		String ret_mess;
		String opvar;
		//host=hostName;
		//port=portNum;
		try
		{

			Lib.driver.quit();
			opvar="Pass";
			ret_mess="Successfully closed the URL ";

		}
		catch(Exception e)
		{
			ret_mess=e.toString();
			opvar="fail";
		}
		repoter(opvar, "NA", "NA", ret_mess);
	}

	/**
	 * This method is used to wait until page loads
	 */
	public void Method_waitForPageLoad()
	{
		String opvar="Pass";
		String message="";

		try
		{
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			opvar="Pass";
			message ="waited for page to load";
		}
		catch (Exception e) 
		{
			opvar="Fail";
			message ="Method_waitForPageLoad:::Some exception has occured:::"+e.toString();
		}
		repoter(opvar, "NA", "NA", message);
	}

	/**
	 * This method is used to click on an object
	 * @param sObjlogicalname
	 */
	public void Method_ObjectClick(String sObjlogicalname)
	{
		String opvar;

		WebElement element = getWebElement(sObjlogicalname);
		String message;	
		try
		{
			element.click();
			opvar="Pass";
			message = sObjlogicalname +" is clicked sucessfully";
		}
		catch (Exception e)
		{
			opvar="fail";
			message = sObjlogicalname +" was not clicked:::Some exception has occured"+ e.getMessage();     
		}
		repoter(opvar, "NA", "NA", message);
	}

	/**
	 * This method is used to select an option from drop-down object
	 * @param objectLogicalName
	 * @param sData
	 */
	public void Method_SelectDropDown(String objectLogicalName, String sData)
	{
		String opvar="fail";
		String message=null;
		boolean bOptionPresent = false;

		WebElement element = getWebElement(objectLogicalName);

		try
		{
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if (sData.equals(option.getText())) {
					option.click();
					opvar="Pass";
					bOptionPresent=true;
					message="Option value "+sData+" successfully selected from "+objectLogicalName;
					break;
				}
			}
			if(!bOptionPresent){
				opvar="Fail";
				message="Option value "+sData+" is not an option to select in "+objectLogicalName+ "object";
			}
		}
		catch (Exception e)
		{
			opvar="fail";
			message="Option value "+sData+" could not be selected from "+objectLogicalName+" :::"+e.toString();
		}
		repoter(opvar, "NA","NA", message);
	}

	/**
	 * This method is used to click on a checkbox 
	 * @param objectLogicalName
	 */
	public void Method_ClickCheckBox(String objectLogicalName)
	{
		String opvar;
		String message;
		WebElement element = getWebElement(objectLogicalName);
		try
		{
			element.click();
			opvar="PASS";
			message="Check box is clicked "+objectLogicalName;
		}
		catch (Exception e)
		{
			opvar="FAIL";
			message = "Method_selectCheckBox:::Some exception has occured:::"+e.toString();
		}
		repoter(opvar, "NA","NA", message);
	}

	/**
	 * This method is used to set text in a text box
	 * @param objlogicalname
	 * @param sData
	 */
	public void Method_SetText(String objlogicalname,String sData)
	{
		String opvar;
		String message;

		WebElement element = getWebElement(objlogicalname);
		try
		{
			element.sendKeys(sData);
			opvar="PASS";
			message =  "Successfully set "+sData+ " in "+objlogicalname;
		}
		catch (Exception e)
		{
			opvar="FAIL";
			message = "Method_SetText:::Some exception has occured:::" +  e.toString(); 
		}
		repoter(opvar, "NA", "NA", message);
	}

	/**
	 * 
	 * @param objlogicalname
	 * @param sData
	 * @return
	 */

	public boolean Method_verifyText (String objlogicalname,String sData){
		boolean bStatus = false;
		String opvar="FAIL";
		String message=null;
		String sActualText=null;

		WebElement element = getWebElement(objlogicalname);
		try
		{
			sActualText = element.getText();
			if(sActualText.equals(sData)){
				opvar="PASS";
				message =  "Text in "+objlogicalname +" is same as expected.";
			}else{
				opvar="FAIL";
				message =  "Text in "+objlogicalname +" is different from expected.";
			}
		}
		catch (Exception e)
		{
			opvar="FAIL";
			message = "Method_verifyText:::Some exception has occured:::" +  e.toString(); 
		}
		repoter(opvar, sData, sActualText, message);
		
		return bStatus;
	}
	
	/**
	 * This method is used to retrieve web elements from application 
	 * using element type and property
	 * @param objlogicalname
	 * @return WebElement
	 */
	public WebElement getWebElement(String objlogicalname)
	{
		String [] objectType; 
		String sObjectType;

		objectType=Method_GetObjectfromExcel(objlogicalname);
		sObjectType=objectType[1];
		String sObjectProperty=objectType[0];
		WebElement element=null;
		try
		{
			switch(sObjectType)
			{
			case "css" :  sObjectType = "css";
			element=driver.findElement(By.cssSelector(sObjectProperty));
			break;
			case "id":  sObjectType = "id";
			element=driver.findElement(By.id(sObjectProperty));
			break;
			case "xpath":  sObjectType = "xpath";
			element=driver.findElement(By.xpath(sObjectProperty));
			break;
			case "link": sObjectType = "link";
			element=driver.findElement(By.linkText(sObjectProperty));
			}
		}
		catch (Exception e)
		{
			System.out.println("getWebElement:::Some exception has occured:::"+e.toString());
		}
		return element;
	}

	
	//	*****************************************************************************************************************************
	//	This section is for Reporting related methods
	//	*****************************************************************************************************************************

	/**
	 * This method is used to create result file
	 */
	public void OpenFile() 
	{
		DateFormat timeFormat = new SimpleDateFormat("ddMMYYYY HHmmss");
		Calendar cal = Calendar.getInstance();
		String StartTime =timeFormat.format(cal.getTime());
		setProperty("StartTime", StartTime);
		setProperty("TeststepCount","0");
		setProperty("PassCount","0");
		setProperty("FailCount","0");
		String sTestCaseName = getProperty("TestCaseName");
		String buildName = getProperty("BuildName");
		String resultDirectory = DriverScript.ResultFolderPath;
		(new File(resultDirectory)).mkdir();
		String imageDirectory = resultDirectory+"\\Image";
		(new File(imageDirectory)).mkdir();
		try
		{ 
			//Start try
			String resultPath = resultDirectory + "\\"+"Result_"+ sTestCaseName+".html";
			FileWriter fstream = new FileWriter(resultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write ("<html>");
			resultFile.write("<title> Test Script Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<h2 align='center'>"+ sTestCaseName+"_"+ buildName +"_"+StartTime+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>TestStepID</font></b></td>");
			resultFile.write("<td width='52%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Message</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Expected Result</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Actual Result</font></b></td>");
			resultFile.write("<td width='28%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='20%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>SnapShots</font></b></td>");
			resultFile.write("</tr>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}// end the function openfile

	/**
	 * This method is used to report status of each method into result file
	 * @param strSatus
	 * @param strExpected
	 * @param strActual
	 * @param strMessage
	 */
	public void repoter(String strSatus,String strExpected,String strActual, String strMessage)
	{
		String buildName = getProperty("BuildName");
		String imageDirectory = DriverScript.ResultFolderPath +"\\Image";
		String testCaseName = getProperty("TestCaseName");
		String StartTime = getProperty("StartTime");
		String TeststepCount = getProperty("TeststepCount");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		String snapshotpath = "";

		try
		// start try..
		{
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle rectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(rectangle);
			String imagepath =imageDirectory + "\\"+ testCaseName+"_"+buildName+"_"+StartTime+"_"+TeststepCount + ".jpg";
			File file = new File(imagepath);
			ImageIO.write(image, "jpg", file);
			//			snapshotpath = "<a href=\"" + imagepath + "\">" + "Snapshot</a>";
			snapshotpath = "<a href=\"" + getRelativePath(imagepath) + "\">" + "Snapshot</a>";

			if (strSatus.equalsIgnoreCase("PASS"))// start if
			{
				int_Pass = Integer.parseInt(getProperty("PassCount"));
				addTestStepReport(strMessage, strExpected, strActual, "Pass", snapshotpath);
				int_Pass = int_Pass + 1;
				setProperty("PassCount", Integer.toString(int_Pass));
			}// end if
			else// start else
			{
				int_Fail = Integer.parseInt(getProperty("FailCount"));
				addTestStepReport(strMessage, strExpected, strActual, "Fail", snapshotpath);
				int_Fail = int_Fail + 1;
				setProperty("FailCount", Integer.toString(int_Fail));
			}// end else
		}// end try
		catch (Exception ex)// start catch
		{
			int_Fail = Integer.parseInt(getProperty("FailCount"));
			String failval = ex.toString();
			addTestStepReport(failval, strExpected, "Exception occured", "Fail", snapshotpath);
			int_Fail = int_Fail + 1;
			setProperty("FailCount", Integer.toString(int_Fail));
		}// end catch
	}// end function

	/**
	 * This method is used to result of each test step into report file
	 * @param strMessage
	 * @param strExpectedResult
	 * @param strActualResult
	 * @param strPassFail
	 * @param strSceenshot
	 */
	public void addTestStepReport(String strMessage,String strExpectedResult,String strActualResult, String strPassFail, String strSceenshot)
	{
		String testCaseName = getProperty("TestCaseName");
		int TeststepCount = Integer.parseInt(getProperty("TeststepCount"));

		try
		{
			// start try..
			String resultPath = DriverScript.ResultFolderPath+"\\Result_"+testCaseName+".html";
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("<tr>");
			TeststepCount = TeststepCount + 1;
			setProperty("TeststepCount", Integer.toString(TeststepCount));
			resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + TeststepCount + "</font></td>");
			resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + strMessage + "</font></td>");
			resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + strExpectedResult + "</font></td>");
			resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + strActualResult + "</font></td>");
			if (strPassFail == "Pass")// start if
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#000000' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end if
			else// start else
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end else
			resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + strSceenshot + "</font></td>");
			resultFile.append("</tr>");
			resultFile.close();
			// end try
		}catch (Exception e) 
		{
			e.printStackTrace();
		}// end catch

	}// end function addTestStepReport

	/**
	 * This method is used to create footer in the result file
	 */
	public void Footer()
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HHmmss");
		DateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		DateFormat displayTimeFormat = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		String StartTime = getProperty("StartTime");
		Date dFrom =null;
		Date dTo = null;
		try{//Start try
			dFrom = dateFormat.parse(StartTime);
			dTo = dateFormat.parse(dateFormat.format(cal.getTime()));
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch

		String sDateFrom = displayDateFormat.format(dFrom);//need to pass global variable having script start time
		String sTimeFrom = displayTimeFormat.format(dFrom);
		String sTimeTo = displayTimeFormat.format(dTo);

		float timeDiffSec=(dTo.getTime() - dFrom.getTime())/(1000);
		int timeDiffMin = (int)(dTo.getTime() - dFrom.getTime())/(60*1000);
		String timeDiff = Integer.toString(timeDiffMin)+"."+(int)(timeDiffSec - (timeDiffMin*60));

		int_Pass = Integer.parseInt(getProperty("PassCount"));
		int_Fail = Integer.parseInt(getProperty("FailCount"));
		testcasetotal = int_Pass + int_Fail;
		SuccessRate = (int_Pass * 100 / (testcasetotal));
		FailRate = Integer.toString(100 - SuccessRate);
		Passwidth = (300 * SuccessRate) / 100;
		Failwidth = Integer.toString(300 - Passwidth);
		String resultDirectory = DriverScript.ResultFolderPath;
		String testCaseName = getProperty("TestCaseName");

		try
		{
			//start try
			String resultPath = resultDirectory + "\\"+"Result_"+ testCaseName+".html";
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			//			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Case Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Case Execution Details :</font></b></td></tr>");
			//			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Total Tests Passed</font></b></td><td width='55%' bgcolor='#FFFFDC'><font face='Tahoma' size='2'>" + int_Pass + "</td></tr>");
			//			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Total Tests Failed</font></b></td><td width='55%' bgcolor='#FFFFDC'><font face='Tahoma' size='2'>" + int_Fail + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sDateFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeTo + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + timeDiff + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(testcasetotal);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Result Summary :</font></b></td></tr></table>");

			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=100 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Steps</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=100 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Steps Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Pass) + "</b></td>  <td width= "+Passwidth+" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + SuccessRate + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=100 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Steps Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Fail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + FailRate + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch

	}// end function footer	

	/**
	 * This method is used to write consolidate status of a test suite into HTML file
	 */
	public void writeConsolidateResult()
	{
		String resultFolderPath = null;;
		String status = null;
		String [] testCaseNames;
		File[] fileList; 
		String [] temp;
		String [] tempTestName = null ;
		String resultFile=null;
		String [] sStatus;
		//*setting the value of global property ResultFolderPath created during createFolder fxn call in the local variable
		resultFolderPath = DriverScript.ResultFolderPath;
		//*setting the value of global property ConsolidateResultFile during createFolder fxn call in the local variable
		String consolidateResultFilePath = DriverScript.ConsolidateResultFile;
		OpenFileConsolidate();
		try
		{
			fileList = new File(resultFolderPath).listFiles();
			testCaseNames=new String[(fileList.length)-1];
			int j=0;
			int iPassCount=0;
			int iFailCount=0;
			for (int i = 0; i < fileList.length; i++) {
				if ((fileList[i].getName().endsWith(".html"))) 
				{
					temp = fileList[i].getName().split(".html");
					testCaseNames[j]= temp[0];
					j++;
				}
			}
			for(int i = 0; i < testCaseNames.length; i++) 
			{
				status = null;
				try
				{
					if(testCaseNames[i].isEmpty())
					{
						break;
					}					
				}
				catch (NullPointerException e) 
				{
					break;
				}
				resultFile=resultFolderPath+"\\"+testCaseNames[i]+".html";
				tempTestName = testCaseNames[i].split("Result_");
				//			}		

				sStatus=getOverAllTestCaseStatus(resultFile);
				status=sStatus[0];
				if(status=="FAIL")
				{
					iFailCount=iFailCount+1;	
					setProperty("FailCount",Integer.toString(iFailCount));

				}
				else
				{
					iPassCount=iPassCount+1;	
					setProperty("PassCount",Integer.toString(iPassCount));	
				}
				String sAPIResultFileLink="<a href=\"" + getRelativePath(resultFile) + "\">TestCaseResultFile</a>";
				addTestStepReportConsolidate(tempTestName[1],status, sAPIResultFileLink,sStatus[1]);
			}

			FooterConsolidate();
			setProperty("PassCount","0");
			setProperty("FailCount","0");
			zipResultFolder();
			String zipFileName = DriverScript.ResultFolderPath + ".zip";
			sendEmail(consolidateResultFilePath, zipFileName);

		}
		catch(Exception ex1)
		{
			System.out.println("Unable to consolidate result" + ex1.toString());
		}
	}

	/**
	 * This method is used to create consolidate HTML result file
	 */
	public void OpenFileConsolidate()
	{
		String StartTime =getProperty("StartTime");
		setProperty("TeststepCount","0");
		setProperty("PassCount","0");
		setProperty("FailCount","0");
		String sTestSuiteName = getProperty("SuiteName");
		String buildName = getProperty("BuildName");
		try
		{ 
			//Start try
			String resultPath = DriverScript.ConsolidateResultFile;
			FileWriter fstream = new FileWriter(resultPath);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.write("<html>");
			resultFile.write("<title> Test Script Report </title>");
			resultFile.write("<head></head>");
			resultFile.write("<body>");
			resultFile.write("<font face='Tahoma'size='2'>");
			resultFile.write("<h2 align='center'>"+ sTestSuiteName+"_"+ buildName +"_"+StartTime+"</h2>");
			resultFile.write("<h3 align='right' ><font color='#000000' face='Tahoma' size='3'></font></h3>");
			resultFile.write("<table border='0' width='100%' height='47'>");
			resultFile.write("<tr>");
			resultFile.write("<td width='2%' bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Case Name</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Status</font></b></td>");
			resultFile.write("<td width='30%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Result File</font></b></td>");
			resultFile.write("<td width='50%' bgcolor='#CCCCFF'align='center'><b><font color='#000000' face='Tahoma' size='2'>Test Case Pass Percentage</font></b></td>");
			resultFile.write("</tr>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch
	}// end the function OpenFileConsolidate	

	/**
	 * This method is used to add test step (line) to consolidate result file
	 * @param testCaseName
	 * @param strPassFail
	 * @param ResultFileLink
	 * @param sPassPercent
	 */
	public void addTestStepReportConsolidate(String testCaseName, String strPassFail, String ResultFileLink,String sPassPercent )
	{
		int TeststepCount = Integer.parseInt(getProperty("TeststepCount"));
		try{
			// start try..

			//String resultPath = currentDirectory + "\\" + resultDirectory + "\\"+ testCaseName+"_"+buildName+"_"+StartTime+".html";
			String resultPath =DriverScript.ConsolidateResultFile;
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("<tr>");
			TeststepCount = TeststepCount + 1;
			setProperty("TeststepCount", Integer.toString(TeststepCount));
			resultFile.append("<td width='22%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + testCaseName + "</font></td>");

			if (strPassFail.equalsIgnoreCase("Pass"))// start if
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='#008000' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end if
			else// start else
			{
				resultFile.append("<td width='18%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Tahoma' size='2'>" + strPassFail + "</font></b></td>");
			}// end else
			resultFile.append("<td width='20%' bgcolor='#FFFFDC' valign='top' align='justify' ><font color='#000000' face='Tahoma' size='2'>" + ResultFileLink + "</font></td>");
			resultFile.append("<td width='13%' bgcolor='#FFFFDC' valign='middle' align='center' ><font color='#000000' face='Tahoma' size='2'>" + sPassPercent+"%" + "</font></td>");
			resultFile.append("</tr>");
			resultFile.close();
			// end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}// end catch

	}// end function addTestStepReport

	/**
	 * This method is used by  writeConsolidateResult method to create footer of test suite HTML result file
	 */
	public void FooterConsolidate()
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HHmmss");
		DateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		DateFormat displayTimeFormat = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		//String StartTime = getProperty("StartTime");
		String StartTime = getProperty("SuiteStartTime");
		Date dFrom =null;
		Date dTo = null;
		try{//Start try
			dFrom = dateFormat.parse(StartTime);
			dTo = dateFormat.parse(dateFormat.format(cal.getTime()));
			//end try
		}catch (Exception e) {
			e.printStackTrace();
		}//end catch

		String sDateFrom = displayDateFormat.format(dFrom);//need to pass global variable having script start time
		String sTimeFrom = displayTimeFormat.format(dFrom);
		String sTimeTo = displayTimeFormat.format(dTo);

		float timeDiffSec=(dTo.getTime() - dFrom.getTime())/(1000);
		int timeDiffMin = (int)(dTo.getTime() - dFrom.getTime())/(60*1000);
		String timeDiff = Integer.toString(timeDiffMin)+"."+(int)(timeDiffSec - (timeDiffMin*60));

		int_Pass = Integer.parseInt(getProperty("PassCount"));
		int_Fail = Integer.parseInt(getProperty("FailCount"));
		testcasetotal = int_Pass + int_Fail;
		SuccessRate = (int_Pass * 100 / (testcasetotal));
		FailRate = Integer.toString(100 - SuccessRate);
		Passwidth = (300 * SuccessRate) / 100;
		Failwidth = Integer.toString(300 - Passwidth);

		try
		{
			//start try
			String resultPath = DriverScript.ConsolidateResultFile;
			FileWriter fstream = new FileWriter(resultPath,true);
			BufferedWriter resultFile = new BufferedWriter(fstream);
			resultFile.append("</table>");
			resultFile.append("<hr>");
			resultFile.append("<table border='0' width='50%'>");
			resultFile.append("<tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Execution Details :</font></b></td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On (DD.MM.YYYY)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sDateFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeFrom + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time (HH:MM:SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + sTimeTo + "</td></tr>");
			resultFile.append("<tr><td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time (MM.SS)</font></b></td><td width='55%' bgcolor= '#FFFFDC'><font face='Tahoma' size='2'>" + timeDiff + "</td></tr>");
			resultFile.append("</table>");
			String totaltest =Integer.toString(testcasetotal);
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Test Suite Result Summary :</font></b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test case Executed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + totaltest + "</b></td>  <td width=300 bgcolor='#E7A1B0'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>100%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr>  <td width=130 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75 ><b>Total Test case Passed</b></td> <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Pass) + "</b></td>  <td width= "+Passwidth+" bgcolor='#008000'></td>  <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + SuccessRate + "%</b></td></tr></table>");
			resultFile.append("<table border=0 cellspacing=1 cellpadding=1 ><tr> <td width=130 bgcolor= '#FFFFDC'><FONT   FACE='Tahoma' SIZE=2.75 ><b>Total Test case Failed</b></td>  <td width=10 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>:</b></td>     <td width=35 bgcolor= '#FFFFDC'><FONT  FACE='Tahoma' SIZE=2.75><b>" + Integer.toString(int_Fail) + "</b></td>   <td width= " + Failwidth + " bgcolor='#FF0000'></td>     <td width=20><FONT COLOR='#000000' FACE='Tahoma' SIZE=1><b>" + FailRate + "%</b></td> </tr></table>");
			resultFile.append("</font>");
			resultFile.append("</body>");
			resultFile.append("</html>");
			resultFile.close();
			//end try
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}//end catch

	}// end function footer	

	/**
	 * This method is used to create zip result folder for mailing result.
	 * It uses addFolderToZip internally
	 * @throws IOException
	 */
	public void  zipResultFolder() throws IOException
	{
		//name of zip file to create
		String ResultFolderPath=DriverScript.ResultFolderPath;
		String outFilename = ResultFolderPath + ".zip";


		//create ZipOutputStream object
		ZipOutputStream out=null;
		try {
			out = new ZipOutputStream(new FileOutputStream(outFilename));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}

		//path to the folder to be zipped
		File zipFolder = new File(ResultFolderPath);

		//get path prefix so that the zip file does not contain the whole path
		// eg. if folder to be zipped is /home/lalit/test
		// the zip file when opened will have test folder and not home/lalit/test folder
		int len = zipFolder.getAbsolutePath().lastIndexOf(File.separator);
		String baseName = zipFolder.getAbsolutePath().substring(0,len+1);
		try
		{
			addFolderToZip(zipFolder, out, baseName);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		out.close();
	} 

	/**
	 * This method is used to add files/folder to zip folder
	 * @param folder
	 * @param zip
	 * @param baseName
	 * @throws IOException
	 */
	public void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException 
	{
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, zip, baseName);
			} else {
				String name = file.getAbsolutePath().substring(baseName.length());
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}

	/**
	 * This method is used to send mails to the mail ids configured in Properties.properties file
	 * @param attachment
	 * @param zipAttachment
	 */
	public void sendEmail(String attachment,String zipAttachment)
	{
		final String username = "metacube.blueline@gmail.com";
		final String password = "Metacube123";
		String filename = null;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication  getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});


		try
		{
			MimeMessage message = new MimeMessage(session);
			((MimeMessage) message).setFrom(new InternetAddress("metacube.blueline@gmail.com"));
			InternetAddress [] address=InternetAddress.parse(getProperty("toMail"));


			message.addRecipients(MimeMessage.RecipientType.TO, address);  

			((MimeMessage) message).setSubject("Automation Report");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("Hello Dear,"
					+ "\n Please find the attached automation report!"
					+ "\n NOTE: Please copy paste consolidate report and zip file at same directory and extract zip content into the same directory, so that links in the report work as expected."
					+"\n\n Thanks & Regards"
					+"\n Metacube Automation Team");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if(attachment!="Null")
			{

				messageBodyPart = new MimeBodyPart();
				filename = attachment;
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
				((Part) message).setContent(multipart);
			}      

			if(zipAttachment!="Null")
			{

				messageBodyPart = new MimeBodyPart();
				filename = zipAttachment;
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
			}    
			Transport.send((javax.mail.Message) message);
		}
		catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is used to get the Pass/fail status of a test case along with its pass percentage
	 * @param sTestCaseResultFile
	 * @return an array containing Pass/Fail and Pass %
	 */
	public String []  getOverAllTestCaseStatus (String sTestCaseResultFile)
	{

		String sStatus="PASS";
		String [] line2;
		String [] line3;
		String []sFinalStatus=new String [5];
		int temp=0;
		try
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(sTestCaseResultFile);
			// Get the object of DataInputStream
			DataInputStream dis = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null) 
			{
				temp=strLine.indexOf("Test Case Execution Details :");
				if(strLine.indexOf("Fail")<temp)
				{
					sStatus = "FAIL";
					sFinalStatus[0]=sStatus;
				}
				else
				{
					sStatus = "PASS";
					sFinalStatus[0]=sStatus;

				}			

				String [] sub1;
				line2=strLine.split("Total Steps Passed");

				line3=line2[1].split(":");
				sub1=line3[1].split("%");
				int stotallength=sub1[0].length();
				int lastindex=sub1[0].lastIndexOf(">");
				int size=stotallength-lastindex;
				String overAllPassPerc=sub1[0].substring(lastindex+1, lastindex+size);
				sFinalStatus[1]=overAllPassPerc;
			}


			//Close the input stream
			dis.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.out.println( "Error: " + e.getMessage());
		}
		return sFinalStatus;
	}

	/**
	 * This method is used to convert system directory path into relative path to create links in HTML report
	 * @param sFilePath
	 * @return relativePath
	 */
	public String  getRelativePath (String sFilePath)
	{

		//def sFilePath = filePath.toString();
		String [] tempPathElement;
		String sRelativePath="./";
		String fileRelativePath = null;
		tempPathElement= sFilePath.split("\\\\");
		for(int i=(tempPathElement.length-2);i<(tempPathElement.length);i++)
		{
			sRelativePath=sRelativePath+"\\"+tempPathElement[i];
		}
		fileRelativePath = new File(sRelativePath).toString();
		return fileRelativePath;

	}

	/**
	 * This method is used to create directories required to store test result and sets different result directory parameters
	 */
	public void createResultFolder()
	{	
		String sOpvar;			// * Variable use to Store the final return output
		String sResultFolder;	// * Variable use to store "Results" folder path
		String sTestSuiteName;	// * Variable use to store the suite path name
		String sProjectPath;		// * Variable use to store the project path
		String sOutputFolder;		// * Variable use to store the output folder path
		File oCreateFolder;		// * Object variable use to create the directory
		String sConsolidateResultFileName = null;	// * Variable use to stor the consolidate report excel file path


		try
		{
			sProjectPath = Method_GetProjectLocation();
			// ** creating the "Results Folder"
			sResultFolder = sProjectPath +"\\Results";
			File oCreateResultFolder = new File(sResultFolder);
			oCreateResultFolder.mkdir();

			// get the test suite name
			//sTestSuiteName = testRunner.testCase.testSuite.name
			sTestSuiteName=	getProperty("SuiteName");

			// ** Suite Name reult Folder
			//def sOutputFolder1 = sResultFolder + "\\" + sTestSuiteName
			sOutputFolder = sResultFolder + "\\" + sTestSuiteName;
			oCreateFolder = new File(sOutputFolder);
			oCreateFolder.mkdir();



			// ** creating the "Result<datetime> Folder under Results"
			Date dTodayDate = new Date();
			SimpleDateFormat oDateFormat = new java.text.SimpleDateFormat("ddMMyyyyhhmmss");
			SimpleDateFormat oCurrentDateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");
			String dShortDate = oDateFormat.format(dTodayDate);
			String dCurrentDate = (String) (oCurrentDateFormat).format(dTodayDate);
			String sTempFolder = sOutputFolder + "\\"+ dCurrentDate.toString();
			File oCreateDateFolder =  new File(sOutputFolder+ "\\"+dCurrentDate.toString());
			oCreateDateFolder.mkdir();
			sConsolidateResultFileName = sOutputFolder +  "\\" + dCurrentDate.toString() + "\\ConsolidateReport_" + dShortDate.toString() + ".html";
			sOutputFolder = sTempFolder + "\\" + dShortDate.toString();
			oCreateFolder = new File(sOutputFolder);
			oCreateFolder.mkdir();
			sOpvar = sOutputFolder;

			DriverScript.ResultFolderPath=sOpvar;
			DriverScript.ConsolidateResultFile=sConsolidateResultFileName;
			DriverScript.ConsolidatePath=sTempFolder;
			
			// ** Setting suite start time in properties
			DateFormat timeFormat = new SimpleDateFormat("ddMMYYYY HHmmss");
			String StartTime =timeFormat.format(dTodayDate);
			setProperty("SuiteStartTime", StartTime);

		}
		catch (Exception e)
		{
			sOpvar = e.toString();
		}


	}

	
	//	*****************************************************************************************************************************
	//	This section is for some generic methods used by different methods
	//	*****************************************************************************************************************************

	/**
	 * This method is used to retrieve object property and property type from Object.xls file
	 * @param sObjectLogicalName
	 * @return an array having property value and type
	 */
	public String [] Method_GetObjectfromExcel(String sObjectLogicalName)
	{
		String sObjectProp=null;
		int rowcount=0,count=0;
		String [] sTemp;
		String sSheetName;
		String [] sObjType=new String [2];
		String sObjLogicalName;

		try{
			sTemp=sObjectLogicalName.split("\\.");
			sSheetName=sTemp[0];
			sObjLogicalName=sTemp[1];

			String sProjectLocation=Method_GetProjectLocation();
			String ObjectSheetPath1=sProjectLocation+"\\Object\\Object.xls";
			Workbook workbook = Workbook.getWorkbook(new File(ObjectSheetPath1));
			Sheet sheet = workbook.getSheet(sSheetName);
			rowcount=sheet.getRows();
			for(int i=0; i<rowcount;i++)
			{
				Cell a2 = sheet.getCell(0,i); 
				String rowval = a2.getContents();
				count++;
				if(sObjLogicalName.equalsIgnoreCase(rowval) && count<=rowcount)
				{
					Cell propertyCell = sheet.getCell(ScriptConstants.OBJECT_PROPERTY_COL_NUM,i); 
					sObjType[0] = propertyCell.getContents();
					Cell typeCell = sheet.getCell(ScriptConstants.OBJECT_TYPE_COL_NUM,i); 
					sObjType[1] = typeCell.getContents();
					break;
				}
				else
				{
					sObjectProp="Object Logical name is not matched";
				}
			}
		}catch(Exception e){
			System.out.println("Method_GetObjectfromExcel:::Some Exception has occured:::"+e.toString());
			sObjType[0]=sObjectProp;
		}
		return sObjType;
	}

	/**
	 * This method is used to get project location on the system
	 * @return projectLocation
	 */
	public String Method_GetProjectLocation()
	{

		String currentDirectory;
		currentDirectory = System.getProperty("user.dir");
		return currentDirectory;

	}

	/**
	 * This method is used to get property value from properties file
	 * @param sPropertyName
	 * @return property value
	 */
	public String getProperty(String sPropertyName) 
	{
		String currentDirectory = Method_GetProjectLocation();
		String sProperty = null;
		Properties properties = new Properties();
		InputStream inputFile = null;
		String sConfigLocation = currentDirectory+"\\Properties\\Properties.properties";
		try {//Start try
			inputFile = new FileInputStream(sConfigLocation);
			properties.load(inputFile);
			// Get the value from the properties file
			sProperty = properties.getProperty(sPropertyName);
			inputFile.close();
			//end try
		} catch(IOException e) {
			System.out.println("getProperty:::Some Exception has occured:::" + e.toString());
		}//end catch
		return sProperty;
	}//end function getProperty

	/**
	 * This method is used to set property value into properties file 
	 * @param sPropertyName
	 * @param sPropertyValue
	 */
	public void setProperty(String sPropertyName, String sPropertyValue) 
	{
		String currentDirectory = Method_GetProjectLocation();
		Properties properties = new Properties();
		InputStream inputFile = null;
		OutputStream outputFile = null;
		String sConfigLocation = currentDirectory+"\\Properties\\Properties.properties";
		try {//Start try
			inputFile = new FileInputStream(sConfigLocation);
			properties.load(inputFile);
			outputFile = new FileOutputStream(sConfigLocation);
			// Get the value from the properties file
			properties.setProperty(sPropertyName, sPropertyValue);
			properties.store(outputFile, "");
			inputFile.close();
			outputFile.close();
			//end try
		} catch (IOException e) {
			System.out.println("setProperty:::Some exception has occured:::" + e.toString());
		}//end catch
	}//end function setProperty

	//	*****************************************************************************************************************************


}
