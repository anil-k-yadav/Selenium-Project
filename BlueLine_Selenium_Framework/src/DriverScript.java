import java.io.File;
import java.io.FilePermission;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import jxl.*;

/**
 * This is the main driver class which reads suite file and acts accordingly
 * @author anil
 */
public class DriverScript {

	public static String ResultFolderPath = null;
	public static String ConsolidateResultFile = null;
	public static String ConsolidatePath = null;

	public static void main(String[] args)
	{
		String sSuiteName;
		String sTestCaseFilePath;
		int iSuitRowCount=0;
		int iTestCaseRowcount=0;
		String sTestCaseName;
		String sExecuteStatus;
		String sCompleteSuitePath;
		String sProjectLocation;
		String sPage;
		String sObject;
		String sMethodName;
		String sTempData;
		String sData;
		String[] saMethod;
		Method method = null;
		try
		{
			DriverScript oDriverScript = new DriverScript();
			Lib oLib=new Lib();
			oLib.createResultFolder();
			oLib.setProperty("PassCount","0");
			oLib.setProperty("FailCount","0");
			sSuiteName=oLib.getProperty("SuiteName");
			sProjectLocation=oLib.Method_GetProjectLocation();
			sCompleteSuitePath=sProjectLocation + File.separator + "Suite" + File.separator + sSuiteName+".xls";
			Workbook wSuitWorkbook = Workbook.getWorkbook(new File(sCompleteSuitePath));
			Sheet shSuitSheet = wSuitWorkbook.getSheet(0);
			iSuitRowCount=shSuitSheet.getRows();
			for (int suitRowCounter=1;suitRowCounter<iSuitRowCount;suitRowCounter++)
			{
				Cell cTestCaseName = shSuitSheet.getCell(ScriptConstants.SUIT_TESTCASE_COL_NUM,suitRowCounter);
				sTestCaseName = cTestCaseName.getContents();

				Cell cExecuteStatus = shSuitSheet.getCell(ScriptConstants.SUIT_EXECUTIONSTATUS_COL_NUM,suitRowCounter); 
				sExecuteStatus = cExecuteStatus.getContents();
				if(sExecuteStatus.equalsIgnoreCase("Yes"))
				{
					oLib.setProperty("TestCaseName", sTestCaseName);
					oLib.OpenFile();
					sTestCaseFilePath=sProjectLocation + File.separator + "Test Case" + File.separator + sTestCaseName+".xls";
					Workbook wTestCaseWorkbook = Workbook.getWorkbook(new File(sTestCaseFilePath));
					Sheet shTestCaseSheet = wTestCaseWorkbook.getSheet(0);
					iTestCaseRowcount=shTestCaseSheet.getRows();
					for(int iTestCaseCounter=1;iTestCaseCounter<iTestCaseRowcount;iTestCaseCounter++)
					{
						try{
							Cell cPage = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_PAGE_COL_NUM,iTestCaseCounter); 
							sPage = cPage.getContents();
							Cell cObject = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_OBJECT_COL_NUM,iTestCaseCounter); 
							sObject = cObject.getContents();
							Cell cMethodName = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_METHOD_COL_NUM,iTestCaseCounter); 
							sMethodName = cMethodName.getContents();
							Cell cData = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_DATA_COL_NUM,iTestCaseCounter); 
							sTempData = cData.getContents();
							saMethod=sMethodName.split("\\.");
							Class classInstance = Class.forName(saMethod[0]);
							Object obj = classInstance.newInstance();
							if(sObject.equalsIgnoreCase("Null") && sTempData.equalsIgnoreCase("Null") )
							{
								method = classInstance.getMethod(saMethod[1],null);
								//invoke method!!
								method.invoke(obj);
							}
							else if (!sObject.equalsIgnoreCase("Null") && sTempData.equalsIgnoreCase("Null"))
							{
								method = classInstance.getMethod(saMethod[1],new Class[] {String.class});
								//Arguments to be passed into method
								sObject=sPage+"."+sObject;
								Object[] arglist = new Object[]{sObject};
								//invoke method!!
								method.invoke(obj,arglist);
							}
							else
							{
								method = classInstance.getMethod(saMethod[1],new Class[] {String.class,String.class});
								//Arguments to be passed into method
								sObject=sPage+"."+sObject;
								sData= oDriverScript.ExtractData(sTempData);
								Object[] arglist = new Object[]{sObject,sData};
								//invoke method!!
								method.invoke(obj,arglist);
							}
						}catch(Exception e)
						{
							oLib.repoter("Fail", "NA", "NA", "Some Exception has coccured in DriverScript ::: "+ e.toString());
						}
					}
					oLib.Footer();
				}
			}
			oLib.writeConsolidateResult();
		}catch (Exception e) 
		{
			e.printStackTrace();
			Lib.driver.quit();
		}
	}

	/**
	 * This method is used by main function to extract data using some internal functions at execution time
	 * @param sData
	 * @return data
	 */
	protected String ExtractData(String sData){
		//"CALC||class||Function||Input"
		//In case of function does not require any input, pass below string
		//"CALC||class||Function||NoInput"
		
		String data=null;
		String calculateKey=null;
		String calaulateMainFunction=null;
		String calaulateSubFunction=null;
		String calculateInputParameters=null;
		//The StringTokenizer method is used to break the data passed in this function into peaces whereever there are tokens. The breakByTokenPipe is name given for breaking the string having ||
		if (sData.contains("CALC||")){
			StringTokenizer breakByTokenPipe = new StringTokenizer(sData, ScriptConstants.TOKEN_PIPE);

			//This While loop would break the data passed into small strings based on the occerance of the token ||
			while (breakByTokenPipe.hasMoreTokens()) {
				calculateKey = breakByTokenPipe.nextToken();
				calaulateMainFunction = breakByTokenPipe.nextToken();
				calaulateSubFunction = breakByTokenPipe.nextToken();
				calculateInputParameters = breakByTokenPipe.nextToken();
				if (calculateKey.equals("CALC")){
					data =  CentralDataProcessing(calaulateMainFunction,calaulateSubFunction, calculateInputParameters);	        	  
				}
			}
		}
		//default data
		else{			
			data=sData;
		}

		return data;
	}

	/**
	 * This method is used to invoke methods to extract data at execution time
	 * @param calaulateMainFunction
	 * @param calaulateSubFunction
	 * @param calculateInputParameters
	 * @return outputOfMethod
	 */
	protected String CentralDataProcessing(String calaulateMainFunction,String calaulateSubFunction, String calculateInputParameters){

		Lib oTempLib = new Lib();
		String value=null;
		try{
			//Loading the class from the name
			Class cClassInstance = Class.forName(calaulateMainFunction);
			//Making an instance of this freshly loaded class of BHARTI Screen Objects
			Object oClassObject = cClassInstance.newInstance();
			Method method = null;
			if(calculateInputParameters.equalsIgnoreCase("NoInput"))
			{
				method = cClassInstance.getMethod(calaulateSubFunction,null);
				//invoke method!!
				method.invoke(oClassObject);
			}
			else
			{
				method = cClassInstance.getMethod(calaulateSubFunction,new Class[] {String.class});
				//Arguments to be passed into method
				Object[] arglist = new Object[]{calculateInputParameters};
				//invoke method!!
				method.invoke(oClassObject,arglist);
			}
		}catch(Exception e)
		{
			oTempLib.repoter("Fail", "NA", "NA", "Some Exception has coccured in data extraction.Please check test case ::: "+ e.toString());
		}
		return value;
	}
}
