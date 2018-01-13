/**
 * Copyright 2018 Gabriel Stelmach
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the 
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Licensed under the MIT license: https://opensource.org/licenses/MIT
 */
package net.itfromhell.howit.jameshook.precommit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.xml.DOMConfigurator;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.admin.SVNLookClient;

/**
 * Executor for implementation of SVN pre-commit hook.<br>
 * All pre-commit hook must extend this class as well implement the interface <b>PreCommitHook</b>.
 * 
 * @see PreCommitHook
 * @author Gabriel Stelmach <gabriel_stelmach@hotmail.com>
 */
public class PreCommitInterdiction
{
	protected static final Logger log = Logger.getLogger(PreCommitInterdiction.class);
  /**
   * Configuration file for Log4J
   */
  protected static final String LOG4J_CONFIG = "log4j.xml";
  /**
   * Generated log file
   */
  protected static final String LOG4J_LOG_FILE = "PreCommitHook.log";
	/**
	 * Configuration file for SVN
	 */
	protected static final String SVN_CONFIG = "svn.properties";
	/**
	 * Username to access SVN repository
	 */
	private static String username;
	/**
	 * User's password
	 */
	private static String password;
	
  static 
  {
    String executionPath;
    /*
		Defines where we are running
		*/
		try
    {
      File place = new File(PreCommitInterdiction.class.getProtectionDomain().getCodeSource().getLocation().getPath());
      executionPath = place.getPath();
      executionPath = executionPath.substring(0, (executionPath.lastIndexOf(FileSystems.getDefault().getSeparator()) + 1));
    }
    catch (Exception ex)
    {
      //At this point we can`t show any error message
			executionPath = "";
    }
    
		/*
		Loads SVN credentials
		*/
		File svn = new File(executionPath + SVN_CONFIG);
		if ((svn.exists()) && (!svn.isDirectory()))
		{
			try
			{
				InputStream inputStream = new FileInputStream(svn);
				Properties properties = new Properties();
				properties.load(inputStream);
				username = properties.getProperty("svn.authentication.username");
				password = properties.getProperty("svn.authentication.password");
			}
			catch (FileNotFoundException ex)
			{
				System.out.println("SVN configuration file not found! Check for " + executionPath + SVN_CONFIG);
			}
			catch (IOException ex)
			{
				System.out.println("Fail while loading SVN configuration! Check for " + executionPath + SVN_CONFIG);
			}
		}
		
		/*
		Initializes the Log4J
		*/
    File log4j = new File(executionPath + LOG4J_CONFIG);
    if ((log4j.exists()) && (!log4j.isDirectory()))
    {
      //TIP: If you get an error in this line, it means that the place is not accessible (permission rights?) by SVN server's OS user
      DOMConfigurator.configure(log4j.getAbsolutePath());
      try
      {
        //Update the place where log file will be written
        Logger logger = Logger.getRootLogger();
        RollingFileAppender appender = (RollingFileAppender)logger.getAppender("logFilePreCommit");
        appender.setFile(executionPath + LOG4J_LOG_FILE);
        appender.activateOptions();
        log.info("Setup of Log4J is concluded!");
      }
      catch (Exception ex)
      {
        log.error("Unable to setup Log4J properly: " + ex.getMessage(), ex);
      }
    }
  }
  
	/**
	 * Application's entry point to run the validation using pre-commit hook.
	 * 
   * @param args Array of parameters for execution. The first parameter must be the fully qualified class name for hook's implementation.
   */
  public final static void main(String args[])
  {
    //Checking for valid entries
    if (!hasValidArguments(args))
    {
      defineAsInternalError("This execution is invalid. Please, check for pre-commit script and useful messages in your log file " + LOG4J_LOG_FILE);
    }
    else
    {
      //Instantiate the hook
      PreCommitHook hook = createHook(args[0]);
      
      //Executes the validation using the hook
      try
      {
        if (hook != null)
        {
          hook.validate(args);
          printSuccess();
        }
      }
      catch (PreCommitException ex)
      {
        log.error("Could not validate because a pre-commit exception happened: " + ex.getMessage(), ex);
        printFail(ex.getMessage());
      }
      catch (Exception ex)
      {
        log.error("Could not validate because a general exception happened: " + ex.getMessage(), ex);
        defineAsInternalError(ex.getMessage());
      }
    }
    log.debug("...end!");
  }
	
