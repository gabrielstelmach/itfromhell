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
package net.itfromhell.howit;

import java.util.regex.Pattern;
import net.itfromhell.howit.jameshook.precommit.PreCommitInterdiction;
import net.itfromhell.howit.jameshook.precommit.PreCommitException;
import net.itfromhell.howit.jameshook.precommit.PreCommitHook;

/**
 * Example of pre-commit hook to validate the name of submitted file.
 * 
 * @author Gabriel Stelmach <gabriel_stelmach@hotmail.com>
 */
public class TickingClockHook extends PreCommitInterdiction implements PreCommitHook
{
	/**
	 * Verify for non-alphabetic characters in filename.
	 * 
	 * @param args Array of arguments used to execute the hook. This hook expect the following sequence of arguments:
	 * <ol>
	 * <li>Fully qualified class name of this hook</li>
	 * <li>Repository's local path</li>
	 * <li>Current SVN transaction</li>
	 * <li>Flag for performed action</li>
	 * <li>File path</li>
	 * </ol>
	 * 
	 * @throws PreCommitException Indicates the use of non-alphabetic character in filename.
	 */
	@Override
	public void validate(String[] args) throws PreCommitException
	{
		//Isolate the file name
		String fileName = args[4];
		log.debug("File name: " + fileName);
		if (fileName.contains("/"))
		{
			fileName = fileName.substring((fileName.lastIndexOf("/") + 1), fileName.length());
		}
		fileName = fileName.substring(0, fileName.indexOf("."));
		
		//Pattern to accept only letters and numbers
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
		
		if (!pattern.matcher(fileName).matches())
		{
			throw new PreCommitException("The file name must use only letters and numbers; " + fileName + " is not valid.");
		}
	}
}