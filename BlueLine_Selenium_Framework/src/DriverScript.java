import java.io.File;
import java.lang.reflect.Method;
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
		String sData;
		String[] saMethod;
		Method method = null;
		try
		{

			Lib oLib=new Lib();
			oLib.createResultFolder();
			oLib.setProperty("PassCount","0");
			oLib.setProperty("FailCount","0");
			sSuiteName=oLib.getProperty("SuiteName");
			sProjectLocation=oLib.Method_GetProjectLocation();
			sCompleteSuitePath=sProjectLocation+"\\Suite\\"+sSuiteName+".xls";
			Workbook wSuitWorkbook = Workbook.getWorkbook(new File(sCompleteSuitePath));
			Sheet shSuitSheet = wSuitWorkbook.getSheet(0);
			iSuitRowCount=shSuitSheet.getRows();
			for (int suitRowCounter=1;suitRowCounter<iSuitRowCount;suitRowCounter++)
			{
				//Cell a2 = sheet.getCell(1,i); 
				Cell cTestCaseName = shSuitSheet.getCell(ScriptConstants.SUIT_TESTCASE_COL_NUM,suitRowCounter);
				sTestCaseName = cTestCaseName.getContents();

				Cell cExecuteStatus = shSuitSheet.getCell(ScriptConstants.SUIT_EXECUTIONSTATUS_COL_NUM,suitRowCounter); 
				sExecuteStatus = cExecuteStatus.getContents();
				//System.out.println(sExecuteStatus.trim());
				if(sExecuteStatus.equalsIgnoreCase("Yes"))
				{
					oLib.setProperty("TestCaseName", sTestCaseName);
					oLib.OpenFile();
					sTestCaseFilePath=sProjectLocation+"\\Test Case\\"+sTestCaseName+".xls";
					Workbook wTestCaseWorkbook = Workbook.getWorkbook(new File(sTestCaseFilePath));
					Sheet shTestCaseSheet = wTestCaseWorkbook.getSheet(0);
					iTestCaseRowcount=shTestCaseSheet.getRows();
					for(int iTestCaseCounter=1;iTestCaseCounter<iTestCaseRowcount;iTestCaseCounter++)
					{
						Cell cPage = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_PAGE_COL_NUM,iTestCaseCounter); 
						sPage = cPage.getContents();
						Cell cObject = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_OBJECT_COL_NUM,iTestCaseCounter); 
						sObject = cObject.getContents();
						Cell cMethodName = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_METHOD_COL_NUM,iTestCaseCounter); 
						sMethodName = cMethodName.getContents();
						Cell cData = shTestCaseSheet.getCell(ScriptConstants.TESTCASE_DATA_COL_NUM,iTestCaseCounter); 
						sData = cData.getContents();
						saMethod=sMethodName.split("\\.");
						Class classInstance = Class.forName(saMethod[0]);
						Object obj = classInstance.newInstance();
						if(sObject.equalsIgnoreCase("Null") && sData.equalsIgnoreCase("Null") )
						{
							try
							{
								method = classInstance.getMethod(saMethod[1],null);
								//invoke method!!
								method.invoke(obj);
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
						}
						else if (!sObject.equalsIgnoreCase("Null") && sData.equalsIgnoreCase("Null"))
						{
							try
							{
								method = classInstance.getMethod(saMethod[1],new Class[] {String.class});
								//Arguments to be passed into method
								sObject=sPage+"."+sObject;
								Object[] arglist = new Object[]{sObject};
								//invoke method!!
								method.invoke(obj,arglist);
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
						}
						else
						{
							try
							{
								method = classInstance.getMethod(saMethod[1],new Class[] {String.class,String.class});
								//Arguments to be passed into method
								sObject=sPage+"."+sObject;
								Object[] arglist = new Object[]{sObject,sData};
								//invoke method!!
								method.invoke(obj,arglist);
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
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
		//Lib.driver.quit();
	}
}