	/**
	 * Verify for very needed parameters to execute the hook.<br>
	 * Expected parameters:
	 * <ol>
	 * <li>Fully qualified name of hook's implementation class</li>
	 * <li>Path of local repository</li>
	 * <li>SVN transaction name</li>
	 * </ol>
   * 
   * @param args Array of execution parameters
   * @return True when the list of parameters seems to be valid
   */
  private static boolean hasValidArguments(String args[])
  {
    if (args.length < 3)
    {
      log.warn("No parameters enough supplied. At least three are expected.");
      return false;
    }
    if (!args[0].contains("."))
    {
      log.warn("The first parameter expected must be the Class for implementation of hook, however, what I got is \"" + args[0] + "\".");
      return false;
    }
    
    return true;
  }
	
	/**
	 * Creates the hook's instance to execution.
   * 
   * @param className Fully qualified name of hook implementation class
   * @return Hook's instance
   */
  private static PreCommitHook createHook(String className)
  {
    log.debug("Implementation: " + className);
    
    PreCommitHook hook = null;
    try
    {
      Class<?> clazz = Class.forName(className);
      Constructor<?> constructor = clazz.getConstructor();
      hook = (PreCommitHook)constructor.newInstance();
    }
    catch (ClassNotFoundException ex)
    {
      log.error("ClassNotFoundException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());
    }
    catch (NoSuchMethodException ex)
    {
      log.error("NoSuchMethodException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());      
    }
    catch (SecurityException ex)
    {
      log.error("SecurityException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());
    }
    catch (InstantiationException ex)
    {
      log.error("InstantiationException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());
    }
    catch (IllegalAccessException ex)
    {
      log.error("IllegalAccessException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());
    }
    catch (IllegalArgumentException ex)
    {
      log.error("IllegalArgumentException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());
    }
    catch (InvocationTargetException ex)
    {
      log.error("InvocationTargetException while instantiating the hook: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());
    }
    catch (Exception ex)
    {
      log.error("Exception while taking the instance: " + ex.getMessage(), ex);
      defineAsInternalError(ex.getMessage());      
    }
    
    return hook;
  }
	
  /**
	 * Loads contents of the submitted file. <br>
	 * The contents read is just before the commitment, therefore, it is not under version control yet.
   * 
   * @param repositoryPath Full system path to local SVN repository
   * @param username User with reading permission granted to SVN
   * @param password User's password
   * @param transaction SVN's pre-commit transaction identification
   * @param filePath Path of the file in process
   * @return The submitted file contents on transaction of pre-commit
   * @throws PreCommitException It happens when is not possible to connect to SVN or the file's contents is not reachable
   */
  protected String loadFileContents(String repositoryPath, String username, String password, String transaction, String filePath) throws PreCommitException
  {
    File repo = null;
    SVNLookClient svnLook = null;
    String contents = null;
    try
    {
      repo = new File(repositoryPath);
      ISVNAuthenticationManager authenticationManager = BasicAuthenticationManager.newInstance(username, password.toCharArray());
      ISVNOptions svnOptions = new DefaultSVNOptions();
      svnLook = new SVNLookClient(authenticationManager, svnOptions);
    }
    catch (Exception ex)
    {
      log.error("Fail while connecting to SVN using \"" + username +  ": " + ex.getMessage(), ex);
      throw new PreCommitException("Unable to connect to SVN: " + ex.getMessage());
    }
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      svnLook.doCat(repo, filePath, transaction, baos);
      contents = baos.toString();
    }
    catch (SVNException ex)
    {
      log.error("Fail while loading contents of \"" + filePath + "\": " + ex.getMessage(), ex);
      throw new PreCommitException("Unable to load contents of submitted file: " + ex.getMessage());
    }
    if (contents == null)
    {
      throw new PreCommitException("Contents loaded from file is invalid.");
    }
    
    return contents;
  }
  
  /**
   * Exit sign of execution as success.
   */
  private static void printSuccess()
  {
    log.trace("Validation concluded!");
    System.out.print("0");
  }
  
  /**
	 * Exit sign of execution as fail.<br>
	 * As OS definition, any execution with exit code greater than zero (<b>0</b>) is a failed process. In this case, the application will be 
	 * successfully terminated. However, to set a failed execution to pre-commit script's flow control, we need print a positive number on console.
   * 
   * @param message Fail message
   */
  private static void printFail(String message)
  {
    log.trace("Validation failed: " + message);
    System.out.print("1 " + message);
  }
  
  /**
   * Indicates that an internal error occurred while executing the hook.
   * 
   * @param message Error message
   */
  private static void defineAsInternalError(String message)
  {
    printFail("Internal error while hooking: " + message);
  }

	public static String getUsername()
	{
		return username;
	}

	public static String getPassword()
	{
		return password;
	}
}